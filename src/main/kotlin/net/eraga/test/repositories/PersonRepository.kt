package net.eraga.test.repositories

import net.eraga.test.models.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<Person?, Int?> {
    fun save(person: Person?): Person?
}