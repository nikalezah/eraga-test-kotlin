package net.eraga.test.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    var id = 0

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "person_id")
    var person: Person? = null

    @Column(unique = true)
    var email: String? = null

    @Column(unique = true)
    var phoneNumber: String? = null
}