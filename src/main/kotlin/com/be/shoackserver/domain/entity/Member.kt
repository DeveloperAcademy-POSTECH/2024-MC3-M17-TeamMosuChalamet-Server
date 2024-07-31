package com.be.shoackserver.domain.entity

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.domain.entity.common.BaseEntity
import jakarta.persistence.*

@Entity
class Member(
    @Column(unique = true)
    var appleUserId: String? = null,
    var name: String? = null,
    var imageName: String? = null,
    var deviceToken: String? = null,
    var role: String? = null
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    companion object {
        fun create(memberDto: MemberDto): Member {
            return Member(
                appleUserId = memberDto.appleUserId,
                name = memberDto.name,
                deviceToken = memberDto.deviceToken,
                role = memberDto.role
            )
        }
    }
}