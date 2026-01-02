package com.smartgrocery.entity

import jakarta.persistence.*

@Entity
@Table(name = "notifications")
class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false, length = 1000)
    var message: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: NotificationType,

    @Column(name = "reference_type")
    var referenceType: String? = null,

    @Column(name = "reference_id")
    var referenceId: Long? = null,

    @Column(name = "is_read", nullable = false)
    var isRead: Boolean = false,

    @Column(name = "read_at")
    var readAt: java.time.Instant? = null
) : BaseEntity()

enum class NotificationType {
    GENERAL,
    FAMILY_INVITE,
    FRIEND_REQUEST,
    FRIDGE_EXPIRY,
    SHOPPING_REMINDER,
    MEAL_PLAN
}
