package com.be.shoackserver.exception

import com.fasterxml.jackson.module.kotlin.jsonMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.log4j.Log4j2
import org.springframework.http.HttpStatus
import org.springframework.web.filter.OncePerRequestFilter

@Log4j2
class ExceptionHandlerFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: ExpiredJwtException) {
            val errorDetails = ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name,
                e.stackTraceToString(),
                e.message ?: "error message not defined",
                request.requestURI
            )
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.contentType = "application/json"
            response.writer.write(jsonMapper().writeValueAsString(errorDetails))

        } catch (e: JwtException) {
            val errorDetails = ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name,
                e.stackTraceToString(),
                e.message ?: "error message not defined",
                request.requestURI
            )
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.contentType = "application/json"
            response.writer.write(jsonMapper().writeValueAsString(errorDetails))

        } catch (e: AccessDeniedException) {
            val errorDetails = ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name,
                e.stackTraceToString(),
                e.message ?: "error message not defined",
                request.requestURI
            )
            response.status = HttpStatus.FORBIDDEN.value()
            response.contentType = "application/json"
            response.writer.write(jsonMapper().writeValueAsString(errorDetails))

        } catch (e: Exception) {
            val errorDetails = ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name,
                e.stackTraceToString(),
                e.message ?: "error message not defined",
                request.requestURI
            )
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            response.contentType = "application/json"
            response.writer.write(jsonMapper().writeValueAsString(errorDetails))
        }
    }
}
