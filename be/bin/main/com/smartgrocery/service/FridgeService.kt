package com.smartgrocery.service

import com.smartgrocery.dto.common.PageResponse
import com.smartgrocery.dto.family.UserSimpleResponse
import com.smartgrocery.dto.fridge.*
import com.smartgrocery.dto.product.ProductSimpleResponse
import com.smartgrocery.entity.FridgeItem
import com.smartgrocery.entity.FridgeItemStatus
import com.smartgrocery.entity.FridgeLocation
import com.smartgrocery.exception.*
import com.smartgrocery.repository.*
import com.smartgrocery.security.CustomUserDetails
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class FridgeService(
    private val fridgeItemRepository: FridgeItemRepository,
    private val familyRepository: FamilyRepository,
    private val familyMemberRepository: FamilyMemberRepository,
    private val userRepository: UserRepository,
    private val productRepository: MasterProductRepository
) {

    @Transactional
    fun addFridgeItem(request: CreateFridgeItemRequest): FridgeItemResponse {
        val currentUser = getCurrentUser()
        checkFamilyMembership(request.familyId, currentUser.id)

        if (request.masterProductId == null && request.customProductName.isNullOrBlank()) {
            throw ValidationException("Either master product or custom product name must be specified")
        }

        val family = familyRepository.findById(request.familyId)
            .orElseThrow { ResourceNotFoundException(ErrorCode.FAMILY_NOT_FOUND) }

        val user = userRepository.findById(currentUser.id)
            .orElseThrow { ResourceNotFoundException(ErrorCode.USER_NOT_FOUND) }

        val masterProduct = request.masterProductId?.let {
            productRepository.findById(it)
                .orElseThrow { ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND) }
        }

        val fridgeItem = FridgeItem(
            family = family,
            masterProduct = masterProduct,
            customProductName = request.customProductName,
            quantity = request.quantity,
            unit = request.unit,
            expirationDate = request.expirationDate,
            location = request.location,
            note = request.note,
            addedBy = user
        )

        val savedItem = fridgeItemRepository.save(fridgeItem)
        return toResponse(savedItem)
    }

    fun getFridgeItemsByFamily(
        familyId: Long,
        location: FridgeLocation? = null,
        status: FridgeItemStatus? = null,
        expiringWithinDays: Int? = null,
        pageable: Pageable
    ): PageResponse<FridgeItemResponse> {
        val currentUser = getCurrentUser()
        checkFamilyMembership(familyId, currentUser.id)

        // Calculate expiration threshold date if filtering by expiring items
        val today = if (expiringWithinDays != null) LocalDate.now() else null
        val thresholdDate = expiringWithinDays?.let { LocalDate.now().plusDays(it.toLong()) }

        // Query with filters at database level for correct pagination
        val page = fridgeItemRepository.findByFamilyIdWithFilters(
            familyId = familyId,
            location = location,
            status = status,
            today = today,
            thresholdDate = thresholdDate,
            pageable = pageable
        )

        return PageResponse.from(page) { toResponse(it) }
    }

    fun getActiveFridgeItems(familyId: Long): List<FridgeItemResponse> {
        val currentUser = getCurrentUser()
        checkFamilyMembership(familyId, currentUser.id)

        return fridgeItemRepository.findActiveByFamilyIdWithDetails(familyId)
            .map { toResponse(it) }
    }

    fun getFridgeItemById(itemId: Long): FridgeItemResponse {
        val currentUser = getCurrentUser()
        val item = fridgeItemRepository.findById(itemId)
            .orElseThrow { ResourceNotFoundException(ErrorCode.FRIDGE_ITEM_NOT_FOUND) }

        checkFamilyMembership(item.family.id!!, currentUser.id)
        return toResponse(item)
    }

    @Transactional
    fun updateFridgeItem(itemId: Long, request: UpdateFridgeItemRequest): FridgeItemResponse {
        val currentUser = getCurrentUser()
        val item = fridgeItemRepository.findById(itemId)
            .orElseThrow { ResourceNotFoundException(ErrorCode.FRIDGE_ITEM_NOT_FOUND) }

        checkFamilyMembership(item.family.id!!, currentUser.id)

        request.quantity?.let { item.quantity = it }
        request.unit?.let { item.unit = it }
        request.expirationDate?.let { item.expirationDate = it }
        request.location?.let { item.location = it }
        request.status?.let { item.status = it }
        request.note?.let { item.note = it }

        val savedItem = fridgeItemRepository.save(item)
        return toResponse(savedItem)
    }

    @Transactional
    fun consumeItem(itemId: Long, request: ConsumeItemRequest): FridgeItemResponse {
        val currentUser = getCurrentUser()
        val item = fridgeItemRepository.findById(itemId)
            .orElseThrow { ResourceNotFoundException(ErrorCode.FRIDGE_ITEM_NOT_FOUND) }

        checkFamilyMembership(item.family.id!!, currentUser.id)

        if (request.quantity > item.quantity) {
            throw ApiException(ErrorCode.INSUFFICIENT_QUANTITY)
        }

        item.quantity = item.quantity.subtract(request.quantity)

        if (item.quantity <= BigDecimal.ZERO) {
            item.status = FridgeItemStatus.CONSUMED
        }

        val savedItem = fridgeItemRepository.save(item)
        return toResponse(savedItem)
    }

    @Transactional
    fun discardItem(itemId: Long): FridgeItemResponse {
        val currentUser = getCurrentUser()
        val item = fridgeItemRepository.findById(itemId)
            .orElseThrow { ResourceNotFoundException(ErrorCode.FRIDGE_ITEM_NOT_FOUND) }

        checkFamilyMembership(item.family.id!!, currentUser.id)

        item.status = FridgeItemStatus.DISCARDED
        val savedItem = fridgeItemRepository.save(item)
        return toResponse(savedItem)
    }

    @Transactional
    fun deleteFridgeItem(itemId: Long) {
        val currentUser = getCurrentUser()
        val item = fridgeItemRepository.findById(itemId)
            .orElseThrow { ResourceNotFoundException(ErrorCode.FRIDGE_ITEM_NOT_FOUND) }

        checkFamilyMembership(item.family.id!!, currentUser.id)
        fridgeItemRepository.delete(item)
    }

    fun getExpiringItems(familyId: Long, daysThreshold: Int = 3): List<FridgeItemResponse> {
        val currentUser = getCurrentUser()
        checkFamilyMembership(familyId, currentUser.id)

        val today = LocalDate.now()
        val thresholdDate = today.plusDays(daysThreshold.toLong())

        return fridgeItemRepository.findExpiringSoonWithDetails(today, thresholdDate)
            .filter { it.family.id == familyId }
            .map { toResponse(it) }
    }

    fun getExpiredItems(familyId: Long): List<FridgeItemResponse> {
        val currentUser = getCurrentUser()
        checkFamilyMembership(familyId, currentUser.id)

        val today = LocalDate.now()
        return fridgeItemRepository.findExpiredWithDetails(today)
            .filter { it.family.id == familyId }
            .map { toResponse(it) }
    }

    fun getFridgeStatistics(familyId: Long): FridgeStatisticsResponse {
        val currentUser = getCurrentUser()
        checkFamilyMembership(familyId, currentUser.id)

        val items = fridgeItemRepository.findActiveByFamilyIdWithDetails(familyId)
        val today = LocalDate.now()
        val thresholdDate = today.plusDays(3)

        val expiringSoon = items.count { it.isExpiringSoon(3) }
        val expired = items.count { it.isExpired() }
        val byLocation = items.groupBy { it.location }.mapValues { it.value.size }
        val byStatus = items.groupBy { it.status }.mapValues { it.value.size }

        return FridgeStatisticsResponse(
            totalItems = items.size,
            expiringSoonCount = expiringSoon,
            expiredCount = expired,
            itemsByLocation = byLocation,
            itemsByStatus = byStatus
        )
    }

    private fun checkFamilyMembership(familyId: Long, userId: Long) {
        if (!familyMemberRepository.existsByFamilyIdAndUserId(familyId, userId)) {
            throw ForbiddenException("You are not a member of this family")
        }
    }

    private fun getCurrentUser(): CustomUserDetails {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.principal as CustomUserDetails
    }

    private fun toResponse(item: FridgeItem): FridgeItemResponse {
        val today = LocalDate.now()
        val daysUntilExpiration = item.expirationDate?.let {
            ChronoUnit.DAYS.between(today, it)
        }

        return FridgeItemResponse(
            id = item.id!!,
            familyId = item.family.id!!,
            productName = item.getProductName(),
            masterProduct = item.masterProduct?.let {
                ProductSimpleResponse(
                    id = it.id!!,
                    name = it.name,
                    imageUrl = it.imageUrl,
                    defaultUnit = it.defaultUnit
                )
            },
            customProductName = item.customProductName,
            quantity = item.quantity,
            unit = item.unit,
            expirationDate = item.expirationDate,
            location = item.location,
            status = item.status,
            note = item.note,
            addedBy = UserSimpleResponse(
                id = item.addedBy.id!!,
                username = item.addedBy.username,
                fullName = item.addedBy.fullName
            ),
            isExpiringSoon = item.isExpiringSoon(),
            isExpired = item.isExpired(),
            daysUntilExpiration = daysUntilExpiration,
            createdAt = item.createdAt,
            updatedAt = item.updatedAt
        )
    }
}

