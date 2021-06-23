package net.eraga.test.repositories

import net.eraga.test.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User?, Int?> {
    fun findByEmail(email: String?): User?
    fun existsByEmail(email: String?): Boolean
}