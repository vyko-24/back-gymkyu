package com.camila.gymkyu.services.clases;

import com.camila.gymkyu.config.ApiResponse;
import com.camila.gymkyu.models.clases.Clases;
import com.camila.gymkyu.models.clases.ClasesRepo;
import com.camila.gymkyu.models.usuarios.Usuario;
import com.camila.gymkyu.models.usuarios.UsuarioRepository;
import com.camila.gymkyu.services.email.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClaseService {
    private final ClasesRepo claseRepo;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    public ClaseService(ClasesRepo claseRepo, UsuarioRepository usuarioRepository) {
        this.claseRepo = claseRepo;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findByName(String name){
        Optional<Clases> foundClase = claseRepo.findByNombre(name);
        if(foundClase.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Clase No Encontrada"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ApiResponse(foundClase.get(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll(){
        return new ResponseEntity<>(new ApiResponse(claseRepo.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findClasesByUser(Long userId){
        Optional<Usuario> foundUser = usuarioRepository.findById(userId);
        if(foundUser.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Usuario No Encontrado"), HttpStatus.BAD_REQUEST);
        List<Clases> foundClases = claseRepo.findByParticipantes(foundUser.get());
        if(foundClases.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Clases No Encontradas"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ApiResponse(foundClases, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById(Long id){
        Optional<Clases> foundClase = claseRepo.findById(id);
        if(foundClase.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Clase No Encontrada"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ApiResponse(foundClase.get(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> save(Clases clase){
        clase.setStatus(true);
        Optional<Clases> foundClase = claseRepo.findByNombre(clase.getNombre());
        if(!foundClase.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Clase ya existe"), HttpStatus.BAD_REQUEST);
        clase= claseRepo.saveAndFlush(clase);
        return new ResponseEntity<>(new ApiResponse(claseRepo.save(clase), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> update(Clases clase, Long id){
        Optional<Clases> foundClase = claseRepo.findById(id);
        if (foundClase.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Clase No Encontrada"), HttpStatus.BAD_REQUEST);
        Clases existingClase = foundClase.get();
        existingClase.setNombre(clase.getNombre());
        existingClase.setDescripcion(clase.getDescripcion());
        existingClase.setFoto(clase.getFoto());
        return new ResponseEntity<>(new ApiResponse(claseRepo.save(existingClase), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> changeStatus(Long id){
        Optional<Clases> foundClase = claseRepo.findById(id);
        if (foundClase.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Clase No Encontrada"), HttpStatus.BAD_REQUEST);
        Clases clase = foundClase.get();
        clase.setStatus(!clase.getStatus());
        return new ResponseEntity<>(new ApiResponse(claseRepo.save(clase), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> addParticipant(Long claseId, Long userId) throws MessagingException {
        Optional<Clases> foundClase = claseRepo.findById(claseId);
        if (foundClase.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Clase No Encontrada"), HttpStatus.BAD_REQUEST);
        Clases clase = foundClase.get();
        Optional<Usuario> foundUser = usuarioRepository.findById(userId);
        if (foundUser.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Usuario No Encontrado"), HttpStatus.BAD_REQUEST);
        Usuario user = foundUser.get();
        clase.getParticipantes().add(user);
        // Llamada al servicio de correo para enviar el correo al usuario
        emailService.sendEmail(
                user.getEmail(),
                "Registro en clase " + clase.getNombre(),
                "Estimado " + user.getNombre() + ", te has registrado en la clase de " + clase.getNombre() + ". ¡Gracias por unirte!"
        );

        return new ResponseEntity<>(new ApiResponse(claseRepo.save(clase), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> removeParticipant(Long claseId, Long userId){
        Optional<Clases> foundClase = claseRepo.findById(claseId);
        if (foundClase.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Clase No Encontrada"), HttpStatus.BAD_REQUEST);
        Clases clase = foundClase.get();
        Optional<Usuario> foundUser = usuarioRepository.findById(userId);
        if (foundUser.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Usuario No Encontrado"), HttpStatus.BAD_REQUEST);
        Usuario user = foundUser.get();
        clase.getParticipantes().remove(user);
        return new ResponseEntity<>(new ApiResponse(claseRepo.save(clase), HttpStatus.OK), HttpStatus.OK);
    }
}
