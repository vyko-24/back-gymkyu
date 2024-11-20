package com.camila.gymkyu.controllers.promos;

import com.camila.gymkyu.config.ApiResponse;
import com.camila.gymkyu.controllers.promos.dto.PromoDto;
import com.camila.gymkyu.services.promos.PromoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gymkyu/promo")
@CrossOrigin(origins = {"*"})
@RequiredArgsConstructor
public class PromoController {
    private final PromoService service;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id){
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@RequestBody PromoDto dto, @PathVariable("id") Long id){
        return service.update(dto.toEntity(), id);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> register(@RequestBody PromoDto dto){
        return service.save(dto.toEntity());
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<ApiResponse> changeStatus(@PathVariable("id") Long id){
        return service.changeStatus(id);
    }
}
