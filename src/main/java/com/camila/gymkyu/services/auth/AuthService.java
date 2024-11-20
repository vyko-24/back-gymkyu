package com.camila.gymkyu.services.auth;

import com.camila.gymkyu.config.ApiResponse;
import com.camila.gymkyu.controllers.auth.dto.SignDto;
import com.camila.gymkyu.controllers.auth.dto.SignedDto;
import com.camila.gymkyu.models.usuarios.Usuario;
import com.camila.gymkyu.security.jwt.JwtProvider;
import com.camila.gymkyu.services.usuarios.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AuthService {
    private final UsuarioService service;
    private final AuthenticationManager manager;
    private final JwtProvider provider;

    public AuthService(UsuarioService service, AuthenticationManager manager, JwtProvider provider) {
        this.service = service;
        this.manager = manager;
        this.provider = provider;
    }

    public ResponseEntity<ApiResponse> signIn(SignDto dto){
        try {
            Optional<Usuario> foundUser = service.findUserByUsername(dto.getEmail());
            if (foundUser.isEmpty())
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, true, "Usuario No Encontrado"), HttpStatus.BAD_REQUEST);
            Usuario user = foundUser.get();
            if (!user.getStatus())
                return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, true, "Inactivo"), HttpStatus.BAD_REQUEST);
            if (!user.getBlocked())
                return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED, true, "Bloqueado"), HttpStatus.BAD_REQUEST);

            System.out.println("Email: " + dto.getEmail() + " Password: " + dto.getPassword());
            Authentication auth = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
            System.out.println("banana");
            System.out.println(dto);
            SecurityContextHolder.getContext().setAuthentication(auth);
            String token = provider.generateToken(auth);
            //esto va en el controller del auth faaaaaaak
            //SignedDto signedDto = new SignedDto(token, "Bearer", user, user.getRole());
            SignedDto signedDto = new SignedDto(token, "Bearer", user, user.getRole());
            return new ResponseEntity<>(
                    new ApiResponse(signedDto, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            String message = "CredentialsMismatch";
            if (e instanceof DisabledException)
                message = "UserDisabled";
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST, true, message),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
