package uz.pdp.organizationalsalaryanalytics.dto

import uz.pdp.organizationalsalaryanalytics.base.BaseDto
import uz.pdp.organizationalsalaryanalytics.enums.CalculationType
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class RegionDto(
    override val id: UUID? = null,
    @field:NotBlank(message = ValidationMessages.REGION_BLANK)
    @field:Size(max = 100, message = ValidationMessages.REGION_MAX)
    val name: String,
    override val createdAt: LocalDateTime? = null,
    override val updatedAt: LocalDateTime? = null
) : BaseDto

data class CreateRegionDto(
    @field:NotBlank(message = ValidationMessages.REGION_BLANK)
    @field:Size(max = 100, message = ValidationMessages.REGION_MAX)
    val name: String
)

data class OrganizationDto(
    override val id: UUID? = null,
    @field:NotBlank(message = ValidationMessages.ORG_BLANK)
    @field:Size(max = 150, message = ValidationMessages.ORG_MAX)
    val name: String,
    val regionId: UUID? = null,
    val regionName: String? = null,
    val parentId: UUID? = null,
    val isLast: Boolean,
    override val createdAt: LocalDateTime? = null,
    override val updatedAt: LocalDateTime? = null
) : BaseDto

data class CreateOrganizationDto(
    @field:NotBlank(message = ValidationMessages.ORG_BLANK)
    @field:Size(max = 150, message = ValidationMessages.ORG_MAX)
    val name: String,
    val regionId: UUID?,
    val parentId: UUID?,
    val isLast: Boolean = false
)

data class UpdateOrganizationDto(
    @field:Size(max = 150, message = ValidationMessages.ORG_MAX)
    val name: String?,
    val regionId: UUID?,
    val parentId: UUID?,
    val isLast: Boolean?
)

data class OrganizationTreeDto(
    val id: UUID,
    val name: String,
    val regionName: String? = null,
    val level: Int,
    val parentId: UUID? = null,
    val children: List<OrganizationTreeDto> = emptyList()
)

data class EmployeeDto(
    override val id: UUID? = null,
    @field:NotBlank(message = ValidationMessages.FIRST_NAME_BLANK)
    val firstName: String,
    @field:NotBlank(message = ValidationMessages.LAST_NAME_BLANK)
    val lastName: String,
    @field:NotBlank(message = ValidationMessages.PINFL_BLANK)
    @field:Pattern(regexp = ValidationMessages.PINFL_REGEX, message = ValidationMessages.PINFL_INVALID)
    val pinfl: String,
    @field:NotNull(message = ValidationMessages.HIRE_DATE_REQUIRED)
    val hireDate: LocalDate,
    val organizationId: UUID,
    val organizationName: String? = null,
    override val createdAt: LocalDateTime? = null,
    override val updatedAt: LocalDateTime? = null
) : BaseDto

data class CreateEmployeeDto(
    @field:NotBlank(message = ValidationMessages.FIRST_NAME_BLANK)
    val firstName: String,
    @field:NotBlank(message = ValidationMessages.LAST_NAME_BLANK)
    val lastName: String,
    @field:NotBlank(message = ValidationMessages.PINFL_BLANK)
    @field:Pattern(regexp = ValidationMessages.PINFL_REGEX, message = ValidationMessages.PINFL_INVALID)
    val pinfl: String,
    @field:NotNull(message = ValidationMessages.HIRE_DATE_REQUIRED)
    val hireDate: LocalDate,
    @field:NotNull(message = ValidationMessages.ORG_ID_REQUIRED)
    val organizationId: UUID
)

data class UpdateEmployeeDto(
    val firstName: String?,
    val lastName: String?,
    @field:Pattern(regexp = ValidationMessages.PINFL_REGEX, message = ValidationMessages.PINFL_INVALID)
    val pinfl: String?,
    val hireDate: LocalDate?,
    val organizationId: UUID?
)

