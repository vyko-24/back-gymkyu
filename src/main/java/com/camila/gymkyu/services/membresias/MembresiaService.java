package com.camila.gymkyu.services.membresias;

import com.camila.gymkyu.config.ApiResponse;
import com.camila.gymkyu.controllers.membresias.dto.MembresiaDto;
import com.camila.gymkyu.models.membresias.Membresia;
import com.camila.gymkyu.models.membresias.MembresiaRepo;
import com.camila.gymkyu.models.promos.PromoRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MembresiaService {
    private final MembresiaRepo membresiaRepo;
    private final PromoRepo promoRepo;

    public MembresiaService(MembresiaRepo membresiaRepo, PromoRepo promoRepo) {
        this.membresiaRepo = membresiaRepo;
        this.promoRepo = promoRepo;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll() {
        List<Membresia> membresias = membresiaRepo.findAll();
        List<MembresiaDto> membresiaDtos = membresias.stream().map(membresia -> {
            double precioFinal = membresia.getPrecio();
            if (membresia.getPromos() != null) {
                double descuento = membresia.getPromos().getPorcentaje();
                precioFinal -= precioFinal * descuento;  // Aplica el descuento directamente
            }
            return new MembresiaDto(
                    membresia.getId(),
                    membresia.getNombre(),
                    membresia.getDescripcion(),
                    precioFinal,
                    membresia.getPromos(),
                    membresia.getStatus()
            );
        }).collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponse(membresiaDtos, HttpStatus.OK), HttpStatus.OK);
    }


    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById(Long id) {
        Membresia foundMembresia = membresiaRepo.findById(id).orElse(null);
        if (foundMembresia == null)
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Membresia No Encontrada"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ApiResponse(foundMembresia, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> update(Membresia membresia, Long id) {
        Membresia foundMembresia = membresiaRepo.findById(id).orElse(null);
        if (foundMembresia == null)
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Membresia No Encontrada"), HttpStatus.BAD_REQUEST);
        Membresia existingMembresia = foundMembresia;
        existingMembresia.setNombre(membresia.getNombre());
        existingMembresia.setDescripcion(membresia.getDescripcion());
        existingMembresia.setPrecio(membresia.getPrecio());
        existingMembresia.setPromos(membresia.getPromos());
        return new ResponseEntity<>(new ApiResponse(membresiaRepo.save(existingMembresia), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> save(Membresia membresia) {
        return new ResponseEntity<>(new ApiResponse(membresiaRepo.save(membresia), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> changeStatus(Long id) {
        Membresia foundMembresia = membresiaRepo.findById(id).orElse(null);
        if (foundMembresia == null)
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Membresia No Encontrada"), HttpStatus.BAD_REQUEST);
        Membresia membresia = foundMembresia;
        membresia.setStatus(!membresia.getStatus());
        return new ResponseEntity<>(new ApiResponse(membresiaRepo.save(membresia), HttpStatus.OK), HttpStatus.OK);
    }

}

