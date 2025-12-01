package uz.pdp.organizationalsalaryanalytics.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uz.pdp.organizationalsalaryanalytics.dto.CreateEmployeeDto
import uz.pdp.organizationalsalaryanalytics.dto.EmployeeDto
import uz.pdp.organizationalsalaryanalytics.dto.UpdateEmployeeDto
import uz.pdp.organizationalsalaryanalytics.service.EmployeeService
import java.util.*

@RestController
@RequestMapping("/employees")
@Tag(name = "Employees", description = "Employee management APIs")
class EmployeeController(
    private val employeeService: EmployeeService
) {

    @GetMapping
    @Operation(summary = "Get all employees")
    fun getAll(pageable: Pageable): ResponseEntity<Page<EmployeeDto>> {
        return ResponseEntity.ok(employeeService.getAll(pageable))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID")
    fun getById(@PathVariable id: UUID): ResponseEntity<EmployeeDto> {
        return ResponseEntity.ok(employeeService.getById(id))
    }

    @PostMapping
    @Operation(summary = "Create new employee")
    fun create(@RequestBody dto: CreateEmployeeDto): ResponseEntity<EmployeeDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(dto))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee")
    fun update(@PathVariable id: UUID, @RequestBody dto: UpdateEmployeeDto): ResponseEntity<EmployeeDto> {
        return ResponseEntity.ok(employeeService.update(id, dto))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        employeeService.delete(id)
        return ResponseEntity.noContent().build()
    }
}