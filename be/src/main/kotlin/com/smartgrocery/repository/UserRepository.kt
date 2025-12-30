package com.smartgrocery.repository

import com.smartgrocery.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    
    fun findByUsername(username: String): User?
    
    fun findByEmail(email: String): User?
    
    fun existsByUsername(username: String): Boolean
    
    fun existsByEmail(email: String): Boolean

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    fun findByUsernameWithRoles(username: String): User?

    @Query("SELECT u FROM User u WHERE u.fcmToken IS NOT NULL AND u.isActive = true")
    fun findAllWithFcmToken(): List<User>

    @Query(
        value = """
            SELECT * FROM users u 
            WHERE u.is_active = true 
            AND u.id != :excludeUserId
            AND (
                LOWER(u.username) LIKE LOWER('%' || :keyword || '%')
                OR LOWER(u.full_name) LIKE LOWER('%' || :keyword || '%')
                OR LOWER(u.email) LIKE LOWER('%' || :keyword || '%')
            )
            ORDER BY u.full_name ASC
        """,
        countQuery = """
            SELECT COUNT(*) FROM users u 
            WHERE u.is_active = true 
            AND u.id != :excludeUserId
            AND (
                LOWER(u.username) LIKE LOWER('%' || :keyword || '%')
                OR LOWER(u.full_name) LIKE LOWER('%' || :keyword || '%')
                OR LOWER(u.email) LIKE LOWER('%' || :keyword || '%')
            )
        """,
        nativeQuery = true
    )
    fun searchUsers(
        @Param("keyword") keyword: String,
        @Param("excludeUserId") excludeUserId: Long,
        pageable: Pageable
    ): Page<User>

    // Search với keyword đã được xử lý (cho phép search linh hoạt hơn)
    @Query(
        value = """
            SELECT * FROM users u 
            WHERE u.is_active = true 
            AND u.id != :excludeUserId
            AND (
                LOWER(u.username) LIKE :pattern
                OR LOWER(u.full_name) LIKE :pattern
                OR LOWER(u.email) LIKE :pattern
            )
            ORDER BY 
                CASE 
                    WHEN LOWER(u.username) = LOWER(:exactKeyword) THEN 0
                    WHEN LOWER(u.username) LIKE LOWER(:exactKeyword) || '%' THEN 1
                    WHEN LOWER(u.full_name) = LOWER(:exactKeyword) THEN 2
                    WHEN LOWER(u.full_name) LIKE LOWER(:exactKeyword) || '%' THEN 3
                    ELSE 4
                END,
                u.full_name ASC
        """,
        countQuery = """
            SELECT COUNT(*) FROM users u 
            WHERE u.is_active = true 
            AND u.id != :excludeUserId
            AND (
                LOWER(u.username) LIKE :pattern
                OR LOWER(u.full_name) LIKE :pattern
                OR LOWER(u.email) LIKE :pattern
            )
        """,
        nativeQuery = true
    )
    fun searchUsersAdvanced(
        @Param("pattern") pattern: String,
        @Param("exactKeyword") exactKeyword: String,
        @Param("excludeUserId") excludeUserId: Long,
        pageable: Pageable
    ): Page<User>
}

