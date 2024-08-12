package com.be.shoackserver.exception

class MemberNotFoundException(memberId: Long) : RuntimeException("Member not found with id: $memberId")