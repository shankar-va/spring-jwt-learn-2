package com.example.spring_jwtlearn.repository;

import com.example.spring_jwtlearn.Enum.Role;
import com.example.spring_jwtlearn.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Roles, Long> {

    @Query("""
                SELECT DISTINCT r
                FROM Roles r
                LEFT JOIN FETCH r.privileges
                WHERE r IN :roles
            """)
    List<Roles> findRolesWithPrivileges(@Param("roles") Set<Roles> roles);


    Optional<Roles> findByRole(Role role);

}
