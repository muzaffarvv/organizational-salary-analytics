package uz.pdp.organizationalsalaryanalytics.service


import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uz.pdp.organizationalsalaryanalytics.exceptions.NotFoundException
import uz.pdp.organizationalsalaryanalytics.dto.*
import uz.pdp.organizationalsalaryanalytics.repo.CalculationRecordRepository
import uz.pdp.organizationalsalaryanalytics.repo.EmployeeRepository
import uz.pdp.organizationalsalaryanalytics.repo.OrganizationRepository
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

@Service
class ReportService(
    private val calculationRecordRepository: CalculationRecordRepository,
    private val organizationService: OrganizationService,
    private val organizationRepository: OrganizationRepository
) {

    @Transactional(readOnly = true)
    fun getHighWorkRateEmployees(period: String, minWorkRate: BigDecimal): List<HighWorkRateReportDto> {
        return calculationRecordRepository.findHighWorkRateEmployees(period, minWorkRate)
    }

    @Transactional(readOnly = true)
    fun getMultiRegionEmployees(period: String): List<MultiRegionEmployeeReportDto> {
        return calculationRecordRepository.findMultiRegionEmployees(period)
    }

    @Transactional(readOnly = true)
    fun getOrganizationSalaryReport(organizationId: UUID, period: String): OrganizationSalaryReportDto {
        val organization = organizationRepository.findById(organizationId)
            .orElseThrow { NotFoundException(arrayOf("Organization, $organizationId"),"Organization not found with id: $organizationId") }

        val allOrgIds = organizationService.getAllOrganizationIdsInTree(organizationId)
        val results = calculationRecordRepository.findEmployeeSalariesByOrganizations(allOrgIds, period)

        val employees = results.map { row ->
            EmployeeSalaryDto(
                employeeId = row[0] as UUID,
                firstName = row[1] as String,
                lastName = row[2] as String,
                pinfl = row[3] as String,
                salaryAmount = (row[4] as? BigDecimal) ?: BigDecimal.ZERO
            )
        }

        val averageSalary = if (employees.isNotEmpty()) {
            val total = employees.sumOf { it.salaryAmount }
            total.divide(BigDecimal(employees.size), 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO

        return OrganizationSalaryReportDto(
            organizationId = organizationId,
            organizationName = organization.name,
            employees = employees,
            averageSalary = averageSalary
        )
    }

    @Transactional(readOnly = true)
    fun getSalaryAndVacationEmployees(period: String): List<SalaryVacationReportDto> {
        return calculationRecordRepository.findSalaryAndVacationEmployees(period)
    }

    fun validatePeriodFormat(period: String): Boolean {
        val regex = Regex("^\\d{4}\\.\\d{2}$")
        if (!regex.matches(period)) return false
        val month = period.split(".")[1].toIntOrNull() ?: return false
        return month in 1..12
    }
}
