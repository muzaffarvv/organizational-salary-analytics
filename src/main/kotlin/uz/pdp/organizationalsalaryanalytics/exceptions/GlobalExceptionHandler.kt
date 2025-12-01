@file:Suppress("CAST_NEVER_SUCCEEDS")

package uz.pdp.organizationalsalaryanalytics.exceptions

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import uz.pdp.organizationalsalaryanalytics.dto.ErrorResponse
import uz.pdp.organizationalsalaryanalytics.enums.ErrorCodes
import java.time.LocalDateTime

sealed class AppException(
    private val messageArgs: Array<out Any?>? = null,
    private val rawMessage: String? = null
) : RuntimeException() {

    abstract fun errorCode(): ErrorCodes

    fun getErrorMessage(messageSource: ResourceBundleMessageSource): String {
        return try {
            val i18nMessage = messageSource.getMessage(
                errorCode().name,
                messageArgs,
                LocaleContextHolder.getLocale()
            )
            if (rawMessage != null) "$i18nMessage: $rawMessage" else i18nMessage
        } catch (e: Exception) {
            rawMessage ?: super.message ?: "Unknown error"
        }
    }
}


@RestControllerAdvice
class GlobalExceptionHandler(private val messageSource: ResourceBundleMessageSource) {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(AppException::class)
    fun handleAppException(ex: AppException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        logger.error("AppException at ${request.requestURI}: ${ex.message}", ex)
        val status = when (ex.errorCode()) {
            ErrorCodes.NOT_FOUND -> HttpStatus.NOT_FOUND
            ErrorCodes.BAD_REQUEST -> HttpStatus.BAD_REQUEST
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        val error = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = status.value(),
            code = ex.errorCode().code,
            error = status.reasonPhrase,
            message = ex.getErrorMessage(messageSource),
            path = request.requestURI
        )
        return ResponseEntity(error, status)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        logger.error("Unhandled exception at ${request.requestURI}: ${ex.message}", ex)
        val error = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            code = ErrorCodes.INTERNAL_ERROR.code,
            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = ex.message ?: "An unexpected error occurred",
            path = request.requestURI
        )
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
