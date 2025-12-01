package uz.pdp.organizationalsalaryanalytics.repo

import org.springframework.stereotype.Repository
import uz.pdp.organizationalsalaryanalytics.base.BaseRepository
import uz.pdp.organizationalsalaryanalytics.entity.Region
import java.util.*

@Repository
interface RegionRepository : BaseRepository<Region> {
    fun findByName(name: String): Optional<Region>
}