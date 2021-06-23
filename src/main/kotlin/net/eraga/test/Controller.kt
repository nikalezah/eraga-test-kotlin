package net.eraga.test

import net.eraga.test.models.Contact
import net.eraga.test.models.File
import net.eraga.test.models.Person
import net.eraga.test.models.User
import net.eraga.test.repositories.ContactRepository
import net.eraga.test.repositories.FileRepository
import net.eraga.test.repositories.PersonRepository
import net.eraga.test.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.io.FileNotFoundException
import java.io.IOException
import java.util.function.Consumer
import java.util.function.Supplier
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class Controller {
    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val personRepository: PersonRepository? = null

    @Autowired
    private val fileRepository: FileRepository? = null

    @Autowired
    private val contactRepository: ContactRepository? = null
    @PostMapping("register")
    fun registerUser(@RequestBody user: @Valid User?) {
        if (userRepository!!.existsByEmail(user!!.email)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
        user.setPassword(BCryptPasswordEncoder().encode(user.password))
        userRepository.save(user)
    }

    @PostMapping("person")
    fun createPerson(@RequestBody person: @Valid Person?): Person? {
        person!!.contacts!!.forEach(Consumer { contact: Contact ->
            contact.person = person
        })
        return personRepository!!.save(person)
    }

    @PostMapping("persons")
    fun createPersons(@RequestBody persons: @Valid MutableList<Person?>?) {
        persons!!.forEach(Consumer { person: Person? -> createPerson(person) })
    }

    @PostMapping("person/{id}/photo")
    @Throws(IOException::class)
    fun uploadFile(
        @PathVariable id: Int,
        @RequestParam("photo") mFile: MultipartFile
    ): Person {
        val person = personRepository!!.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }!!
        val file = mFile.originalFilename?.let { mFile.contentType?.let { it1 -> File(it, it1, mFile.bytes) } }
        if (file != null) {
            file.person = person
        }
        fileRepository?.save(file)
        return person
    }

    @PostMapping("person/{id}/contact")
    fun addContact(
        @PathVariable id: Int,
        @RequestBody contact: Contact
    ): Person? {
        val person: Person? = personRepository?.findById(id)
            ?.orElseThrow(Supplier { ResponseStatusException(HttpStatus.NOT_FOUND) })
        contact.person = person
        contactRepository?.save(contact)
        return person
    }

    @GetMapping("persons")
    fun getPersons(@RequestParam page: Int): Page<Person?>? {
        return personRepository?.findAll(PageRequest.of(page, 10, Sort.unsorted()))
    }

    @GetMapping("person/{id}")
    fun getPerson(@PathVariable id: Int): Person? {
        return personRepository?.findById(id)
            ?.orElseThrow(Supplier { ResponseStatusException(HttpStatus.NOT_FOUND) })
    }

    @GetMapping("/download/{fileId}")
    @Throws(FileNotFoundException::class)
    fun downloadFile(@PathVariable fileId: String): ResponseEntity<Resource> {
        val file: File? = fileRepository?.findById(fileId)
            ?.orElseThrow(Supplier { ResponseStatusException(HttpStatus.NOT_FOUND) })
        if (file != null) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.name + "\"")
                .body(ByteArrayResource(file.data))
        }
        return ResponseEntity.noContent().build();
    }

    @get:GetMapping("report")
    val report: List<Report.Region?>?
        get() = personRepository?.let { Report(it).getRegions() }

    @Transactional
    @PutMapping("person/{id}")
    fun updatePerson(
        @PathVariable id: Int,
        @RequestBody newPerson: Person
    ): Person? {
        val person: Person? = personRepository?.findById(id)
            ?.orElseThrow(Supplier { ResponseStatusException(HttpStatus.NOT_FOUND) })
        if (person != null) {
            person.name = newPerson.name
        }
        if (person != null) {
            person.region = newPerson.region
        }
        if (person != null) {
            person.city = newPerson.city
        }
        if (person != null) {
            person.comment = newPerson.comment
        }
        contactRepository?.deleteByPerson(person)
        if (person != null) {
            person.contacts = newPerson.contacts
        }
        if (person != null) {
            person.contacts?.forEach(Consumer { contact: Contact ->
                contact.person = person
                contactRepository?.save(contact)
            })
        }
        return personRepository?.save(person)
    }

    @DeleteMapping("person/{id}")
    fun deletePerson(@PathVariable id: Int): ResponseEntity<*> {
        val person: Person? = personRepository?.findById(id)
            ?.orElseThrow(Supplier { ResponseStatusException(HttpStatus.NOT_FOUND) })
        if (person != null) {
            personRepository?.delete(person)
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body<Any>(null)
    }
}