package com.smartgrocery.entity

import jakarta.persistence.*

enum class RecipeDifficulty {
    EASY,
    MEDIUM,
    HARD
}

@Entity
@Table(name = "recipes")
class Recipe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "title", nullable = false, length = 200)
    var title: String,

    @Column(name = "description", length = 500)
    var description: String? = null,

    @Column(name = "instructions", columnDefinition = "TEXT")
    var instructions: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false, length = 20)
    var difficulty: RecipeDifficulty = RecipeDifficulty.MEDIUM,

    @Column(name = "prep_time")
    var prepTime: Int? = null,  // in minutes

    @Column(name = "cook_time")
    var cookTime: Int? = null,  // in minutes

    @Column(name = "servings")
    var servings: Int = 2,

    @Column(name = "image_url", length = 500)
    var imageUrl: String? = null,

    @Column(name = "is_public", nullable = false)
    var isPublic: Boolean = true,

    @Column(name = "notes", columnDefinition = "TEXT")
    var notes: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    var createdBy: User? = null,

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var ingredients: MutableList<RecipeIngredient> = mutableListOf()
) : BaseEntity()

