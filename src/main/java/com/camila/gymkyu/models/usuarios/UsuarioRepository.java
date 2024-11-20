package com.camila.gymkyu.models.usuarios;

import com.camila.gymkyu.models.roles.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNombre (String nombre);
    List<Usuario> findByRole (Rol role);
}
