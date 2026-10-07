package com.example.spring_jwtlearn.model;

import com.example.spring_jwtlearn.Enum.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_role")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    @Builder.Default
    private Role role = Role.ROLE_USER;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_mapping", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    private Set<Privileges> privileges;

}
