package uz.pdp.organizationalsalaryanalytics.controller


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uz.pdp.organizationalsalaryanalytics.dto.CalculationRecordDto
import uz.pdp.organizationalsalaryanalytics.dto.CreateCalculationRecordDto
import uz.pdp.organizationalsalaryanalytics.dto.UpdateCalculationRecordDto
import uz.pdp.organizationalsalaryanalytics.service.CalculationRecordService
import java.util.*

@RestController
@RequestMapping("/calculation-records")
@Tag(name = "Calculation Records", description = "Calculation record management APIs")
class CalculationRecordController(
    private val calculationRecordService: CalculationRecordService
) {

    @GetMapping
    @Operation(summary = "Get all calculation records")
    fun getAll(pageable: Pageable): ResponseEntity<Page<CalculationRecordDto>> {
        return ResponseEntity.ok(calculationRecordService.getAll(pageable))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get calculation record by ID")
    fun getById(@PathVariable id: UUID): ResponseEntity<CalculationRecordDto> {
        return ResponseEntity.ok(calculationRecordService.getById(id))
    }

    @PostMapping
    @Operation(summary = "Create new calculation record")
    fun create(@RequestBody dto: CreateCalculationRecordDto): ResponseEntity<CalculationRecordDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(calculationRecordService.create(dto))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update calculation record")
    fun update(
        @PathVariable id: UUID,
        @RequestBody dto: UpdateCalculationRecordDto
    ): ResponseEntity<CalculationRecordDto> {
        return ResponseEntity.ok(calculationRecordService.update(id, dto))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete calculation record")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        calculationRecordService.delete(id)
        return ResponseEntity.noContent().build()
    }
}