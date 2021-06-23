package net.eraga.test.models

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
@Table(name = "user_entity")
class User : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id = 0

    @Column(unique = true)
    var email: @Email(regexp = ".+\\..+") @NotBlank(message = "Email is required") String? = null
    private var password: @NotEmpty(message = "Password is required") @Size(
        min = 5,
        message = "At least 5 characters are required"
    ) String? = null

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return setOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    override fun getUsername(): String {
        return email!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getPassword(): String {
        return password!!
    }

    fun setPassword(password: String?) {
        this.password = password
    }
}