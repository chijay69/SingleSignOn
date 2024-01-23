package com.example.SingleSignOn.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


// import javax.persistence.Entity;
// import javax.persistence.Id;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"user\"", indexes = {@Index(name = "idx_username", columnList = "username")})
public class User implements UserDetails {
    @SequenceGenerator(
            name = "user_id_auto",
            sequenceName = "user_id_auto",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "user_id_auto"
    )
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Boolean locked;
    private Boolean enabled;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<?extends GrantedAuthority> getAuthorities () {
        return role.grantedAuthorities();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

