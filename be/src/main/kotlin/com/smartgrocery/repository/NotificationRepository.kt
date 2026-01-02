package com.smartgrocery.repository

import com.smartgrocery.entity.Notification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<Notification, Long> {
    
    fun findByUserIdOrderByCreatedAtDesc(userId: Long, pageable: Pageable): Page<Notification>
    
    fun findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId: Long, pageable: Pageable): Page<Notification>
    
    fun countByUserId(userId: Long): Long
    
    fun countByUserIdAndIsReadFalse(userId: Long): Long
    
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = CURRENT_TIMESTAMP WHERE n.user.id = :userId AND n.isRead = false")
    fun markAllAsRead(userId: Long): Int
    
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = CURRENT_TIMESTAMP WHERE n.id IN :ids AND n.user.id = :userId")
    fun markAsRead(ids: List<Long>, userId: Long): Int
    
    fun findByIdAndUserId(id: Long, userId: Long): Notification?
}
