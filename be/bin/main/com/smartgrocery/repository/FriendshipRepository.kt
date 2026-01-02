package com.smartgrocery.repository

import com.smartgrocery.entity.Friendship
import com.smartgrocery.entity.FriendshipStatus
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FriendshipRepository : JpaRepository<Friendship, Long> {

    @EntityGraph(attributePaths = ["requester", "addressee"])
    @Query("SELECT f FROM Friendship f WHERE f.requester.id = :userId OR f.addressee.id = :userId")
    fun findAllByUserIdWithUsers(@Param("userId") userId: Long): List<Friendship>

    @EntityGraph(attributePaths = ["requester", "addressee"])
    @Query("""
        SELECT f FROM Friendship f 
        WHERE (f.requester.id = :userId OR f.addressee.id = :userId) 
        AND f.status = :status
    """)
    fun findByUserIdAndStatusWithUsers(
        @Param("userId") userId: Long,
        @Param("status") status: FriendshipStatus
    ): List<Friendship>

    @EntityGraph(attributePaths = ["requester", "addressee"])
    @Query("SELECT f FROM Friendship f WHERE f.addressee.id = :userId AND f.status = 'PENDING'")
    fun findPendingRequestsForUser(@Param("userId") userId: Long): List<Friendship>

    @EntityGraph(attributePaths = ["requester", "addressee"])
    @Query("SELECT f FROM Friendship f WHERE f.requester.id = :userId AND f.status = 'PENDING'")
    fun findSentRequestsByUser(@Param("userId") userId: Long): List<Friendship>

    @Query("""
        SELECT f FROM Friendship f 
        WHERE ((f.requester.id = :userId1 AND f.addressee.id = :userId2) 
        OR (f.requester.id = :userId2 AND f.addressee.id = :userId1))
    """)
    fun findByUserPair(
        @Param("userId1") userId1: Long,
        @Param("userId2") userId2: Long
    ): Friendship?

    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END 
        FROM Friendship f 
        WHERE ((f.requester.id = :userId1 AND f.addressee.id = :userId2) 
        OR (f.requester.id = :userId2 AND f.addressee.id = :userId1))
        AND f.status = 'ACCEPTED'
    """)
    fun areFriends(
        @Param("userId1") userId1: Long,
        @Param("userId2") userId2: Long
    ): Boolean

    @Query("""
        SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END 
        FROM Friendship f 
        WHERE (f.requester.id = :userId1 AND f.addressee.id = :userId2) 
        OR (f.requester.id = :userId2 AND f.addressee.id = :userId1)
    """)
    fun existsByUserPair(
        @Param("userId1") userId1: Long,
        @Param("userId2") userId2: Long
    ): Boolean

    @EntityGraph(attributePaths = ["requester", "addressee"])
    @Query("SELECT f FROM Friendship f WHERE f.id = :id")
    fun findByIdWithUsers(@Param("id") id: Long): Friendship?
}

