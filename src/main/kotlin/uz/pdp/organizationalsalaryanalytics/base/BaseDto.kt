package uz.pdp.organizationalsalaryanalytics.base

import java.time.LocalDateTime
import java.util.*

interface BaseDto {
    val id: UUID?
    val createdAt: LocalDateTime?
    val updatedAt: LocalDateTime?
}