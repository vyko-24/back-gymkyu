package com.camila.gymkyu.models.suscripcion;

import com.camila.gymkyu.models.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuscripcionRepo extends JpaRepository<Suscripcion, Long> {
    List<Suscripcion> findByUsuario(Usuario usuario);
    List<Suscripcion> findByFechaFin(LocalDateTime fechaFin);
    List<Suscripcion> findByStatus(Boolean status);


    @Query("SELECT s FROM Suscripcion s WHERE s.fechaInicio >= :fechaInicio")
    List<Suscripcion> findSuscripcionesUltimosDosAnios(@Param("fechaInicio") LocalDateTime fechaInicio);
}
