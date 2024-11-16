package com.lms.onlinelms.usermanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.*;

@EqualsAndHashCode
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @Size(max = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Size(max = 100)
    private String lastName;

    @Column(nullable = false)
    @Size(max = 100)
    @Email
    private String email;

    @Column(nullable = false)
    @Size(max = 100)
    private String myPassword;

    @JoinColumn(name = "image_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL)
    private Image profileImage;

    private String phoneNumber;

    @Column(nullable = false)
    @Value("false")
    private Boolean isActive;

    @Column(nullable = false)
    @Value("false")
    private Boolean isBlocked;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id" ,referencedColumnName = "id",nullable = false)
    private Role role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public String getPassword() {
        return this.getMyPassword();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.getIsBlocked();
    }

    @Override
    public boolean isEnabled() {
        return this.getIsActive();
    }

}
