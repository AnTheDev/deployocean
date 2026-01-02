package com.smartgrocery.service

import com.smartgrocery.dto.category.CategoryResponse
import com.smartgrocery.dto.category.CreateCategoryRequest
import com.smartgrocery.dto.category.UpdateCategoryRequest
import com.smartgrocery.entity.Category
import com.smartgrocery.exception.ConflictException
import com.smartgrocery.exception.ErrorCode
import com.smartgrocery.exception.ResourceNotFoundException
import com.smartgrocery.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    fun getAllCategories(): List<CategoryResponse> {
        return categoryRepository.findAllActiveOrdered().map { toResponse(it) }
    }

    fun getCategoryById(id: Long): CategoryResponse {
        val category = categoryRepository.findById(id)
            .orElseThrow { ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND) }
        return toResponse(category)
    }

    fun searchCategories(name: String): List<CategoryResponse> {
        return categoryRepository.findByNameContainingIgnoreCase(name).map { toResponse(it) }
    }

    @Transactional
    fun createCategory(request: CreateCategoryRequest): CategoryResponse {
        if (categoryRepository.existsByName(request.name)) {
            throw ConflictException(ErrorCode.CONFLICT, "Category with this name already exists")
        }

        val category = Category(
            name = request.name,
            iconUrl = request.iconUrl,
            description = request.description,
            displayOrder = request.displayOrder
        )

        val savedCategory = categoryRepository.save(category)
        return toResponse(savedCategory)
    }

    @Transactional
    fun updateCategory(id: Long, request: UpdateCategoryRequest): CategoryResponse {
        val category = categoryRepository.findById(id)
            .orElseThrow { ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND) }

        request.name?.let { category.name = it }
        request.iconUrl?.let { category.iconUrl = it }
        request.description?.let { category.description = it }
        request.displayOrder?.let { category.displayOrder = it }
        request.isActive?.let { category.isActive = it }

        val savedCategory = categoryRepository.save(category)
        return toResponse(savedCategory)
    }

    @Transactional
    fun deleteCategory(id: Long) {
        if (!categoryRepository.existsById(id)) {
            throw ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND)
        }
        categoryRepository.deleteById(id)
    }

    private fun toResponse(category: Category): CategoryResponse {
        return CategoryResponse(
            id = category.id!!,
            name = category.name,
            iconUrl = category.iconUrl,
            description = category.description,
            displayOrder = category.displayOrder,
            isActive = category.isActive
        )
    }
}

