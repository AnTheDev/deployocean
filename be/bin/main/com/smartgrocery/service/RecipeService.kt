package com.smartgrocery.service

import com.smartgrocery.dto.common.PageResponse
import com.smartgrocery.dto.family.UserSimpleResponse
import com.smartgrocery.dto.product.ProductSimpleResponse
import com.smartgrocery.dto.recipe.*
import com.smartgrocery.entity.Recipe
import com.smartgrocery.entity.RecipeIngredient
import com.smartgrocery.exception.ErrorCode
import com.smartgrocery.exception.ForbiddenException
import com.smartgrocery.exception.ResourceNotFoundException
import com.smartgrocery.repository.*
import com.smartgrocery.security.CustomUserDetails
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val recipeIngredientRepository: RecipeIngredientRepository,
    private val userRepository: UserRepository,
    private val productRepository: MasterProductRepository,
    private val fridgeItemRepository: FridgeItemRepository,
    private val familyMemberRepository: FamilyMemberRepository
) {

    fun getAllRecipes(pageable: Pageable): PageResponse<RecipeResponse> {
        val page = recipeRepository.findByIsPublicTrueWithCreatedBy(pageable)
        return PageResponse.from(page) { toResponse(it) }
    }

    fun getRecipeById(recipeId: Long): RecipeDetailResponse {
        val recipe = recipeRepository.findByIdWithIngredients(recipeId)
            ?: throw ResourceNotFoundException(ErrorCode.RECIPE_NOT_FOUND)
        return toDetailResponse(recipe)
    }

    fun searchRecipes(title: String): List<RecipeResponse> {
        return recipeRepository.findByTitleContainingIgnoreCaseWithCreatedBy(title)
            .filter { it.isPublic }
            .map { toResponse(it) }
    }

    fun getMyRecipes(): List<RecipeResponse> {
        val currentUser = getCurrentUser()
        return recipeRepository.findByCreatedByIdWithCreatedBy(currentUser.id)
            .map { toResponse(it) }
    }

    @Transactional
    fun createRecipe(request: CreateRecipeRequest): RecipeDetailResponse {
        val currentUser = getCurrentUser()
        val user = userRepository.findById(currentUser.id)
            .orElseThrow { ResourceNotFoundException(ErrorCode.USER_NOT_FOUND) }

        val recipe = Recipe(
            title = request.title,
            description = request.description,
            instructions = request.instructions,
            difficulty = request.difficulty,
            prepTime = request.prepTime,
            cookTime = request.cookTime,
            servings = request.servings,
            imageUrl = request.imageUrl,
            isPublic = request.isPublic,
            notes = request.notes,
            createdBy = user
        )

        val savedRecipe = recipeRepository.save(recipe)

        // Add ingredients
        val ingredients = request.ingredients.map { ingredientRequest ->
            createIngredient(savedRecipe, ingredientRequest)
        }
        recipeIngredientRepository.saveAll(ingredients)
        savedRecipe.ingredients.addAll(ingredients)

        return toDetailResponse(savedRecipe)
    }

    @Transactional
    fun updateRecipe(recipeId: Long, request: UpdateRecipeRequest): RecipeDetailResponse {
        val currentUser = getCurrentUser()
        val recipe = recipeRepository.findByIdWithIngredients(recipeId)
            ?: throw ResourceNotFoundException(ErrorCode.RECIPE_NOT_FOUND)

        // Check ownership
        if (recipe.createdBy?.id != currentUser.id) {
            throw ForbiddenException("You can only edit your own recipes")
        }

        request.title?.let { recipe.title = it }
        request.description?.let { recipe.description = it }
        request.instructions?.let { recipe.instructions = it }
        request.difficulty?.let { recipe.difficulty = it }
        request.prepTime?.let { recipe.prepTime = it }
        request.cookTime?.let { recipe.cookTime = it }
        request.servings?.let { recipe.servings = it }
        request.imageUrl?.let { recipe.imageUrl = it }
        request.isPublic?.let { recipe.isPublic = it }
        request.notes?.let { recipe.notes = it }

        // Update ingredients if provided
        request.ingredients?.let { ingredientRequests ->
            // Remove old ingredients
            recipe.ingredients.clear()
            recipeIngredientRepository.deleteByRecipeId(recipeId)

            // Add new ingredients
            val ingredients = ingredientRequests.map { ingredientRequest ->
                createIngredient(recipe, ingredientRequest)
            }
            recipeIngredientRepository.saveAll(ingredients)
            recipe.ingredients.addAll(ingredients)
        }

        val savedRecipe = recipeRepository.save(recipe)
        return toDetailResponse(savedRecipe)
    }

    @Transactional
    fun deleteRecipe(recipeId: Long) {
        val currentUser = getCurrentUser()
        val recipe = recipeRepository.findById(recipeId)
            .orElseThrow { ResourceNotFoundException(ErrorCode.RECIPE_NOT_FOUND) }

        // Check ownership
        if (recipe.createdBy?.id != currentUser.id) {
            throw ForbiddenException("You can only delete your own recipes")
        }

        recipeRepository.delete(recipe)
    }

    fun suggestRecipes(familyId: Long): List<RecipeSuggestionResponse> {
        val currentUser = getCurrentUser()
        
        // Check membership
        if (!familyMemberRepository.existsByFamilyIdAndUserId(familyId, currentUser.id)) {
            throw ForbiddenException("You are not a member of this family")
        }

        // Get available ingredients from fridge
        val fridgeItems = fridgeItemRepository.findActiveByFamilyIdWithDetails(familyId)
        val availableProductIds = fridgeItems.mapNotNull { it.masterProduct?.id }.distinct()

        if (availableProductIds.isEmpty()) {
            return emptyList()
        }

        // Find recipes that use available ingredients
        val recipes = recipeRepository.findByIngredientProductIdsWithDetails(availableProductIds)

        return recipes.map { recipe ->
            val recipeIngredients = recipe.ingredients.toList()
            val requiredProductIds = recipeIngredients.mapNotNull { it.masterProduct?.id }
            
            val matchedIngredients = recipeIngredients
                .filter { it.masterProduct?.id in availableProductIds }
                .map { it.getIngredientName() }
            
            val missingIngredients = recipeIngredients
                .filter { it.masterProduct?.id !in availableProductIds && !it.isOptional }
                .map { it.getIngredientName() }

            val requiredCount = recipeIngredients.count { !it.isOptional }
            val matchedCount = recipeIngredients.count { it.masterProduct?.id in availableProductIds && !it.isOptional }
            val matchPercentage = if (requiredCount > 0) {
                (matchedCount.toDouble() / requiredCount.toDouble()) * 100
            } else {
                100.0
            }

            RecipeSuggestionResponse(
                recipe = toResponse(recipe),
                matchedIngredients = matchedIngredients,
                missingIngredients = missingIngredients,
                matchPercentage = matchPercentage
            )
        }.sortedByDescending { it.matchPercentage }
    }

    private fun createIngredient(recipe: Recipe, request: CreateRecipeIngredientRequest): RecipeIngredient {
        val masterProduct = request.masterProductId?.let {
            productRepository.findById(it)
                .orElseThrow { ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND) }
        }

        return RecipeIngredient(
            recipe = recipe,
            masterProduct = masterProduct,
            customIngredientName = request.customIngredientName,
            quantity = request.quantity,
            unit = request.unit,
            note = request.note,
            isOptional = request.isOptional
        )
    }

    private fun getCurrentUser(): CustomUserDetails {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.principal as CustomUserDetails
    }

    fun toResponse(recipe: Recipe): RecipeResponse {
        return RecipeResponse(
            id = recipe.id!!,
            title = recipe.title,
            description = recipe.description,
            instructions = recipe.instructions,
            difficulty = recipe.difficulty,
            prepTime = recipe.prepTime,
            cookTime = recipe.cookTime,
            totalTime = calculateTotalTime(recipe.prepTime, recipe.cookTime),
            servings = recipe.servings,
            imageUrl = recipe.imageUrl,
            isPublic = recipe.isPublic,
            notes = recipe.notes,
            createdBy = recipe.createdBy?.let {
                UserSimpleResponse(
                    id = it.id!!,
                    username = it.username,
                    fullName = it.fullName
                )
            },
            createdAt = recipe.createdAt,
            updatedAt = recipe.updatedAt
        )
    }

    private fun toDetailResponse(recipe: Recipe): RecipeDetailResponse {
        return RecipeDetailResponse(
            id = recipe.id!!,
            title = recipe.title,
            description = recipe.description,
            instructions = recipe.instructions,
            difficulty = recipe.difficulty,
            prepTime = recipe.prepTime,
            cookTime = recipe.cookTime,
            totalTime = calculateTotalTime(recipe.prepTime, recipe.cookTime),
            servings = recipe.servings,
            imageUrl = recipe.imageUrl,
            isPublic = recipe.isPublic,
            notes = recipe.notes,
            createdBy = recipe.createdBy?.let {
                UserSimpleResponse(
                    id = it.id!!,
                    username = it.username,
                    fullName = it.fullName
                )
            },
            ingredients = recipe.ingredients.map { ingredient ->
                RecipeIngredientResponse(
                    id = ingredient.id!!,
                    ingredientName = ingredient.getIngredientName(),
                    masterProduct = ingredient.masterProduct?.let {
                        ProductSimpleResponse(
                            id = it.id!!,
                            name = it.name,
                            imageUrl = it.imageUrl,
                            defaultUnit = it.defaultUnit
                        )
                    },
                    customIngredientName = ingredient.customIngredientName,
                    quantity = ingredient.quantity,
                    unit = ingredient.unit,
                    note = ingredient.note,
                    isOptional = ingredient.isOptional
                )
            },
            createdAt = recipe.createdAt,
            updatedAt = recipe.updatedAt
        )
    }

    private fun calculateTotalTime(prepTime: Int?, cookTime: Int?): Int? {
        return when {
            prepTime != null && cookTime != null -> prepTime + cookTime
            prepTime != null -> prepTime
            cookTime != null -> cookTime
            else -> null
        }
    }
}

