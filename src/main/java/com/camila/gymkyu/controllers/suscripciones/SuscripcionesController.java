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
    private final SuscripcionService suscripcionService;

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse> findByUser(@PathVariable Long id){
        return suscripcionService.findByUser(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> findById(@PathVariable Long id){
        return suscripcionService.findById(id);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> nuevaSuscripcion(@RequestBody SuscripcionesDto suscripcion){
        return suscripcionService.nuevaSuscripcion(suscripcion.toEntity());
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAll(){
        return suscripcionService.findAll();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> changeStatus(@PathVariable Long id){
        return suscripcionService.changeStatus(id);
    }

}
