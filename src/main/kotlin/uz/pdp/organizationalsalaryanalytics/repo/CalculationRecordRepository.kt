package uz.pdp.organizationalsalaryanalytics.repo
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import uz.pdp.organizationalsalaryanalytics.base.BaseRepository
import uz.pdp.organizationalsalaryanalytics.dto.*
import uz.pdp.organizationalsalaryanalytics.entity.CalculationRecord
import java.math.BigDecimal
import java.util.*

@Repository
interface CalculationRecordRepository : BaseRepository<CalculationRecord> {

    @Query("""
        SELECT new uz.pdp.organizationalsalaryanalytics.dto.HighWorkRateReportDto(
            e.pinfl,
            e.id,
            SUM(c.workRate)
        )
        FROM CalculationRecord c
        JOIN c.employee e
        WHERE c.period = :period
        GROUP BY e.pinfl, e.id
        HAVING SUM(c.workRate) > :minWorkRate
        ORDER BY SUM(c.workRate) DESC
    """)
    fun findHighWorkRateEmployees(
        @Param("period") period: String,
        @Param("minWorkRate") minWorkRate: BigDecimal
    ): List<HighWorkRateReportDto>

    @Query("""
    SELECT new uz.pdp.organizationalsalaryanalytics.dto.MultiRegionEmployeeReportDto(
        e.pinfl,
        e.id,
        e.firstName,
        e.lastName,
        COUNT(DISTINCT o.region.id),
        SUM(CASE WHEN c.calculationType = 'SALARY' THEN c.amount ELSE 0 END)
    )
    FROM CalculationRecord c
    JOIN c.employee e
    JOIN c.organization o
    WHERE c.period = :period
    GROUP BY e.pinfl, e.id, e.firstName, e.lastName
    HAVING COUNT(DISTINCT o.region.id) > 1
    ORDER BY COUNT(DISTINCT o.region.id) DESC
""")
    fun findMultiRegionEmployees(@Param("period") period: String): List<MultiRegionEmployeeReportDto>

    @Query("""
        SELECT e.id, e.firstName, e.lastName, e.pinfl,
               COALESCE(SUM(CASE WHEN c.calculationType = 'SALARY' THEN c.amount ELSE 0 END), 0)
        FROM Employee e
        LEFT JOIN CalculationRecord c ON c.employee.id = e.id AND c.period = :period
        WHERE e.organization.id IN :organizationIds
        GROUP BY e.id, e.firstName, e.lastName, e.pinfl
    """)
    fun findEmployeeSalariesByOrganizations(
        @Param("organizationIds") organizationIds: List<UUID>,
        @Param("period") period: String
    ): List<Array<Any>>

    @Query("""
        SELECT new uz.pdp.organizationalsalaryanalytics.dto.SalaryVacationReportDto(
            e.id,
            e.firstName,
            e.lastName,
            e.pinfl,
            MAX(CASE WHEN c.calculationType = 'SALARY' THEN c.amount ELSE 0 END),
            MAX(CASE WHEN c.calculationType = 'VACATION' THEN c.amount ELSE 0 END)
        )
        FROM CalculationRecord c
        JOIN c.employee e
        WHERE c.period = :period
          AND c.calculationType IN ('SALARY', 'VACATION')
        GROUP BY e.id, e.firstName, e.lastName, e.pinfl
        HAVING SUM(CASE WHEN c.calculationType = 'SALARY' THEN 1 ELSE 0 END) > 0
           AND SUM(CASE WHEN c.calculationType = 'VACATION' THEN 1 ELSE 0 END) > 0
    """)
    fun findSalaryAndVacationEmployees(@Param("period") period: String): List<SalaryVacationReportDto>

    fun findByPeriod(period: String): List<CalculationRecord>

    fun findByEmployeeIdAndPeriod(employeeId: UUID, period: String): List<CalculationRecord>
}