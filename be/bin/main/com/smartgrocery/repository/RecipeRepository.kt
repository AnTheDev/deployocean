package com.smartgrocery.repository

import com.smartgrocery.entity.Recipe
import com.smartgrocery.entity.RecipeDifficulty
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : JpaRepository<Recipe, Long> {
    
    @EntityGraph(attributePaths = ["createdBy"])
    @Query("SELECT r FROM Recipe r WHERE r.isPublic = true")
    fun findByIsPublicTrueWithCreatedBy(): List<Recipe>
    
    @EntityGraph(attributePaths = ["createdBy"])
    @Query("SELECT r FROM Recipe r WHERE r.isPublic = true")
    fun findByIsPublicTrueWithCreatedBy(pageable: Pageable): Page<Recipe>
    
    @EntityGraph(attributePaths = ["createdBy"])
    @Query("SELECT r FROM Recipe r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    fun findByTitleContainingIgnoreCaseWithCreatedBy(title: String): List<Recipe>
    
    fun findByDifficulty(difficulty: RecipeDifficulty): List<Recipe>

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.ingredients i LEFT JOIN FETCH i.masterProduct LEFT JOIN FETCH r.createdBy WHERE r.id = :id")
    fun findByIdWithIngredients(id: Long): Recipe?

    @EntityGraph(attributePaths = ["createdBy"])
    @Query("""
        SELECT r FROM Recipe r 
        WHERE r.isPublic = true 
        OR r.createdBy.id = :userId
    """)
    fun findAvailableForUser(userId: Long): List<Recipe>

    @EntityGraph(attributePaths = ["createdBy"])
    @Query("""
        SELECT r FROM Recipe r 
        WHERE r.isPublic = true 
        OR r.createdBy.id = :userId
    """)
    fun findAvailableForUser(userId: Long, pageable: Pageable): Page<Recipe>

    @EntityGraph(attributePaths = ["createdBy", "ingredients"])
    @Query("""
        SELECT DISTINCT r FROM Recipe r 
        JOIN r.ingredients ri 
        WHERE ri.masterProduct.id IN :productIds 
        AND r.isPublic = true
    """)
    fun findByIngredientProductIdsWithDetails(productIds: List<Long>): List<Recipe>

    @EntityGraph(attributePaths = ["createdBy"])
    @Query("SELECT r FROM Recipe r WHERE r.createdBy.id = :userId")
    fun findByCreatedByIdWithCreatedBy(userId: Long): List<Recipe>
}

