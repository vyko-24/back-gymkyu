package com.camila.gymkyu.config;

import com.camila.gymkyu.models.clases.Clases;
import com.camila.gymkyu.models.clases.ClasesRepo;
import com.camila.gymkyu.models.membresias.Membresia;
import com.camila.gymkyu.models.membresias.MembresiaRepo;
import com.camila.gymkyu.models.roles.Rol;
import com.camila.gymkyu.models.roles.RoleRepo;
import com.camila.gymkyu.models.suscripcion.Suscripcion;
import com.camila.gymkyu.models.suscripcion.SuscripcionRepo;
import com.camila.gymkyu.models.usuarios.Usuario;
import com.camila.gymkyu.models.usuarios.UsuarioRepository;
import com.camila.gymkyu.services.suscripcion.SuscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class InitialConfig implements CommandLineRunner {
    private final RoleRepo roleRepo;
    private final UsuarioRepository usuarioRepository;
    private final ClasesRepo claseRepo;
    private final MembresiaRepo membresiaRepo;
    private final PasswordEncoder encoder;
    private final SuscripcionRepo suscripcionRepo;

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public void run(String... args) throws Exception{
        Rol admin = getORSaveRole(new Rol("ADMIN"));
        Rol empleado = getORSaveRole(new Rol("EMPLEADO"));
        Rol cliente = getORSaveRole(new Rol("CLIENTE"));

        Usuario userAdmin = getOrSaveUser(new Usuario("Víctor", "Barrera", "Ocampo","cafatofofo@gmail.com", 20, "7773308599"
                , encoder.encode("admin"),"aaah",admin, true, true));
        Usuario userEmpleado = getOrSaveUser(new Usuario("Agles", "Avelar", "Ocampo","agles@gmail.com", 60, "7771234567"
                , encoder.encode("empleado"),"aaah",empleado, true, true));
        Usuario userClliente = getOrSaveUser(new Usuario("David", "Bahena", "Gomes","eddie@gmail.com", 80, "77799032599"
                , encoder.encode("cliente"),"aaah",cliente, true, true));
        Usuario userClliente2 = getOrSaveUser(new Usuario("Cristian", "Saldaña", "Otiz","cafatofo@gmail.com", 20, "77799513579"
                , encoder.encode("cliente"),"aaah",cliente, true, true));

        Clases clase1 = getOrSaveClase(new Clases("Yoga", "Pos es yoga qué más", "aag", true));
        saveUserClases(userClliente.getId(), clase1.getId());
        saveUserClases(userClliente2.getId(), clase1.getId());

        Clases clase2 = getOrSaveClase(new Clases("Zumba", "Pos es zumba qué más", "aag", true));
        saveUserClases(userClliente.getId(), clase2.getId());

        Membresia membresia1 = getOrSaveMembresia(new Membresia("Fit Express", "Membresia mensual", 500.0, true));
        Membresia membresia2 = getOrSaveMembresia(new Membresia("Six Pack", "Membresia mensual", 800.0, true));
        Membresia membresia3 = getOrSaveMembresia(new Membresia("Full Body", "Membresia mensual", 1000.0, true));


        Suscripcion suscripcion1 = getOrSaveSuscripcion(new Suscripcion(userClliente, membresia1, LocalDateTime.now(), LocalDateTime.now().plusMonths(2), true));
    }

    @Transactional
    public Rol getORSaveRole (Rol rol){
        Optional<Rol> foundRol = roleRepo.findByNombre(rol.getNombre());
        return foundRol.orElseGet(()-> roleRepo.saveAndFlush(rol));
    }

    @Transactional
    public Usuario getOrSaveUser(Usuario user){
        Optional<Usuario> foundUser = usuarioRepository.findByEmail(user.getEmail());
        return foundUser.orElseGet(() -> usuarioRepository.saveAndFlush(user));
    }

    @Transactional
    public Clases getOrSaveClase(Clases clase){
        Optional<Clases> foundClase = claseRepo.findByNombre(clase.getNombre());
        return foundClase.orElseGet(() -> claseRepo.saveAndFlush(clase));
    }

    @Transactional
    public Suscripcion getOrSaveSuscripcion(Suscripcion suscripcion){
        Optional<Suscripcion> foundSuscripcion = suscripcionRepo.findByUsuario(suscripcion.getUsuario());
        return foundSuscripcion.orElseGet(() -> suscripcionRepo.saveAndFlush(suscripcion));
    }

    @Transactional
    public Membresia getOrSaveMembresia(Membresia membresia){
        Optional<Membresia> foundMembresia = membresiaRepo.findByNombre(membresia.getNombre());
        return foundMembresia.orElseGet(() -> membresiaRepo.saveAndFlush(membresia));
    }
    @Transactional
    public void saveUserClases(Long userId, Long claseId){
        Optional<Usuario> foundUser = usuarioRepository.findById(userId);
        Optional<Clases> foundClase = claseRepo.findById(claseId);
        if(foundUser.isPresent() && foundClase.isPresent()){
            Usuario user = foundUser.get();
            Clases clase = foundClase.get();
            if(clase.getParticipantes().contains(user)){
                return;
            }
            clase.getParticipantes().add(user);
            claseRepo.save(clase);
        }
    }
}
