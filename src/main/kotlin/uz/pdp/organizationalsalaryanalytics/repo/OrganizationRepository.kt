package uz.pdp.organizationalsalaryanalytics.repo

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import uz.pdp.organizationalsalaryanalytics.base.BaseRepository
import uz.pdp.organizationalsalaryanalytics.entity.Organization
import java.util.*

@Repository
interface OrganizationRepository : BaseRepository<Organization> {

    fun findByParentId(parentId: UUID): List<Organization>

    @Query("""
        SELECT o FROM Organization o 
        LEFT JOIN FETCH o.region 
        WHERE o.id = :id
    """)
    fun findByIdWithRegion(@Param("id") id: UUID): Optional<Organization>

}