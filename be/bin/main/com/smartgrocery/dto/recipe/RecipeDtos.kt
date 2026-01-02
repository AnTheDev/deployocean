package com.smartgrocery.dto.recipe

import com.smartgrocery.dto.family.UserSimpleResponse
import com.smartgrocery.dto.product.ProductSimpleResponse
import com.smartgrocery.entity.RecipeDifficulty
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.Instant

data class CreateRecipeRequest(
    @field:NotBlank(message = "Recipe title is required")
    @field:Size(max = 200, message = "Title must not exceed 200 characters")
    val title: String,

    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String? = null,

    val instructions: String? = null,

    val difficulty: RecipeDifficulty = RecipeDifficulty.MEDIUM,

    @field:Positive(message = "Prep time must be positive")
    val prepTime: Int? = null,

    @field:Positive(message = "Cook time must be positive")
    val cookTime: Int? = null,

    @field:Positive(message = "Servings must be positive")
    val servings: Int = 2,

    val imageUrl: String? = null,

    val isPublic: Boolean = true,

    val notes: String? = null,

    @field:Valid
    val ingredients: List<CreateRecipeIngredientRequest> = emptyList()
)

data class UpdateRecipeRequest(
    @field:Size(max = 200, message = "Title must not exceed 200 characters")
    val title: String? = null,

    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String? = null,

    val instructions: String? = null,

    val difficulty: RecipeDifficulty? = null,

    @field:Positive(message = "Prep time must be positive")
    val prepTime: Int? = null,

    @field:Positive(message = "Cook time must be positive")
    val cookTime: Int? = null,

    @field:Positive(message = "Servings must be positive")
    val servings: Int? = null,

    val imageUrl: String? = null,

    val isPublic: Boolean? = null,

    val notes: String? = null,

    @field:Valid
    val ingredients: List<CreateRecipeIngredientRequest>? = null
)

data class CreateRecipeIngredientRequest(
    val masterProductId: Long? = null,

    @field:Size(max = 200, message = "Custom ingredient name must not exceed 200 characters")
    val customIngredientName: String? = null,

    @field:NotNull(message = "Quantity is required")
    @field:Positive(message = "Quantity must be positive")
    val quantity: BigDecimal,

    @field:NotBlank(message = "Unit is required")
    @field:Size(max = 50, message = "Unit must not exceed 50 characters")
    val unit: String,

    @field:Size(max = 255, message = "Note must not exceed 255 characters")
    val note: String? = null,

    val isOptional: Boolean = false
)

data class RecipeResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val instructions: String?,
    val difficulty: RecipeDifficulty,
    val prepTime: Int?,
    val cookTime: Int?,
    val totalTime: Int?,
    val servings: Int,
    val imageUrl: String?,
    val isPublic: Boolean,
    val notes: String?,
    val createdBy: UserSimpleResponse?,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class RecipeDetailResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val instructions: String?,
    val difficulty: RecipeDifficulty,
    val prepTime: Int?,
    val cookTime: Int?,
    val totalTime: Int?,
    val servings: Int,
    val imageUrl: String?,
    val isPublic: Boolean,
    val notes: String?,
    val createdBy: UserSimpleResponse?,
    val ingredients: List<RecipeIngredientResponse>,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class RecipeIngredientResponse(
    val id: Long,
    val ingredientName: String,
    val masterProduct: ProductSimpleResponse?,
    val customIngredientName: String?,
    val quantity: BigDecimal,
    val unit: String,
    val note: String?,
    val isOptional: Boolean
)

data class RecipeSuggestionResponse(
    val recipe: RecipeResponse,
    val matchedIngredients: List<String>,
    val missingIngredients: List<String>,
    val matchPercentage: Double
)

