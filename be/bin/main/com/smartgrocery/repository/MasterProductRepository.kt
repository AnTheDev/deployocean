package com.smartgrocery.repository

import com.smartgrocery.entity.MasterProduct
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MasterProductRepository : JpaRepository<MasterProduct, Long> {
    
    @EntityGraph(attributePaths = ["categories"])
    @Query("SELECT p FROM MasterProduct p WHERE p.isActive = true")
    fun findByIsActiveTrueWithCategories(): List<MasterProduct>
    
    @EntityGraph(attributePaths = ["categories"])
    @Query("SELECT p FROM MasterProduct p WHERE p.isActive = true")
    fun findByIsActiveTrueWithCategories(pageable: Pageable): Page<MasterProduct>
    
    @EntityGraph(attributePaths = ["categories"])
    @Query("SELECT p FROM MasterProduct p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.isActive = true")
    fun findByNameContainingIgnoreCaseAndIsActiveTrueWithCategories(name: String): List<MasterProduct>
    
    @EntityGraph(attributePaths = ["categories"])
    @Query("SELECT p FROM MasterProduct p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.isActive = true")
    fun findByNameContainingIgnoreCaseAndIsActiveTrueWithCategories(name: String, pageable: Pageable): Page<MasterProduct>

    @Query("SELECT p FROM MasterProduct p LEFT JOIN FETCH p.categories WHERE p.id = :id")
    fun findByIdWithCategories(id: Long): MasterProduct?

    @EntityGraph(attributePaths = ["categories"])
    @Query("""
        SELECT DISTINCT p FROM MasterProduct p 
        JOIN p.categories c 
        WHERE c.id = :categoryId AND p.isActive = true
    """)
    fun findByCategoryIdWithCategories(categoryId: Long): List<MasterProduct>

    @EntityGraph(attributePaths = ["categories"])
    @Query(
        value = "SELECT DISTINCT p FROM MasterProduct p JOIN p.categories c WHERE c.id = :categoryId AND p.isActive = true",
        countQuery = "SELECT COUNT(DISTINCT p) FROM MasterProduct p JOIN p.categories c WHERE c.id = :categoryId AND p.isActive = true"
    )
    fun findByCategoryIdWithCategories(categoryId: Long, pageable: Pageable): Page<MasterProduct>

    fun existsByName(name: String): Boolean
}

