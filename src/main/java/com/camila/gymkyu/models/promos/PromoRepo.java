package com.camila.gymkyu.models.promos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromoRepo extends JpaRepository<Promos, Long> {
    Optional<Promos> findByNombre (String nombre);
    List<Promos> findByFechaFin (LocalDateTime fechaFin);

}
