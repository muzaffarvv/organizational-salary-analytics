package uz.pdp.organizationalsalaryanalytics.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uz.pdp.organizationalsalaryanalytics.base.BaseService
import uz.pdp.organizationalsalaryanalytics.dto.CreateRegionDto
import uz.pdp.organizationalsalaryanalytics.dto.RegionDto
import uz.pdp.organizationalsalaryanalytics.entity.Region
import uz.pdp.organizationalsalaryanalytics.repo.RegionRepository

@Service
class RegionService(
    private val regionRepository: RegionRepository
) : BaseService<Region, RegionDto>(regionRepository) {

    @Transactional
    fun create(dto: CreateRegionDto): RegionDto {
        val region = Region(name = dto.name)
        val saved = regionRepository.save(region)
        return toDto(saved)
    }

    override fun toDto(entity: Region): RegionDto {
        return RegionDto(
            id = entity.id,
            name = entity.name,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    override fun toEntity(dto: RegionDto): Region {
        return Region(name = dto.name).apply {
            id = dto.id
        }
    }
}