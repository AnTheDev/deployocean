package com.smartgrocery.repository

import com.smartgrocery.entity.Family
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FamilyRepository : JpaRepository<Family, Long> {
    
    @EntityGraph(attributePaths = ["createdBy"])
    @Query("SELECT f FROM Family f WHERE f.inviteCode = :inviteCode")
    fun findByInviteCodeWithCreatedBy(inviteCode: String): Family?
    
    fun existsByInviteCode(inviteCode: String): Boolean

    @Query("SELECT DISTINCT f FROM Family f LEFT JOIN FETCH f.members m LEFT JOIN FETCH m.user LEFT JOIN FETCH f.createdBy WHERE f.id = :id")
    fun findByIdWithMembers(id: Long): Family?

    @EntityGraph(attributePaths = ["createdBy"])
    @Query("""
        SELECT DISTINCT f FROM Family f 
        JOIN f.members m 
        WHERE m.user.id = :userId
    """)
    fun findAllByUserIdWithCreatedBy(userId: Long): List<Family>
}

