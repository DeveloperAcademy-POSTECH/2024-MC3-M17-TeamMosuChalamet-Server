package com.be.shoackserver.domain.repository

import com.be.shoackserver.domain.entity.Friendship
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FriendshipRepository : JpaRepository<Friendship, Long>{
}