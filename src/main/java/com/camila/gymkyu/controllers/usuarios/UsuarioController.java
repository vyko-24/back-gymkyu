package com.camila.gymkyu.controllers.usuarios;

import com.camila.gymkyu.config.ApiResponse;
import com.camila.gymkyu.controllers.usuarios.dto.UsuarioDto;
import com.camila.gymkyu.services.usuarios.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gymkyu/usuario")
@CrossOrigin(origins = {"*"})
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id){
        return service.findById(id);
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<ApiResponse> changeStatus(@PathVariable("id") Long id){
        return service.changeUserStatus(id);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> register(@RequestBody UsuarioDto dto){
        return service.saveUser(dto.toEntity());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@RequestBody UsuarioDto dto, @PathVariable("id") Long id){
        return service.updateUser(dto.toEntity(), id);
    }
}
