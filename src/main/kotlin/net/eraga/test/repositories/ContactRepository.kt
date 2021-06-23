package net.eraga.test.repositories

import net.eraga.test.models.Contact
import net.eraga.test.models.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository : JpaRepository<Contact?, Int?> {
    @Modifying
    @Query("delete from Contact c where c.person = :person")
    fun deleteByPerson(person: Person?)
}