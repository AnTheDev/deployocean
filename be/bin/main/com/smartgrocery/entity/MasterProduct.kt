package com.smartgrocery.entity

import jakarta.persistence.*

@Entity
@Table(name = "master_products")
class MasterProduct(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = false, length = 200)
    var name: String,

    @Column(name = "image_url", length = 500)
    var imageUrl: String? = null,

    @Column(name = "default_unit", nullable = false, length = 50)
    var defaultUnit: String,

    @Column(name = "avg_shelf_life")
    var avgShelfLife: Int? = null,  // in days

    @Column(name = "description", length = 500)
    var description: String? = null,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "product_categories",
        joinColumns = [JoinColumn(name = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    var categories: MutableSet<Category> = mutableSetOf()
) : BaseEntity()

