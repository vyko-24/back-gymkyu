package com.camila.gymkyu.models.membresias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembresiaRepo extends JpaRepository<Membresia, Long> {
    Optional<Membresia> findByNombre (String nombre);
}
