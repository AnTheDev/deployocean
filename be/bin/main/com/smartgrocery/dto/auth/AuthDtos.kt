package com.smartgrocery.dto.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:NotBlank(message = "Username is required")
    val username: String,

    @field:NotBlank(message = "Password is required")
    val password: String
)

data class RegisterRequest(
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    val username: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    val password: String,

    @field:NotBlank(message = "Full name is required")
    @field:Size(max = 100, message = "Full name must not exceed 100 characters")
    val fullName: String
)

data class RefreshTokenRequest(
    @field:NotBlank(message = "Refresh token is required")
    val refreshToken: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val user: UserResponse
)

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val fullName: String,
    val avatarUrl: String? = null,
    val isActive: Boolean,
    val roles: List<String>
)

data class UpdateProfileRequest(
    @field:Size(max = 100, message = "Full name must not exceed 100 characters")
    val fullName: String? = null,

    @field:Email(message = "Invalid email format")
    val email: String? = null,

    val fcmToken: String? = null
)

data class ChangePasswordRequest(
    @field:NotBlank(message = "Current password is required")
    val currentPassword: String,

    @field:NotBlank(message = "New password is required")
    @field:Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    val newPassword: String
)

