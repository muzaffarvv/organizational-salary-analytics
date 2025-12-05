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

//    SELECT
//    e.pinfl,
//    SUM(ct.rate) AS total_rate,           -- umumiy ishlangan rate
//    COUNT(DISTINCT e.id) AS employees_count -- bir xil PINFL ga ega employees
//    FROM CalculationTable ct
//    JOIN Employee e ON ct.employee_id = e.id
//    WHERE ct.date >= '2024-01-01'
//    AND ct.date < '2024-02-01'
//    AND ct.rate > 1.0                       -- rate dan ko'p
//    GROUP BY e.pinfl
//    HAVING COUNT(DISTINCT e.id) > 1          -- bir xil PINFL lilar
//    ORDER BY total_rate DESC;
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

//    SELECT
//    e.pinfl,
//    COUNT(DISTINCT ct.organization_id) AS total_organizations, -- organization soni
//    SUM(ct.amount) AS total_salary,                            -- umumiy salary
//    COUNT(DISTINCT o.region_id) AS total_regions               -- ishlagan region lar soni
//    FROM CalculationTable ct
//    JOIN Employee e ON ct.employee_id = e.id
//    JOIN Organization o ON ct.organization_id = o.id
//    WHERE ct.date >= '2024-01-01'
//    AND ct.date < '2024-02-01'
//    GROUP BY e.pinfl
//    HAVING COUNT(DISTINCT o.region_id) > 1                        -- turli region da ishlaganlar
//    ORDER BY total_salary DESC;
    fun findMultiRegionEmployees(@Param("period") period: String): List<MultiRegionEmployeeReportDto>

    @Query("""
        SELECT e.id, e.firstName, e.lastName, e.pinfl,
               COALESCE(SUM(CASE WHEN c.calculationType = 'SALARY' THEN c.amount ELSE 0 END), 0)
        FROM Employee e
        LEFT JOIN CalculationRecord c ON c.employee.id = e.id AND c.period = :period
        WHERE e.organization.id IN :organizationIds
        GROUP BY e.id, e.firstName, e.lastName, e.pinfl
    """)

//    SELECT e.id, e.first_name, e.last_name, e.pinfl,
//    o.id AS org_id,
//    o.name AS org_name,
//    AVG(ct.amount) AS avg_salary  -- o'rtacha salary
//    FROM CalculationTable ct
//    JOIN Employee e ON ct.employee_id = e.id
//    JOIN Organization o ON e.organization_id = o.id
//    WHERE ct.date >= '2024-01-01'
//    AND ct.date < '2024-02-01'
//    AND (o.id = :organization_id OR o.parent = :organization_id)    -- berilgan org va child org lar
//    GROUP BY e.id, e.first_name, e.last_name, e.pinfl, o.id, o.name
//    ORDER BY avg_salary DESC;

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
//    SELECT e.id, e.first_name, e.last_name, e.pinfl,
//    SUM(CASE WHEN ct.calculation_type = 'SALARY' THEN ct.amount ELSE 0 END) AS total_salary,
//    SUM(CASE WHEN ct.calculation_type = 'VACATION' THEN ct.amount ELSE 0 END) AS total_vacation
//    FROM CalculationTable ct
//    JOIN Employee e ON ct.employee_id = e.id
//    WHERE ct.date >= '2024-01-01'
//    AND ct.date < '2024-02-01'
//    AND ct.calculation_type IN ('SALARY', 'VACATION')
//    GROUP BY e.id, e.first_name, e.last_name, e.pinfl
//    HAVING SUM(CASE WHEN ct.calculation_type = 'SALARY' THEN 1 ELSE 0 END) > 0
//    AND SUM(CASE WHEN ct.calculation_type = 'VACATION' THEN 1 ELSE 0 END) > 0
//    ORDER BY total_salary DESC;
    fun findSalaryAndVacationEmployees(@Param("period") period: String): List<SalaryVacationReportDto>

    fun findByPeriod(period: String): List<CalculationRecord>

    fun findByEmployeeIdAndPeriod(employeeId: UUID, period: String): List<CalculationRecord>
}