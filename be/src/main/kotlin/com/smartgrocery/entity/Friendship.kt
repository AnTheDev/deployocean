package com.smartgrocery.entity

import jakarta.persistence.*

enum class FriendshipStatus {
    PENDING,    // Đang chờ chấp nhận
    ACCEPTED,   // Đã chấp nhận
    REJECTED,   // Đã từ chối
    BLOCKED     // Đã chặn
}

@Entity
@Table(name = "friendships")
class Friendship(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    var requester: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addressee_id", nullable = false)
    var addressee: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    var status: FriendshipStatus = FriendshipStatus.PENDING
) : BaseEntity()

