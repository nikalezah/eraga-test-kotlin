package net.eraga.test.repositories

import net.eraga.test.models.File
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FileRepository : JpaRepository<File?, String?> {
    fun save(file: File?)
}