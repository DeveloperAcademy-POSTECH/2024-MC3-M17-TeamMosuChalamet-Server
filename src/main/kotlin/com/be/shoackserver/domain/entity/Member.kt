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
    var appleRefreshToken: String? = null,
    var role: String? = null,

    @OneToMany(mappedBy = "subjectMember", cascade = [CascadeType.ALL])
    var friendshipsAsSubject: MutableList<Friendship> = mutableListOf(),

    @OneToMany(mappedBy = "objectMember", cascade = [CascadeType.ALL])
    var friendshipsAsObject: MutableList<Friendship> = mutableListOf()

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