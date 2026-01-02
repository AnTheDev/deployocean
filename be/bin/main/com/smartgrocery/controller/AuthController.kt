package com.smartgrocery.controller

import com.smartgrocery.dto.auth.*
import com.smartgrocery.dto.common.ApiResponse
import com.smartgrocery.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication and user management APIs")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    fun register(
        @Valid @RequestBody request: RegisterRequest
    ): ResponseEntity<ApiResponse<AuthResponse>> {
        val response = authService.register(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(response, "User registered successfully"))
    }

    @PostMapping("/login")
    @Operation(summary = "Login with username and password")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): ResponseEntity<ApiResponse<AuthResponse>> {
        val response = authService.login(request)
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"))
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using refresh token")
    fun refreshToken(
        @Valid @RequestBody request: RefreshTokenRequest
    ): ResponseEntity<ApiResponse<AuthResponse>> {
        val response = authService.refreshToken(request)
        return ResponseEntity.ok(ApiResponse.success(response, "Token refreshed successfully"))
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user information")
    fun getCurrentUser(): ResponseEntity<ApiResponse<UserResponse>> {
        val response = authService.getCurrentUser()
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @PatchMapping("/me")
    @Operation(summary = "Update current user profile")
    fun updateProfile(
        @Valid @RequestBody request: UpdateProfileRequest
    ): ResponseEntity<ApiResponse<UserResponse>> {
        val response = authService.updateProfile(request)
        return ResponseEntity.ok(ApiResponse.success(response, "Profile updated successfully"))
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change current user password")
    fun changePassword(
        @Valid @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        authService.changePassword(request)
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"))
    }

    @PostMapping("/me/avatar", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "Upload or update user avatar")
    fun uploadAvatar(
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<ApiResponse<UserResponse>> {
        val response = authService.uploadAvatar(file)
        return ResponseEntity.ok(ApiResponse.success(response, "Avatar uploaded successfully"))
    }

    @DeleteMapping("/me/avatar")
    @Operation(summary = "Delete user avatar")
    fun deleteAvatar(): ResponseEntity<ApiResponse<UserResponse>> {
        val response = authService.deleteAvatar()
        return ResponseEntity.ok(ApiResponse.success(response, "Avatar deleted successfully"))
    }
}

