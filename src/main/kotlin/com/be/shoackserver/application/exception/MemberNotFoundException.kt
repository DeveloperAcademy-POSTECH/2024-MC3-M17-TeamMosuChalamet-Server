package com.be.shoackserver.application.exception

class MemberNotFoundException(memberId: Long) : RuntimeException("Member not found with id: $memberId")