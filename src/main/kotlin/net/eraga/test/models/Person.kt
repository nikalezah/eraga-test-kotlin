package net.eraga.test.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

@Entity
class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    var id = 0
    var name: @NotBlank(message = "Name is required") String? = null
    var region = 0
    var city: String? = null

    @OneToMany(mappedBy = "person", cascade = [CascadeType.ALL])
    var photos: List<File>? = null
    var comment: String? = null

    @OneToMany(mappedBy = "person", cascade = [CascadeType.ALL])
    var contacts: @NotEmpty(message = "Contact is required") MutableList<Contact>? = null
}