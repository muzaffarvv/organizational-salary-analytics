package uz.pdp.organizationalsalaryanalytics.service
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uz.pdp.organizationalsalaryanalytics.base.BaseService
import uz.pdp.organizationalsalaryanalytics.exceptions.NotFoundException
import uz.pdp.organizationalsalaryanalytics.dto.CreateEmployeeDto
import uz.pdp.organizationalsalaryanalytics.dto.EmployeeDto
import uz.pdp.organizationalsalaryanalytics.dto.UpdateEmployeeDto
import uz.pdp.organizationalsalaryanalytics.entity.Employee
import uz.pdp.organizationalsalaryanalytics.repo.EmployeeRepository
import uz.pdp.organizationalsalaryanalytics.repo.OrganizationRepository
import java.util.*

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository,
    private val organizationRepository: OrganizationRepository
) : BaseService<Employee, EmployeeDto>(employeeRepository) {

    @Transactional
    fun create(dto: CreateEmployeeDto): EmployeeDto {
        val organization = organizationRepository.findById(dto.organizationId)
            .orElseThrow { NotFoundException(arrayOf("Organization, ${dto.organizationId}"),"Organization not found with id: ${dto.organizationId}") }

        val employee = Employee(
            firstName = dto.firstName,
            lastName = dto.lastName,
            pinfl = dto.pinfl,
            hireDate = dto.hireDate,
            organization = organization
        )

        val saved = employeeRepository.save(employee)
        return toDto(saved)
    }

    @Transactional
    fun update(id: UUID, dto: UpdateEmployeeDto): EmployeeDto {
        val employee = employeeRepository.findById(id)
            .orElseThrow { NotFoundException(arrayOf("Employee, $id"),"Employee not found with id: $id") }

        dto.firstName?.let { employee.firstName = it }
        dto.lastName?.let { employee.lastName = it }
        dto.pinfl?.let { employee.pinfl = it }
        dto.hireDate?.let { employee.hireDate = it }
        dto.organizationId?.let { orgId ->
            employee.organization = organizationRepository.findById(orgId)
                .orElseThrow { NotFoundException(arrayOf("Organization, $orgId"),"Organization not found with id: $orgId") }
        }

        val updated = employeeRepository.save(employee)
        return toDto(updated)
    }

    override fun toDto(entity: Employee): EmployeeDto {
        return EmployeeDto(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            pinfl = entity.pinfl,
            hireDate = entity.hireDate,
            organizationId = entity.organization.id!!,
            organizationName = entity.organization.name,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    override fun toEntity(dto: EmployeeDto): Employee {
        val organization = organizationRepository.findById(dto.organizationId)
            .orElseThrow { NotFoundException(arrayOf("Organization"),"Organization not found") }

        return Employee(
            firstName = dto.firstName,
            lastName = dto.lastName,
            pinfl = dto.pinfl,
            hireDate = dto.hireDate,
            organization = organization
        ).apply {
            id = dto.id
        }
    }
}
