package uz.pdp.organizationalsalaryanalytics.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uz.pdp.organizationalsalaryanalytics.dto.CreateOrganizationDto
import uz.pdp.organizationalsalaryanalytics.dto.OrganizationDto
import uz.pdp.organizationalsalaryanalytics.dto.OrganizationTreeDto
import uz.pdp.organizationalsalaryanalytics.dto.UpdateOrganizationDto
import uz.pdp.organizationalsalaryanalytics.service.OrganizationService
import java.util.*

@RestController
@RequestMapping("/organizations")
@Tag(name = "Organizations", description = "Organization management APIs")
class OrganizationController(
    private val organizationService: OrganizationService
) {

    @GetMapping
    @Operation(summary = "Get all organizations")
    fun getAll(pageable: Pageable): ResponseEntity<Page<OrganizationDto>> {
        return ResponseEntity.ok(organizationService.getAll(pageable))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get organization by ID")
    fun getById(@PathVariable id: UUID): ResponseEntity<OrganizationDto> {
        return ResponseEntity.ok(organizationService.getById(id))
    }

    @GetMapping("/{id}/tree")
    @Operation(summary = "Get organization tree (with all children)")
    fun getTree(@PathVariable id: UUID): ResponseEntity<OrganizationTreeDto> {
        return ResponseEntity.ok(organizationService.getOrganizationTree(id))
    }

    @PostMapping
    @Operation(summary = "Create new organization")
    fun create(@RequestBody dto: CreateOrganizationDto): ResponseEntity<OrganizationDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.create(dto))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update organization")
    fun update(@PathVariable id: UUID, @RequestBody dto: UpdateOrganizationDto): ResponseEntity<OrganizationDto> {
        return ResponseEntity.ok(organizationService.update(id, dto))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete organization")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        organizationService.delete(id)
        return ResponseEntity.noContent().build()
    }
}