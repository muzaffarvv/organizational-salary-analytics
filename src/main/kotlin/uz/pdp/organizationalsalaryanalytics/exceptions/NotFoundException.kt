package uz.pdp.organizationalsalaryanalytics.exceptions

import uz.pdp.organizationalsalaryanalytics.enums.ErrorCodes

class NotFoundException(
    args: Array<Any?>? = null,
    message: String? = null
) : AppException(args, message) {
    override fun errorCode() = ErrorCodes.NOT_FOUND
}