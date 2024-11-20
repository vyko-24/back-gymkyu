package com.camila.gymkyu.security.service;

import com.camila.gymkyu.models.usuarios.Usuario;
import com.camila.gymkyu.security.entity.UserDetailsImpl;
import com.camila.gymkyu.services.usuarios.UsuarioService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioService service;

    public UserDetailsServiceImpl(UsuarioService service) {
        this.service = service;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> foundUser = service.findUserByUsername(username);
        if (foundUser.isPresent())
        return UserDetailsImpl.build(foundUser.get());
        throw new UsernameNotFoundException("Usuario no encontrado");
    }
}
