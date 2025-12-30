package com.smartgrocery.entity

import jakarta.persistence.*
import java.time.Instant

enum class InvitationStatus {
    PENDING,    // Đang chờ phản hồi
    ACCEPTED,   // Đã chấp nhận
    REJECTED    // Đã từ chối
}

@Entity
@Table(name = "family_invitations")
class FamilyInvitation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    var family: Family,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id", nullable = false)
    var inviter: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitee_id", nullable = false)
    var invitee: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    var status: InvitationStatus = InvitationStatus.PENDING,

    @Column(name = "responded_at")
    var respondedAt: Instant? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now()
)

