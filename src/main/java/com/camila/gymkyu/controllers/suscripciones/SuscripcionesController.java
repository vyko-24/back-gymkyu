package com.camila.gymkyu.controllers.suscripciones;

import com.camila.gymkyu.config.ApiResponse;
import com.camila.gymkyu.services.suscripcion.SuscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gymkyu/suscripcion")
@CrossOrigin(origins = {"*"})
@RequiredArgsConstructor
public class SuscripcionesController {
    private final SuscripcionService service;

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse> findByUser(@PathVariable Long id){
        return service.findByUser(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> findById(@PathVariable Long id){
        return service.findById(id);
    }

    @GetMapping("/ganancias/")
    public ResponseEntity<ApiResponse> findGanacias(){
        return service.allPrecios();
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> nuevaSuscripcion(@RequestBody SuscripcionesDto suscripcion){
        return service.nuevaSuscripcion(suscripcion.toEntity());
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAll(){
        return service.findAll();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> changeStatus(@PathVariable Long id){
        return service.changeStatus(id);
    }

}
