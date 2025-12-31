package com.smartgrocery.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "username", nullable = false, unique = true, length = 50)
    var username: String,

    @Column(name = "email", nullable = false, unique = true, length = 100)
    var email: String,

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String,

    @Column(name = "full_name", nullable = false, length = 100)
    var fullName: String,

    @Column(name = "avatar_url", length = 500)
    var avatarUrl: String? = null,

    @Column(name = "fcm_token", length = 255)
    var fcmToken: String? = null,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = mutableSetOf()
) : BaseEntity()

