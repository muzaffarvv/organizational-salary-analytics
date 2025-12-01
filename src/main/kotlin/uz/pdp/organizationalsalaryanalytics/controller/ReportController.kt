package uz.pdp.organizationalsalaryanalytics.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.MessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uz.pdp.organizationalsalaryanalytics.exceptions.BadRequestException
import uz.pdp.organizationalsalaryanalytics.dto.*
import uz.pdp.organizationalsalaryanalytics.service.ReportService
import java.math.BigDecimal
import java.util.*

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports", description = "Analytics and reporting APIs")
class ReportController(
    private val reportService: ReportService,
    private val messageSource: MessageSource ) {

    @GetMapping("/high-work-rate")
    @Operation(
        summary = "Report 1: Employees with high work rate",
        description = "Returns employees whose total work rate exceeds the specified threshold in a given period."
    )
    fun getHighWorkRateEmployees(
        @Parameter(description = "Period in format yyyy.MM", example = "2024.01", required = true)
        @RequestParam period: String,

        @Parameter(description = "Minimum work rate threshold", example = "1.5", required = true)
        @RequestParam minWorkRate: BigDecimal
    ): ResponseEntity<List<HighWorkRateReportDto>> {
        validatePeriod(period)
        val result = reportService.getHighWorkRateEmployees(period, minWorkRate)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/multi-region")
    @Operation(
        summary = "Report 2: Employees working in multiple regions",
        description = "Returns employees who worked in different regions within the specified period, " +
                "along with the count of distinct regions and total salary amount."
    )
    fun getMultiRegionEmployees(
        @Parameter(description = "Period in format yyyy.MM", example = "2024.01", required = true)
        @RequestParam period: String
    ): ResponseEntity<List<MultiRegionEmployeeReportDto>> {
        validatePeriod(period)
        val result = reportService.getMultiRegionEmployees(period)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/organization-salary/{organizationId}")
    @Operation(
        summary = "Report 3: Organization salary report with tree hierarchy",
        description = "Returns all employees in the organization tree (including child organizations) " +
                "with their salary amounts and average salary."
    )
    fun getOrganizationSalaryReport(
        @Parameter(description = "Organization ID (root)", required = true)
        @PathVariable organizationId: UUID,

        @Parameter(description = "Period in format yyyy.MM", example = "2024.01", required = true)
        @RequestParam period: String
    ): ResponseEntity<OrganizationSalaryReportDto> {
        validatePeriod(period)
        val result = reportService.getOrganizationSalaryReport(organizationId, period)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/salary-and-vacation")
    @Operation(
        summary = "Report 4: Employees receiving both salary and vacation",
        description = "Returns employees who received both SALARY and VACATION calculation types " +
                "in the specified period, along with both amounts."
    )
    fun getSalaryAndVacationEmployees(
        @Parameter(description = "Period in format yyyy.MM", example = "2024.01", required = true)
        @RequestParam period: String
    ): ResponseEntity<List<SalaryVacationReportDto>> {
        validatePeriod(period)
        val result = reportService.getSalaryAndVacationEmployees(period)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/validate-period")
    @Operation(
        summary = "Validate period format",
        description = "Validates if the period string is in the correct yyyy.MM format."
    )
    fun validatePeriodFormat(
        @Parameter(description = "Period to validate", example = "2024.01")
        @RequestParam period: String
    ): ResponseEntity<Map<String, Any>> {
        val isValid = reportService.validatePeriodFormat(period)
        return ResponseEntity.ok(
            mapOf(
                "period" to period,
                "isValid" to isValid,
                "message" to if (isValid) "Period format is valid" else "Invalid period format. Expected: yyyy.MM"
            )
        )
    }

    private fun validatePeriod(period: String) {
        if (!reportService.validatePeriodFormat(period)) {
            throw BadRequestException(arrayOf("2024.01"), "Invalid period format. Expected: yyyy.MM")
        }
    }

}