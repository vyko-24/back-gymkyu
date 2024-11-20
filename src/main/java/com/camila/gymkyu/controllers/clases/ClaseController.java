package com.camila.gymkyu.controllers.clases;

import com.camila.gymkyu.config.ApiResponse;
import com.camila.gymkyu.controllers.clases.dto.AddPart;
import com.camila.gymkyu.controllers.clases.dto.ClaseDto;
import com.camila.gymkyu.services.clases.ClaseService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gymkyu/clase")
@CrossOrigin(origins = {"*"})
@RequiredArgsConstructor
public class ClaseController {
    private final ClaseService service;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id){
        return service.findById(id);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ApiResponse> getByName(@PathVariable("nombre") String nombre){
        return service.findByName(nombre);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<ApiResponse> getByUser(@PathVariable("id") Long id){
        return service.findClasesByUser(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@RequestBody ClaseDto dto, @PathVariable("id") Long id){
        return service.update(dto.toEntity(), id);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> register(@RequestBody ClaseDto dto){
        return service.save(dto.toEntity());
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<ApiResponse> changeStatus(@PathVariable("id") Long id){
        return service.changeStatus(id);
    }

    @PatchMapping("/newPart/{id}")
    public ResponseEntity<ApiResponse> addParticipant(@PathVariable("id") Long id, @RequestBody AddPart request) throws MessagingException {
        return service.addParticipant(id,  request.getUserId());
    }

    @PatchMapping("/delPart/{id}")
    public ResponseEntity<ApiResponse> removeParticipant(@PathVariable("id") Long id, @RequestBody AddPart request){
        return service.removeParticipant(id,  request.getUserId());
    }
}
