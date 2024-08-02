package com.be.shoackserver.exception

import com.fasterxml.jackson.module.kotlin.jsonMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.log4j.Log4j2
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
        } catch (e: UnauthorizedException) {
            val errorDetails = ErrorResponse(
                System.currentTimeMillis(),
                HttpServletResponse.SC_UNAUTHORIZED,
                HttpServletResponse.SC_UNAUTHORIZED.toString(),
                e.stackTraceToString(),
                e.message ?: "error message not defined",
                request.requestURI
            )
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.write(jsonMapper().writeValueAsString(errorDetails))
        } catch (e: Exception) {
            val errorDetails = ErrorResponse(
                System.currentTimeMillis(),
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR.toString(),
                e.stackTraceToString(),
                e.message ?: "error message not defined",
                request.requestURI
            )
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.write(jsonMapper().writeValueAsString(errorDetails))
        }
    }
}
