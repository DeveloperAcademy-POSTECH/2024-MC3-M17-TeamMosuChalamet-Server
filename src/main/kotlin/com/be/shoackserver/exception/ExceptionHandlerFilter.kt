package com.be.shoackserver.exception

import com.fasterxml.jackson.module.kotlin.jsonMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.log4j.Log4j2
import org.springframework.http.HttpStatus
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.filter.OncePerRequestFilter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Log4j2
class ExceptionHandlerFilter : OncePerRequestFilter() {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)

        } catch(e: HttpRequestMethodNotSupportedException) {
            val errorDetails = ErrorResponse(
                LocalDateTime.now().format(dateFormatter),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                HttpStatus.METHOD_NOT_ALLOWED.name,
                e.stackTraceToString(),
                e.message ?: "error message not defined",
                request.requestURI
            )

            response.status = HttpStatus.METHOD_NOT_ALLOWED.value()
            response.contentType = "application/json"
            response.writer.write(jsonMapper().writeValueAsString(errorDetails))
        }

        catch (e: IllegalArgumentException) {
            val errorDetails = ErrorResponse(
                LocalDateTime.now().format(dateFormatter),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name,
                e.stackTraceToString(),
                e.message ?: "error message not defined",
                request.requestURI
            )

            response.status = HttpStatus.BAD_REQUEST.value()
            response.contentType = "application/json"
            response.writer.write(jsonMapper().writeValueAsString(errorDetails))

        } catch (e: ExpiredJwtException) {
            val errorDetails = ErrorResponse(
                LocalDateTime.now().format(dateFormatter),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name,
                e.stackTraceToString(),
                e.message ?: "error message not defined",
                request.requestURI
            )

            response.status = HttpStatus.UNAUTHORIZED.value()
            response.contentType = "application/json"
            response.writer.write(jsonMapper().writeValueAsString(errorDetails))

        } catch(e: SignatureException) {
            val errorDetails = ErrorResponse(
                LocalDateTime.now().format(dateFormatter),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name,
                e.stackTraceToString(),
                e.message ?: "error message not defined",
                request.requestURI
            )

            response.status = HttpStatus.UNAUTHORIZED.value()
            response.contentType = "application/json"
            response.writer.write(jsonMapper().writeValueAsString(errorDetails))

        }
        catch (e: JwtException) {
            val errorDetails = ErrorResponse(
                LocalDateTime.now().format(dateFormatter),
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
                LocalDateTime.now().format(dateFormatter),
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
                LocalDateTime.now().format(dateFormatter),
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
