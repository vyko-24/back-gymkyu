package com.camila.gymkyu.services.usuarios;

import com.camila.gymkyu.config.ApiResponse;
import com.camila.gymkyu.models.membresias.Membresia;
import com.camila.gymkyu.models.membresias.MembresiaRepo;
import com.camila.gymkyu.models.promos.PromoRepo;
import com.camila.gymkyu.models.promos.Promos;
import com.camila.gymkyu.models.roles.Rol;
import com.camila.gymkyu.models.roles.RoleRepo;
import com.camila.gymkyu.models.suscripcion.Suscripcion;
import com.camila.gymkyu.models.suscripcion.SuscripcionRepo;
import com.camila.gymkyu.models.usuarios.Usuario;
import com.camila.gymkyu.models.usuarios.UsuarioRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RoleRepo roleRepo;
    private final SuscripcionRepo suscripcionRepo;
    private final MembresiaRepo membresiaRepo;
    private final PromoRepo promoRepo;

    public UsuarioService(UsuarioRepository usuarioRepository, RoleRepo roleRepo, SuscripcionRepo suscripcionRepo, MembresiaRepo membresiaRepo, PromoRepo promoRepo) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepo = roleRepo;
        this.suscripcionRepo = suscripcionRepo;
        this.membresiaRepo = membresiaRepo;
        this.promoRepo = promoRepo;
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findUserByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findUserByUsername(String username) {
        return usuarioRepository.findByEmail(username);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById(Long id){
        Optional<Usuario> foundUser = usuarioRepository.findById(id);
        if(foundUser.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Usuario No Encontrado"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ApiResponse(foundUser.get(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll(){
        return new ResponseEntity<>(new ApiResponse(usuarioRepository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findUserByRole(Long roleId) {
        Optional<Rol> foundRole = roleRepo.findById(roleId);
        if (foundRole.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Rol No Encontrado"), HttpStatus.BAD_REQUEST);
        List<Usuario> foundUsers = usuarioRepository.findByRole(foundRole.get());
        if (foundUsers.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Usuarios No Encontrados"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ApiResponse(foundUsers, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> changeUserStatus(Long id) {
        Optional<Usuario> foundUser = usuarioRepository.findById(id);
        if (foundUser.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Usuario No Encontrado"), HttpStatus.BAD_REQUEST);
        Usuario user = foundUser.get();
        user.setStatus(!user.getStatus());
        return new ResponseEntity<>(new ApiResponse(usuarioRepository.save(user), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> createUserSuscribed(Usuario user, Long memId, Boolean mes) {
        Optional<Usuario> foundUser = usuarioRepository.findByEmail(user.getEmail());
        if(foundUser.isPresent())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Usuario ya existe"), HttpStatus.BAD_REQUEST);
        Optional<Membresia> foundMembresia = membresiaRepo.findById(memId);
        if(foundMembresia.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Membresia No Encontrada"), HttpStatus.BAD_REQUEST);

        Membresia membresia = foundMembresia.get();
        Double precioFinal = membresia.getPrecio();
        if (membresia.getPromos() != null && !membresia.getPromos().isEmpty()) {
            for (Promos promo : membresia.getPromos()) {
                    if (promo.getStatus()) {
                        precioFinal -= (precioFinal * promo.getPorcentaje() / 100);
                    }
                }
            }

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setUsuario(user);
        suscripcion.setMembresia(membresia);
        suscripcion.setPrecio(precioFinal);
        suscripcion.setFechaInicio(LocalDate.now().atStartOfDay());
        if(mes){
            suscripcion.setFechaFin(LocalDate.now().plusMonths(1).atStartOfDay());
        }else{
            suscripcion.setFechaFin(LocalDate.now().plusYears(1).atStartOfDay());
        }

        user.setStatus(true);
        user.setBlocked(true);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setContrasena(encoder.encode(user.getContrasena()));
        usuarioRepository.save(user);
        suscripcionRepo.save(suscripcion);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED, false, "Usuario creado y suscrito exitosamente"), HttpStatus.CREATED);

    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> updateUser(Usuario user, Long id) {
        Optional<Usuario> foundUser = usuarioRepository.findById(id);
        if (foundUser.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Usuario No Encontrado"), HttpStatus.BAD_REQUEST);
        Usuario existingUser = foundUser.get();
        existingUser.setNombre(user.getNombre());
        existingUser.setApMaterno(user.getApMaterno());
        existingUser.setApPaterno(user.getApPaterno());
        existingUser.setEmail(user.getEmail());
        existingUser.setFoto(user.getFoto());
        existingUser.setTelefono(user.getTelefono());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        existingUser.setContrasena(encoder.encode(user.getContrasena()));
        existingUser.setRole(user.getRole());
        return new ResponseEntity<>(new ApiResponse(usuarioRepository.save(existingUser), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<ApiResponse> saveUser(Usuario user) {
        user.setStatus(true);
        user.setBlocked(true);
        Optional<Usuario> foundUser = usuarioRepository.findByEmail(user.getEmail());
        if (foundUser.isPresent())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Usuario ya existe"), HttpStatus.BAD_REQUEST);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setContrasena(encoder.encode(user.getContrasena()));
        user = usuarioRepository.saveAndFlush(user);
        return new ResponseEntity<>(new ApiResponse(usuarioRepository.save(user), HttpStatus.OK), HttpStatus.OK);
    }
}
