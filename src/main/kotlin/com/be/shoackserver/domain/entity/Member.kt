package com.be.shoackserver.domain.entity

import com.be.shoackserver.domain.entity.common.BaseEntity
import jakarta.persistence.*

@Entity
class Member : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(unique = true) // 동일한 애플 유저 중복 방지
    var appleUserId: String? = null
    var name: String? = null
    var imageName: String? = null
    var deviceToken: String? = null
}