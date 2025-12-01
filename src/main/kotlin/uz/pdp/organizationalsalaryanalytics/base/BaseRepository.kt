package uz.pdp.organizationalsalaryanalytics.base

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface BaseRepository<T : BaseModel> : JpaRepository<T, UUID>