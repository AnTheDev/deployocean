package com.smartgrocery.dto.family

import com.smartgrocery.dto.auth.UserResponse
import com.smartgrocery.entity.FamilyRole
import com.smartgrocery.entity.InvitationStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import java.time.Instant

// DTO cho việc tạo family với multipart form
data class CreateFamilyRequest(
    val name: String,
    val description: String? = null,
    val friendIds: List<Long> = emptyList()  // Danh sách bạn bè cần mời
)

data class UpdateFamilyRequest(
    @field:Size(max = 100, message = "Family name must not exceed 100 characters")
    val name: String? = null,

    @field:Size(max = 255, message = "Description must not exceed 255 characters")
    val description: String? = null
)

data class JoinFamilyRequest(
    @field:NotBlank(message = "Invite code is required")
    val inviteCode: String,

    @field:Size(max = 50, message = "Nickname must not exceed 50 characters")
    val nickname: String? = null
)

data class FamilyResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val inviteCode: String,
    val createdBy: UserSimpleResponse,
    val memberCount: Int,
    val createdAt: Instant
)

data class FamilyDetailResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val inviteCode: String,
    val createdBy: UserSimpleResponse,
    val members: List<FamilyMemberResponse>,
    val pendingInvitations: List<FamilyInvitationResponse>? = null,
    val createdAt: Instant
)

data class FamilyInvitationResponse(
    val id: Long,
    val familyId: Long,
    val familyName: String,
    val inviter: UserSimpleResponse,
    val invitee: UserSimpleResponse,
    val status: InvitationStatus,
    val createdAt: Instant
)

data class FamilyMemberResponse(
    val userId: Long,
    val username: String,
    val fullName: String,
    val email: String,
    val role: FamilyRole,
    val nickname: String?,
    val joinedAt: Instant
)

data class UserSimpleResponse(
    val id: Long,
    val username: String,
    val fullName: String
)

data class UpdateMemberRequest(
    val nickname: String? = null,
    val role: FamilyRole? = null
)

data class RegenerateInviteCodeResponse(
    val inviteCode: String
)

data class RespondToInvitationRequest(
    val accept: Boolean
)

