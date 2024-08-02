package com.be.shoackserver.exception

import jakarta.servlet.http.HttpServletRequest
import lombok.extern.log4j.Log4j2
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Log4j2
@ControllerAdvice
class ExceptionController {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        val errorDetail = ErrorResponse(
            System.currentTimeMillis(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.name,
            e.stackTraceToString(),
            e.message ?: "error message not defined",
            request.requestURI
        )
        return ResponseEntity(errorDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(e: AccessDeniedException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        val errorDetail = ErrorResponse(
            System.currentTimeMillis(),
            HttpStatus.FORBIDDEN.value(),
            HttpStatus.FORBIDDEN.name,
            e.stackTraceToString(),
            e.message ?: "error message not defined",
            request.requestURI
        )
        return ResponseEntity(errorDetail, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(CustomException::class)
    fun handleAuthenticationException(e: CustomException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        val errorDetail = ErrorResponse(
            System.currentTimeMillis(),
            HttpStatus.UNAUTHORIZED.value(),
            HttpStatus.UNAUTHORIZED.name,
            e.stackTraceToString(),
            e.message ?: "error message not defined",
            request.requestURI
        )
        return ResponseEntity(errorDetail, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(e: Exception, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        val errorDetail = ErrorResponse(
            System.currentTimeMillis(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.name,
            e.stackTraceToString(),
            e.message ?: "error message not defined",
            request.requestURI
        )
        return ResponseEntity(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

data class ErrorResponse(
    val timestamp: Long,
    val status: Int,
    val error: String,
    val trace: String,
    val message: String,
    val path: String
){

}