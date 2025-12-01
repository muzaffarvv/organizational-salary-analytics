package uz.pdp.organizationalsalaryanalytics.entity

import jakarta.persistence.*
import uz.pdp.organizationalsalaryanalytics.base.BaseModel
import java.time.LocalDate
import java.util.*

@Entity
@Table(
    name = "employees",
    indexes = [Index(name = "idx_employee_pinfl", columnList = "pinfl")]
)
data class Employee(
    @Column(name = "first_name", nullable = false, length = 50)
    var firstName: String,

    @Column(name = "last_name", nullable = false, length = 50)
    var lastName: String,

    @Column(nullable = false, length = 14)
    var pinfl: String,

    @Column(name = "hire_date", nullable = false)
    var hireDate: LocalDate,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    var organization: Organization
) : BaseModel()