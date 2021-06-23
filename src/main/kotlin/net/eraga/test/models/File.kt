package net.eraga.test.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
class File(
    @field:JsonIgnore var name: String,
    @field:JsonIgnore var type: String,
    @field:JsonIgnore var data: ByteArray
) {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    var id: String? = null

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "person_id")
    var person: Person? = null

}