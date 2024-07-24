package com.be.shoackserver.domain.entity

import com.be.shoackserver.domain.entity.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["subject_member_id", "object_member_id"])]) // 친구관계 중복 방지
class Friendship : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_member_id")
    var subjectMember: Member? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_member_id")
    var objectMember: Member? = null

    @Enumerated(EnumType.STRING)
    var status: FriendshipStatus? = FriendshipStatus.ACCEPTED

    enum class FriendshipStatus {
        Pending, ACCEPTED, REJECTED
    }

}