data class CalculationRecordDto(
    override val id: UUID? = null,
    @field:NotNull(message = ValidationMessages.EMPLOYEE_ID_REQUIRED)
    val employeeId: UUID,
    val employeeName: String? = null,
    @field:NotNull(message = ValidationMessages.ORG_ID_REQUIRED)
    val organizationId: UUID,
    val organizationName: String? = null,
    @field:NotBlank(message = ValidationMessages.PERIOD_REQUIRED)
    @field:Pattern(regexp = ValidationMessages.PERIOD_REGEX, message = ValidationMessages.PERIOD_INVALID)
    val period: String,
    @field:NotNull(message = ValidationMessages.CALC_TYPE_REQUIRED)
    val calculationType: CalculationType,
    @field:DecimalMin("0.0", message = ValidationMessages.POSITIVE_AMOUNT)
    val amount: BigDecimal,
    @field:DecimalMin("0.0", message = ValidationMessages.POSITIVE_WORK_RATE)
    val workRate: BigDecimal,
    override val createdAt: LocalDateTime? = null,
    override val updatedAt: LocalDateTime? = null
) : BaseDto

data class CreateCalculationRecordDto(
    @field:NotNull(message = ValidationMessages.EMPLOYEE_ID_REQUIRED)
    val employeeId: UUID,
    @field:NotNull(message = ValidationMessages.ORG_ID_REQUIRED)
    val organizationId: UUID,
    @field:NotBlank(message = ValidationMessages.PERIOD_REQUIRED)
    @field:Pattern(regexp = ValidationMessages.PERIOD_REGEX, message = ValidationMessages.PERIOD_INVALID)
    val period: String,
    @field:NotNull(message = ValidationMessages.CALC_TYPE_REQUIRED)
    val calculationType: CalculationType,
    @field:DecimalMin("0.0", message = ValidationMessages.POSITIVE_AMOUNT)
    val amount: BigDecimal,
    @field:DecimalMin("0.0", message = ValidationMessages.POSITIVE_WORK_RATE)
    val workRate: BigDecimal
)

data class UpdateCalculationRecordDto(
    val employeeId: UUID?,
    val organizationId: UUID?,
    @field:Pattern(regexp = ValidationMessages.PERIOD_REGEX, message = ValidationMessages.PERIOD_INVALID)
    val period: String?,
    val calculationType: CalculationType?,
    @field:DecimalMin("0.0", message = ValidationMessages.POSITIVE_AMOUNT)
    val amount: BigDecimal?,
    @field:DecimalMin("0.0", message = ValidationMessages.POSITIVE_WORK_RATE)
    val workRate: BigDecimal?
)

data class HighWorkRateReportDto(
    val pinfl: String,
    val employeeId: UUID,
    val totalWorkRate: BigDecimal
)

data class MultiRegionEmployeeReportDto(
    val pinfl: String,
    val employeeId: UUID,
    val firstName: String,
    val lastName: String,
    val distinctRegionCount: Long,
    val totalSalaryAmount: BigDecimal
)

data class OrganizationSalaryReportDto(
    val organizationId: UUID,
    val organizationName: String,
    val employees: List<EmployeeSalaryDto>,
    val averageSalary: BigDecimal
)

data class EmployeeSalaryDto(
    val employeeId: UUID,
    val firstName: String,
    val lastName: String,
    val pinfl: String,
    val salaryAmount: BigDecimal
)

data class SalaryVacationReportDto(
    val employeeId: UUID,
    val firstName: String,
    val lastName: String,
    val pinfl: String,
    val salaryAmount: BigDecimal,
    val vacationAmount: BigDecimal
)

data class ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val code: Long,
    val error: String,
    val message: String,
    val path: String?
)

object ValidationMessages {
    const val FIRST_NAME_BLANK = "First name can't be blank"
    const val LAST_NAME_BLANK = "Last name can't be blank"
    const val PINFL_BLANK = "PINFL can't be blank"
    const val PINFL_REGEX = "\\d{14}"
    const val PINFL_INVALID = "PINFL must be 14 digits"
    const val HIRE_DATE_REQUIRED = "Hire date is required"
    const val PERIOD_REQUIRED = "Period is required"
    const val PERIOD_REGEX = "^\\d{4}\\.\\d{2}$"
    const val PERIOD_INVALID = "Period must be in yyyy.MM format"
    const val POSITIVE_AMOUNT = "Amount must be positive"
    const val POSITIVE_WORK_RATE = "Work rate must be positive"
    const val REGION_BLANK = "Region name can't be blank"
    const val REGION_MAX = "Region name can't exceed 100 characters"
    const val ORG_BLANK = "Organization name can't be blank"
    const val ORG_MAX = "Organization name can't exceed 150 characters"
    const val EMPLOYEE_ID_REQUIRED = "Employee ID is required"
    const val ORG_ID_REQUIRED = "Organization ID is required"
    const val CALC_TYPE_REQUIRED = "Calculation type is required"
}
