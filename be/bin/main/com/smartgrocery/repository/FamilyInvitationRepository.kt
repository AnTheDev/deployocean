package com.smartgrocery.repository

import com.smartgrocery.entity.FamilyInvitation
import com.smartgrocery.entity.InvitationStatus
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FamilyInvitationRepository : JpaRepository<FamilyInvitation, Long> {

    @EntityGraph(attributePaths = ["family", "inviter", "invitee", "family.createdBy"])
    @Query("SELECT i FROM FamilyInvitation i WHERE i.invitee.id = :userId AND i.status = 'PENDING'")
    fun findPendingInvitationsForUser(@Param("userId") userId: Long): List<FamilyInvitation>

    @EntityGraph(attributePaths = ["family", "inviter", "invitee", "family.createdBy"])
    @Query("SELECT i FROM FamilyInvitation i WHERE i.family.id = :familyId")
    fun findByFamilyIdWithDetails(@Param("familyId") familyId: Long): List<FamilyInvitation>

    fun findByFamilyIdAndInviteeId(familyId: Long, inviteeId: Long): FamilyInvitation?

    fun existsByFamilyIdAndInviteeIdAndStatus(
        familyId: Long,
        inviteeId: Long,
        status: InvitationStatus
    ): Boolean

    @EntityGraph(attributePaths = ["family", "inviter", "invitee", "family.createdBy"])
    override fun findById(id: Long): java.util.Optional<FamilyInvitation>
}

