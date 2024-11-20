package com.camila.gymkyu.controllers.membresias;

import com.camila.gymkyu.config.ApiResponse;
import com.camila.gymkyu.controllers.membresias.dto.MembresiaDto;
import com.camila.gymkyu.services.membresias.MembresiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gymkyu/membresia")
@CrossOrigin(origins = {"*"})
@RequiredArgsConstructor
public class MembresiaController {
    private final MembresiaService service;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id){
        return service.findById(id);
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<ApiResponse> changeStatus(@PathVariable Long id){
        return service.changeStatus(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@RequestBody MembresiaDto dto, @PathVariable Long id){
        return service.update(dto.toEntity(), id);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> register(@RequestBody MembresiaDto dto){
        return service.save(dto.toEntity());
    }
}
