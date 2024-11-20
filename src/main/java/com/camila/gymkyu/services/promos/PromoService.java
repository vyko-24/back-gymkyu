package com.camila.gymkyu.services.promos;

import com.camila.gymkyu.config.ApiResponse;
import com.camila.gymkyu.models.promos.PromoRepo;
import com.camila.gymkyu.models.promos.Promos;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PromoService {
    private final PromoRepo promoRepo;

    public PromoService(PromoRepo promoRepo) {
        this.promoRepo = promoRepo;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll(){
        return new ResponseEntity<>(new ApiResponse(promoRepo.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById(Long id){
        Optional<Promos> foundPromo = promoRepo.findById(id);
        if(foundPromo.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Promo No Encontrado"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ApiResponse(foundPromo.get(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> update(Promos promo, Long id){
        Optional<Promos> foundPromo = promoRepo.findById(id);
        if (foundPromo.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Promo No Encontrado"), HttpStatus.BAD_REQUEST);
        Promos existingPromo = foundPromo.get();
        existingPromo.setNombre(promo.getNombre());
        existingPromo.setDescripcion(promo.getDescripcion());
        existingPromo.setPorcentaje(promo.getPorcentaje());
        existingPromo.setFechaInicio(promo.getFechaInicio());
        existingPromo.setFechaFin(promo.getFechaFin());
        return new ResponseEntity<>(new ApiResponse(promoRepo.save(existingPromo), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> save(Promos promo){
        return new ResponseEntity<>(new ApiResponse(promoRepo.save(promo), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> changeStatus(Long id){
        Optional<Promos> foundPromo = promoRepo.findById(id);
        if (foundPromo.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Promo No Encontrado"), HttpStatus.BAD_REQUEST);
        Promos promo = foundPromo.get();
        promo.setStatus(!promo.getStatus());
        return new ResponseEntity<>(new ApiResponse(promoRepo.save(promo), HttpStatus.OK), HttpStatus.OK);
    }
}
