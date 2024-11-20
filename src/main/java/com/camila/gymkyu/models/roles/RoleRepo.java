package com.camila.gymkyu.models.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String name);

}
