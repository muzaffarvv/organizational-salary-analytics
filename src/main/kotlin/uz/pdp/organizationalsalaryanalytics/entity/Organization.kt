package uz.pdp.organizationalsalaryanalytics.entity
import jakarta.persistence.*
import uz.pdp.organizationalsalaryanalytics.base.BaseModel
import java.util.*

@Entity
@Table(name = "organizations")
data class Organization(
    @Column(nullable = false, length = 75)
    var name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    var region: Region? = null,

    @Column(name = "parent_id")
    var parentId: UUID? = null,

    @Column(name = "is_last", nullable = false)
    var isLast: Boolean = false,

    @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY)
    @Transient
    var children: MutableList<Organization> = mutableListOf()
) : BaseModel()