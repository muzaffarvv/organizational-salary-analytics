package uz.pdp.organizationalsalaryanalytics.base

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import uz.pdp.organizationalsalaryanalytics.exceptions.NotFoundException
import java.util.*

abstract class BaseService<E : BaseModel, D : BaseDto>(
    protected val repository: BaseRepository<E>
) {

    @Transactional(readOnly = true)
    open fun getAll(pageable: Pageable): Page<D> {
        return repository.findAll(pageable).map { toDto(it) }
    }

    @Transactional(readOnly = true)
    open fun getById(id: UUID): D {
        val entity = repository.findById(id)
            .orElseThrow { NotFoundException(arrayOf("$id"),"Entity not found with id: $id") } //todo duplicated 3 times
        return toDto(entity)
    }

    @Transactional
    open fun create(dto: D): D {
        val entity = toEntity(dto)
        val saved = repository.save(entity)
        return toDto(saved)
    }

    @Transactional
    open fun update(id: UUID, dto: D): D {
        if (!repository.existsById(id)) {
            throw NotFoundException(arrayOf("$id"),"Entity not found with id: $id")
        }
        val entity = toEntity(dto)
        entity.id = id
        val updated = repository.save(entity)
        return toDto(updated)
    }

    @Transactional
    open fun delete(id: UUID) {
        if (!repository.existsById(id)) {
            throw NotFoundException(arrayOf("$id"),"Entity not found with id: $id")
        }
        repository.deleteById(id)
    }

    protected abstract fun toDto(entity: E): D
    protected abstract fun toEntity(dto: D): E
}