package com.smartgrocery.repository

import com.smartgrocery.entity.FamilyMember
import com.smartgrocery.entity.FamilyMemberId
import com.smartgrocery.entity.FamilyRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FamilyMemberRepository : JpaRepository<FamilyMember, FamilyMemberId> {
    
    @Query("SELECT fm FROM FamilyMember fm JOIN FETCH fm.user WHERE fm.family.id = :familyId")
    fun findByFamilyIdWithUsers(familyId: Long): List<FamilyMember>
    
    @Query("SELECT fm FROM FamilyMember fm WHERE fm.family.id = :familyId AND fm.user.id = :userId")
    fun findByFamilyIdAndUserId(familyId: Long, userId: Long): FamilyMember?
    
    @Query("SELECT COUNT(fm) > 0 FROM FamilyMember fm WHERE fm.family.id = :familyId AND fm.user.id = :userId")
    fun existsByFamilyIdAndUserId(familyId: Long, userId: Long): Boolean

    @Query("SELECT fm FROM FamilyMember fm WHERE fm.family.id = :familyId AND fm.role = :role")
    fun findByFamilyIdAndRole(familyId: Long, role: FamilyRole): List<FamilyMember>

    @Query("SELECT fm FROM FamilyMember fm JOIN FETCH fm.family f JOIN FETCH f.createdBy WHERE fm.user.id = :userId")
    fun findByUserIdWithFamily(userId: Long): List<FamilyMember>
}

