package uz.pdp.organizationalsalaryanalytics.repo

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import uz.pdp.organizationalsalaryanalytics.base.BaseRepository
import uz.pdp.organizationalsalaryanalytics.entity.Employee
import java.util.*

@Repository
interface EmployeeRepository : BaseRepository<Employee> {

    fun findByPinfl(pinfl: String): List<Employee>

    @Query("""
        SELECT e FROM Employee e 
        LEFT JOIN FETCH e.organization o
        LEFT JOIN FETCH o.region
        WHERE e.id = :id
    """)
    fun findByIdWithOrganization(@Param("id") id: UUID): Optional<Employee>

    @Query("""
        SELECT e FROM Employee e 
        WHERE e.organization.id IN :organizationIds
    """)
    fun findByOrganizationIds(@Param("organizationIds") organizationIds: List<UUID>): List<Employee>
}
