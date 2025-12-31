package com.smartgrocery.service

import com.smartgrocery.config.JwtConfig
import com.smartgrocery.dto.auth.*
import com.smartgrocery.entity.User
import com.smartgrocery.exception.*
import com.smartgrocery.repository.RoleRepository
import com.smartgrocery.repository.UserRepository
import com.smartgrocery.security.CustomUserDetails
import com.smartgrocery.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtConfig: JwtConfig,
    private val fileStorageService: FileStorageService
) {

    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        // Check if username exists
        if (userRepository.existsByUsername(request.username)) {
            throw ConflictException(ErrorCode.USERNAME_ALREADY_EXISTS)
        }

        // Check if email exists
        if (userRepository.existsByEmail(request.email)) {
            throw ConflictException(ErrorCode.EMAIL_ALREADY_EXISTS)
        }

        // Get default USER role
        val userRole =
            roleRepository.findByName("USER") ?: throw ApiException(ErrorCode.INTERNAL_ERROR, "Default role not found")

        // Create new user
        val user = User(
            username = request.username,
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password),
            fullName = request.fullName,
            roles = mutableSetOf(userRole)
        )

        val savedUser = userRepository.save(user)

        // Generate tokens
        val accessToken = jwtTokenProvider.generateAccessToken(savedUser.username)
        val refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.username)

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtConfig.accessTokenExpiration,
            user = toUserResponse(savedUser)
        )
    }

    fun login(request: LoginRequest): AuthResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )

        SecurityContextHolder.getContext().authentication = authentication

        val userDetails = authentication.principal as CustomUserDetails
        val user = userRepository.findByUsernameWithRoles(request.username)
            ?: throw AuthenticationException(ErrorCode.USER_NOT_FOUND)

        if (!user.isActive) {
            throw AuthenticationException(ErrorCode.ACCOUNT_DISABLED)
        }

        val accessToken = jwtTokenProvider.generateAccessToken(authentication)
        val refreshToken = jwtTokenProvider.generateRefreshToken(user.username)

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtConfig.accessTokenExpiration,
            user = toUserResponse(user)
        )
    }

    fun refreshToken(request: RefreshTokenRequest): AuthResponse {
        if (!jwtTokenProvider.validateToken(request.refreshToken)) {
            throw AuthenticationException(ErrorCode.TOKEN_INVALID)
        }

        if (!jwtTokenProvider.isRefreshToken(request.refreshToken)) {
            throw AuthenticationException(ErrorCode.TOKEN_INVALID, "Invalid refresh token")
        }

        val username = jwtTokenProvider.getUsernameFromToken(request.refreshToken)
        val user =
            userRepository.findByUsernameWithRoles(username) ?: throw AuthenticationException(ErrorCode.USER_NOT_FOUND)

        if (!user.isActive) {
            throw AuthenticationException(ErrorCode.ACCOUNT_DISABLED)
        }

        val accessToken = jwtTokenProvider.generateAccessToken(username)
        val refreshToken = jwtTokenProvider.generateRefreshToken(username)

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtConfig.accessTokenExpiration,
            user = toUserResponse(user)
        )
    }

    fun getCurrentUser(): UserResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as CustomUserDetails

        val user = userRepository.findByUsernameWithRoles(userDetails.username) ?: throw ResourceNotFoundException(
            ErrorCode.USER_NOT_FOUND
        )

        return toUserResponse(user)
    }

    @Transactional
    fun updateProfile(request: UpdateProfileRequest): UserResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as CustomUserDetails

        val user =
            userRepository.findById(userDetails.id).orElseThrow { ResourceNotFoundException(ErrorCode.USER_NOT_FOUND) }

        request.fullName?.let { user.fullName = it }
        request.email?.let {
            if (it != user.email && userRepository.existsByEmail(it)) {
                throw ConflictException(ErrorCode.EMAIL_ALREADY_EXISTS)
            }
            user.email = it
        }
        request.fcmToken?.let { user.fcmToken = it }

        val savedUser = userRepository.save(user)
        return toUserResponse(savedUser)
    }

    @Transactional
    fun changePassword(request: ChangePasswordRequest) {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as CustomUserDetails

        val user =
            userRepository.findById(userDetails.id).orElseThrow { ResourceNotFoundException(ErrorCode.USER_NOT_FOUND) }

        if (!passwordEncoder.matches(request.currentPassword, user.passwordHash)) {
            throw ApiException(ErrorCode.PASSWORD_MISMATCH)
        }

        user.passwordHash = passwordEncoder.encode(request.newPassword)
        userRepository.save(user)
    }

    @Transactional
    fun uploadAvatar(file: MultipartFile): UserResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as CustomUserDetails

        val user = userRepository.findById(userDetails.id)
            .orElseThrow { ResourceNotFoundException(ErrorCode.USER_NOT_FOUND) }

        // Delete old avatar if exists
        user.avatarUrl?.let { oldPath ->
            fileStorageService.deleteFile(oldPath)
        }

        // Store new avatar
        val avatarPath = fileStorageService.storeFile(file, "users")
        user.avatarUrl = avatarPath

        val savedUser = userRepository.save(user)
        return toUserResponse(savedUser)
    }

    @Transactional
    fun deleteAvatar(): UserResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as CustomUserDetails

        val user = userRepository.findById(userDetails.id)
            .orElseThrow { ResourceNotFoundException(ErrorCode.USER_NOT_FOUND) }

        // Delete avatar file if exists
        user.avatarUrl?.let { oldPath ->
            fileStorageService.deleteFile(oldPath)
        }

        user.avatarUrl = null

        val savedUser = userRepository.save(user)
        return toUserResponse(savedUser)
    }

    private fun toUserResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id!!,
            username = user.username,
            email = user.email,
            fullName = user.fullName,
            avatarUrl = user.avatarUrl?.let { "/files/$it" },
            isActive = user.isActive,
            roles = user.roles.map { it.name })
    }
}

