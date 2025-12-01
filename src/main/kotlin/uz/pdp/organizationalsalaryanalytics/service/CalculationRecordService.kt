package uz.pdp.organizationalsalaryanalytics.service
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uz.pdp.organizationalsalaryanalytics.base.BaseService
import uz.pdp.organizationalsalaryanalytics.exceptions.NotFoundException
import uz.pdp.organizationalsalaryanalytics.dto.CalculationRecordDto
import uz.pdp.organizationalsalaryanalytics.dto.CreateCalculationRecordDto
import uz.pdp.organizationalsalaryanalytics.dto.UpdateCalculationRecordDto
import uz.pdp.organizationalsalaryanalytics.entity.CalculationRecord
import uz.pdp.organizationalsalaryanalytics.repo.CalculationRecordRepository
import uz.pdp.organizationalsalaryanalytics.repo.EmployeeRepository
import uz.pdp.organizationalsalaryanalytics.repo.OrganizationRepository
import java.util.*

@Service
class CalculationRecordService(
    private val calculationRecordRepository: CalculationRecordRepository,
    private val employeeRepository: EmployeeRepository,
    private val organizationRepository: OrganizationRepository
) : BaseService<CalculationRecord, CalculationRecordDto>(calculationRecordRepository) {

    @Transactional
    fun create(dto: CreateCalculationRecordDto): CalculationRecordDto {
        val employee = employeeRepository.findById(dto.employeeId)
            .orElseThrow { NotFoundException(arrayOf("Employee, ${dto.employeeId}"),"Employee not found with id: ${dto.employeeId}") }

        val organization = organizationRepository.findById(dto.organizationId)
            .orElseThrow { NotFoundException(arrayOf("Organization, ${dto.organizationId}"),"Organization not found with id: ${dto.organizationId}") }

        val record = CalculationRecord(
            employee = employee,
            organization = organization,
            period = dto.period,
            calculationType = dto.calculationType,
            amount = dto.amount,
            workRate = dto.workRate
        )

        val saved = calculationRecordRepository.save(record)
        return toDto(saved)
    }

    @Transactional
    fun update(id: UUID, dto: UpdateCalculationRecordDto): CalculationRecordDto {
        val record = calculationRecordRepository.findById(id)
            .orElseThrow { NotFoundException(arrayOf("CalculationRecord, $id"),"CalculationRecord not found with id: $id") }

        dto.employeeId?.let {
            record.employee = employeeRepository.findById(it)
                .orElseThrow { NotFoundException(arrayOf("Employee, $it"),"Employee not found with id: $it") }
        }
        dto.organizationId?.let {
            record.organization = organizationRepository.findById(it)
                .orElseThrow { NotFoundException(arrayOf("Organization, $it"),"Organization not found with id: $it") }
        }
        dto.period?.let { record.period = it }
        dto.calculationType?.let { record.calculationType = it }
        dto.amount?.let { record.amount = it }
        dto.workRate?.let { record.workRate = it }

        val updated = calculationRecordRepository.save(record)
        return toDto(updated)
    }

    override fun toDto(entity: CalculationRecord): CalculationRecordDto {
        return CalculationRecordDto(
            id = entity.id,
            employeeId = entity.employee.id!!,
            employeeName = "${entity.employee.firstName} ${entity.employee.lastName}",
            organizationId = entity.organization.id!!,
            organizationName = entity.organization.name,
            period = entity.period,
            calculationType = entity.calculationType,
            amount = entity.amount,
            workRate = entity.workRate,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    override fun toEntity(dto: CalculationRecordDto): CalculationRecord {
        val employee = employeeRepository.findById(dto.employeeId)
            .orElseThrow { NotFoundException(arrayOf("Employee"),"Employee not found") }
        val organization = organizationRepository.findById(dto.organizationId)
            .orElseThrow { NotFoundException(arrayOf("Organization"),"Organization not found") }

        return CalculationRecord(
            employee = employee,
            organization = organization,
            period = dto.period,
            calculationType = dto.calculationType,
            amount = dto.amount,
            workRate = dto.workRate
        ).apply {
            id = dto.id
        }
    }
}