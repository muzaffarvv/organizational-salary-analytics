package uz.pdp.organizationalsalaryanalytics.controller


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uz.pdp.organizationalsalaryanalytics.dto.CreateRegionDto
import uz.pdp.organizationalsalaryanalytics.dto.RegionDto
import uz.pdp.organizationalsalaryanalytics.service.RegionService
import java.util.*

@RestController
@RequestMapping("/regions")
@Tag(name = "Regions", description = "Region management APIs")
class RegionController(
    private val regionService: RegionService
) {

    @GetMapping
    @Operation(summary = "Get all regions")
    fun getAll(pageable: Pageable): ResponseEntity<Page<RegionDto>> {
        return ResponseEntity.ok(regionService.getAll(pageable))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get region by ID")
    fun getById(@PathVariable id: UUID): ResponseEntity<RegionDto> {
        return ResponseEntity.ok(regionService.getById(id))
    }

    @PostMapping
    @Operation(summary = "Create new region")
    fun create(@RequestBody dto: CreateRegionDto): ResponseEntity<RegionDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(regionService.create(dto))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update region")
    fun update(@PathVariable id: UUID, @RequestBody dto: RegionDto): ResponseEntity<RegionDto> {
        return ResponseEntity.ok(regionService.update(id, dto))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete region")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        regionService.delete(id)
        return ResponseEntity.noContent().build()
    }
}