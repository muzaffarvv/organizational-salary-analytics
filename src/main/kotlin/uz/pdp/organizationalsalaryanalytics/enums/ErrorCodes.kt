package uz.pdp.organizationalsalaryanalytics.enums

enum class ErrorCodes(val code: Long) {
    NOT_FOUND(100),
    BAD_REQUEST(101),
    INTERNAL_ERROR(102)
}