package com.be.shoackserver.exception

import jakarta.servlet.http.HttpServletRequest
import lombok.extern.log4j.Log4j2
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.NoHandlerFoundException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Log4j2
@ControllerAdvice
class ExceptionController {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val logger: Logger = LogManager.getLogger(ExceptionController::class.java)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        val errorDetail = ErrorResponse(
            LocalDateTime.now().format(dateFormatter),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.name,
            e.stackTraceToString(),
            e.message ?: "error message not defined",
            request.requestURI
        )

        logger.error("IllegalArgumentException: ${errorDetail.message}")

        return ResponseEntity(errorDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(e: AccessDeniedException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        val errorDetail = ErrorResponse(
            LocalDateTime.now().format(dateFormatter),
            HttpStatus.FORBIDDEN.value(),
            HttpStatus.FORBIDDEN.name,
            e.stackTraceToString(),
            e.message ?: "error message not defined",
            request.requestURI
        )

        logger.error("AccessDeniedException: ${errorDetail.message}")

        return ResponseEntity(errorDetail, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        val errorDetail = ErrorResponse(
            LocalDateTime.now().format(dateFormatter),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.name,
            e.stackTraceToString(),
            e.message ?: "error message not defined",
            request.requestURI
        )

        logger.error("RuntimeException: ${errorDetail.message}")

        return ResponseEntity(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNotFoundException(e: NoHandlerFoundException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        val errorDetail = ErrorResponse(
            LocalDateTime.now().format(dateFormatter),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.name,
            e.stackTraceToString(),
            e.message ?: "error message not defined",
            request.requestURI
        )

        logger.error("NoHandlerFoundException: ${errorDetail.message}")

        return ResponseEntity(errorDetail, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotAllowedException(e: HttpRequestMethodNotSupportedException, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        val errorDetail = ErrorResponse(
            LocalDateTime.now().format(dateFormatter),
            HttpStatus.METHOD_NOT_ALLOWED.value(),
            HttpStatus.METHOD_NOT_ALLOWED.name,
            e.stackTraceToString(),
            e.message ?: "error message not defined",
            request.requestURI
        )

        logger.error("HttpRequestMethodNotSupportedException: ${errorDetail.message}")

        return ResponseEntity(errorDetail, HttpStatus.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(e: Exception, request: HttpServletRequest) : ResponseEntity<ErrorResponse> {
        val errorDetail = ErrorResponse(
            LocalDateTime.now().format(dateFormatter),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.name,
            e.stackTraceToString(),
            e.message ?: "error message not defined",
            request.requestURI
        )

        logger.error("Exception: ${errorDetail.message}")

        return ResponseEntity(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

data class ErrorResponse(
    val timestamp: String,
    val status: Int,
    val error: String,
    val trace: String,
    val message: String,
    val path: String
)