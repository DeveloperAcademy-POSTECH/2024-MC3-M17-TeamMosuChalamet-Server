package com.be.shoackserver.domain.entity

import com.be.shoackserver.domain.entity.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["subject_member_id", "object_member_id"])]) // 친구관계 중복 방지
class Friendship(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_member_id")
    var subjectMember: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_member_id")
    var objectMember: Member,

    @Enumerated(EnumType.STRING)
    var status: FriendshipStatus = FriendshipStatus.ACCEPTED

) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    enum class FriendshipStatus {
        PENDING, ACCEPTED, REJECTED
    }

    companion object {
        fun create(requester: Member, selfMember: Member, status: FriendshipStatus = FriendshipStatus.ACCEPTED): Friendship {
            return Friendship(
                subjectMember = selfMember,
                objectMember = requester,
                status = status
            )
        }
    }
}