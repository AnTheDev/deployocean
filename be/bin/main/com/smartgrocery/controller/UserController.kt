package com.smartgrocery.controller

import com.smartgrocery.dto.common.ApiResponse
import com.smartgrocery.dto.common.PageResponse
import com.smartgrocery.dto.user.UserProfileResponse
import com.smartgrocery.dto.user.UserSearchResponse
import com.smartgrocery.entity.FriendshipStatus
import com.smartgrocery.repository.FriendshipRepository
import com.smartgrocery.repository.UserRepository
import com.smartgrocery.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management APIs")
class UserController(
    private val userRepository: UserRepository,
    private val friendshipRepository: FriendshipRepository
) {

    @GetMapping("/search")
    @Operation(summary = "Search users by username, full name, or email")
    fun searchUsers(
        @RequestParam keyword: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<PageResponse<UserSearchResponse>>> {
        val currentUser = getCurrentUser()
        
        val trimmedKeyword = keyword.trim()
        
        // Cho phép search từ 1 ký tự trở lên
        if (trimmedKeyword.isBlank()) {
            return ResponseEntity.ok(
                ApiResponse.success(
                    PageResponse(
                        content = emptyList(),
                        page = page,
                        size = size,
                        totalElements = 0,
                        totalPages = 0,
                        first = true,
                        last = true
                    )
                )
            )
        }

        val pageable = PageRequest.of(page, size)
        
        // Tạo pattern cho LIKE query - thêm % ở đầu và cuối
        val searchPattern = "%${trimmedKeyword.lowercase()}%"
        
        val usersPage = userRepository.searchUsersAdvanced(
            pattern = searchPattern,
            exactKeyword = trimmedKeyword,
            excludeUserId = currentUser.id,
            pageable = pageable
        )

        val userResponses = usersPage.content.map { user ->
            val friendship = friendshipRepository.findByUserPair(currentUser.id, user.id!!)
            UserSearchResponse(
                id = user.id!!,
                username = user.username,
                fullName = user.fullName,
                email = user.email,
                isFriend = friendship?.status == FriendshipStatus.ACCEPTED,
                friendshipStatus = friendship?.status?.name
            )
        }

        val response = PageResponse(
            content = userResponses,
            page = usersPage.number,
            size = usersPage.size,
            totalElements = usersPage.totalElements,
            totalPages = usersPage.totalPages,
            first = usersPage.isFirst,
            last = usersPage.isLast
        )

        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user profile by ID")
    fun getUserById(@PathVariable id: Long): ResponseEntity<ApiResponse<UserProfileResponse>> {
        val user = userRepository.findById(id)
            .orElseThrow { com.smartgrocery.exception.ResourceNotFoundException(
                com.smartgrocery.exception.ErrorCode.USER_NOT_FOUND
            ) }

        val response = UserProfileResponse(
            id = user.id!!,
            username = user.username,
            fullName = user.fullName,
            email = user.email,
            createdAt = user.createdAt
        )

        return ResponseEntity.ok(ApiResponse.success(response))
    }

    private fun getCurrentUser(): CustomUserDetails {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.principal as CustomUserDetails
    }
}

