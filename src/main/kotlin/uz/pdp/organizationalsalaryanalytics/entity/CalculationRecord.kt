package uz.pdp.organizationalsalaryanalytics.entity

import jakarta.persistence.*
import uz.pdp.organizationalsalaryanalytics.base.BaseModel
import uz.pdp.organizationalsalaryanalytics.enums.CalculationType
import java.math.BigDecimal

@Entity
@Table(
    name = "calculation_records",
    indexes = [
        Index(name = "idx_calc_period", columnList = "period"),
        Index(name = "idx_calc_employee", columnList = "employee_id"),
        Index(name = "idx_calc_organization", columnList = "organization_id"),
        Index(name = "idx_calc_type", columnList = "calculation_type")
    ]
)
data class CalculationRecord(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    var employee: Employee,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    var organization: Organization,

    @Column(nullable = false, length = 7)
    var period: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "calculation_type", nullable = false, length = 20)
    var calculationType: CalculationType,

    @Column(nullable = false, precision = 15, scale = 2)
    var amount: BigDecimal,

    @Column(name = "work_rate", nullable = false, precision = 5, scale = 2)
    var workRate: BigDecimal
) : BaseModel()