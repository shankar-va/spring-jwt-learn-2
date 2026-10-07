package com.example.spring_jwtlearn.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_info")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id")
    @SequenceGenerator(name = "user_id", sequenceName = "seq_user_id", allocationSize = 1)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email_id", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Builder.Default
    @Column(name = "isEnabled", nullable = false)
    private boolean enabled = true;

    @Builder.Default
    @Column(name = "is_account_valid", nullable = false)
    private boolean isAccountNonLocked = true;

    @Builder.Default
    @Column(name = "is_account_non_expired", nullable = false)
    private boolean isAccountNonExpired = true;

    @Builder.Default
    @Column(name = "is_credentials_non_expired", nullable = false)
    private boolean isCredentialsNonExpired = true;

    @Column(name = "role_info")
    @JoinTable(name = "role_infos", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Roles> roles;

}
