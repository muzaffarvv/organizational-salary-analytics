package uz.pdp.organizationalsalaryanalytics.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uz.pdp.organizationalsalaryanalytics.base.BaseService
import uz.pdp.organizationalsalaryanalytics.exceptions.NotFoundException
import uz.pdp.organizationalsalaryanalytics.dto.CreateOrganizationDto
import uz.pdp.organizationalsalaryanalytics.dto.OrganizationDto
import uz.pdp.organizationalsalaryanalytics.dto.OrganizationTreeDto
import uz.pdp.organizationalsalaryanalytics.dto.UpdateOrganizationDto
import uz.pdp.organizationalsalaryanalytics.entity.Organization
import uz.pdp.organizationalsalaryanalytics.repo.OrganizationRepository
import uz.pdp.organizationalsalaryanalytics.repo.RegionRepository
import java.util.*

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
    private val regionRepository: RegionRepository
) : BaseService<Organization, OrganizationDto>(organizationRepository) {

    @Transactional
    fun create(dto: CreateOrganizationDto): OrganizationDto {
        val region = dto.regionId?.let {
            regionRepository.findById(it)
                .orElseThrow { NotFoundException(arrayOf("Organization, "),"Region not found with id: $it") }
        }

        val organization = Organization(
            name = dto.name,
            region = region,
            parentId = dto.parentId,
            isLast = dto.isLast
        )

        val saved = organizationRepository.save(organization)
        return toDto(saved)
    }

    @Transactional
    fun update(id: UUID, dto: UpdateOrganizationDto): OrganizationDto {
        val organization = organizationRepository.findById(id)
            .orElseThrow { NotFoundException(arrayOf("Organization, $id"),"Organization not found with id: $id") }

        dto.name?.let { organization.name = it }
        dto.isLast?.let { organization.isLast = it }
        dto.parentId?.let { organization.parentId = it }
        dto.regionId?.let { regionId ->
            organization.region = regionRepository.findById(regionId)
                .orElseThrow { NotFoundException(arrayOf("Region, $regionId"),"Region not found with id: $regionId") }
        }

        val updated = organizationRepository.save(organization)
        return toDto(updated)
    }

    @Transactional(readOnly = true)
    fun getOrganizationTree(id: UUID): OrganizationTreeDto {
        return buildTreeRecursive(id, 0)
    }

    @Transactional(readOnly = true)
    fun getAllOrganizationIdsInTree(rootId: UUID): List<UUID> {
        val result = mutableListOf<UUID>()
        collectOrganizationIds(rootId, result)
        return result
    }

    private fun collectOrganizationIds(orgId: UUID, result: MutableList<UUID>) {
        result.add(orgId)
        val children = organizationRepository.findByParentId(orgId)
        children.forEach { collectOrganizationIds(it.id!!, result) }
    }

    private fun buildTreeRecursive(id: UUID, level: Int): OrganizationTreeDto {
        val org = organizationRepository.findByIdWithRegion(id)
            .orElseThrow { NotFoundException(arrayOf("Organization, $id"),"Organization not found with id: $id") }

        val children = organizationRepository.findByParentId(id)
            .map { buildTreeRecursive(it.id!!, level + 1) }

        return OrganizationTreeDto(
            id = org.id!!,
            name = org.name,
            regionName = org.region?.name,
            level = level,
            parentId = org.parentId,
            children = children
        )
    }

    override fun toDto(entity: Organization): OrganizationDto {
        return OrganizationDto(
            id = entity.id,
            name = entity.name,
            regionId = entity.region?.id,
            regionName = entity.region?.name,
            parentId = entity.parentId,
            isLast = entity.isLast,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    override fun toEntity(dto: OrganizationDto): Organization {
        val region = dto.regionId?.let {
            regionRepository.findById(it).orElse(null)
        }

        return Organization(
            name = dto.name,
            region = region,
            parentId = dto.parentId,
            isLast = dto.isLast
        ).apply {
            id = dto.id
        }
    }
}