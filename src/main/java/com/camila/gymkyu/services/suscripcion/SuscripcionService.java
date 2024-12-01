package com.camila.gymkyu.services.suscripcion;

import com.camila.gymkyu.config.ApiResponse;
import com.camila.gymkyu.controllers.suscripciones.SuscripcionesDto;
import com.camila.gymkyu.models.membresias.Membresia;
import com.camila.gymkyu.models.membresias.MembresiaRepo;
import com.camila.gymkyu.models.suscripcion.Suscripcion;
import com.camila.gymkyu.models.suscripcion.SuscripcionRepo;
import com.camila.gymkyu.models.usuarios.Usuario;
import com.camila.gymkyu.models.usuarios.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SuscripcionService {
    private final SuscripcionRepo repository;
    private final MembresiaRepo membresiaRepo;
    private final UsuarioRepository usuarioRepository;

    public SuscripcionService(SuscripcionRepo repository, MembresiaRepo membresiaRepo, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.membresiaRepo = membresiaRepo;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> findAll(){
        return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> changeStatus(Long id){
        Optional<Suscripcion> suscripcion = repository.findById(id);
        if(suscripcion.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Suscripcion No Encontrada"), HttpStatus.BAD_REQUEST);
        suscripcion.get().setStatus(!suscripcion.get().getStatus());
        return new ResponseEntity<>(new ApiResponse(repository.save(suscripcion.get()), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> nuevaSuscripcion(Suscripcion suscripcion){
        Optional<Usuario> usuario = usuarioRepository.findById(suscripcion.getUsuario().getId());
        if(usuario.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Usuario No Encontrado"), HttpStatus.BAD_REQUEST);
        Optional<Membresia> membresia = membresiaRepo.findById(suscripcion.getMembresia().getId());
        if(membresia.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Membresia No Encontrada"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ApiResponse(repository.save(suscripcion), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> allPrecios(){
        List<Suscripcion> suscripciones = repository.findAll();
        List<SuscripcionesDto> suscripcionesDtos = suscripciones.stream()
                .map(s -> new SuscripcionesDto(s.getFechaInicio(), s.getPrecio()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponse(suscripcionesDtos, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public void generarCSV(HttpServletResponse response) throws IOException{
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=suscripciones.csv");

        LocalDateTime fechaInicio = LocalDateTime.now().minusYears(2);

        List<Suscripcion> suscripciones = repository.findSuscripcionesUltimosDosAnios(fechaInicio);

        PrintWriter writer = response.getWriter();writer.println("Precio;Fecha"); // Usar punto y coma como separador
        for (Suscripcion s : suscripciones) {
            System.out.println("Precio: " + s.getPrecio());
            System.out.println("Fecha de Inicio: " + s.getFechaInicio());


            writer.printf("%.2f;%s%n", s.getPrecio(), s.getFechaInicio());
        }
        writer.flush();
    }


    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById(Long id){
        Optional<Suscripcion> foundSuscripcion = repository.findById(id);
        if(foundSuscripcion.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Suscripcion No Encontrada"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ApiResponse(foundSuscripcion.get(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findByUser(Long id){
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if(usuario.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Usuario No Encontrado"), HttpStatus.BAD_REQUEST);
        List<Suscripcion> foundSuscripcion = repository.findByUsuario(usuario.get());
        return new ResponseEntity<>(new ApiResponse(repository.findByUsuario(usuario.get()), HttpStatus.OK), HttpStatus.OK);
    }


    @Scheduled(cron = "0 0 0 * * ?") // Ejecuta cada día a medianoche
    @Transactional
    public void desactivarPromosVencidas() {
        List<Suscripcion> suscripcionesVencidas = repository.findByFechaFin(LocalDateTime.now());
        for (Suscripcion suscripcion : suscripcionesVencidas) {
            suscripcion.setStatus(false);
            repository.saveAndFlush(suscripcion);
        }
        System.out.println("Suscripciones vencidas desactivadas");
    }

}
