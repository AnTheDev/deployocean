package com.smartgrocery.dto.user

import java.time.Instant

data class UserSearchResponse(
    val id: Long,
    val username: String,
    val fullName: String,
    val email: String,
    val isFriend: Boolean,
    val friendshipStatus: String? // PENDING, ACCEPTED, null
)

data class UserProfileResponse(
    val id: Long,
    val username: String,
    val fullName: String,
    val email: String,
    val createdAt: Instant
)

