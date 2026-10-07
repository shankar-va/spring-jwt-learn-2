package com.example.spring_jwtlearn.model;

import com.example.spring_jwtlearn.Enum.Privilege;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Privileges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "privilege_name")
    private Privilege privilege = Privilege.READ_PRIVILEGE;

}
