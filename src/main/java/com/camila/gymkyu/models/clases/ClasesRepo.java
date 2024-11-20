package com.camila.gymkyu.models.clases;

import com.camila.gymkyu.models.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClasesRepo extends JpaRepository<Clases, Long> {
    Optional<Clases> findByNombre(String name);
    List<Clases> findByParticipantes(Usuario user);
}
