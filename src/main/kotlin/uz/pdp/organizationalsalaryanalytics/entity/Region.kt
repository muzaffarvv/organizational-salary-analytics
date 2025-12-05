package uz.pdp.organizationalsalaryanalytics.entity

import jakarta.persistence.*
import uz.pdp.organizationalsalaryanalytics.base.BaseModel

@Entity
@Table(name = "regions")
data class Region(
    @Column(nullable = false, unique = true, length = 75)
    var name: String
) : BaseModel()