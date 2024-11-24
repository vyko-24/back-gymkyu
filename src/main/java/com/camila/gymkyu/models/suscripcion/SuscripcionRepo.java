package com.camila.gymkyu.models.suscripcion;

import com.camila.gymkyu.models.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuscripcionRepo extends JpaRepository<Suscripcion, Long> {
    Optional<Suscripcion> findByUsuario(Usuario usuario);
    List<Suscripcion> findByFechaFin(LocalDateTime fechaFin);
    List<Suscripcion> findByStatus(Boolean status);
}
