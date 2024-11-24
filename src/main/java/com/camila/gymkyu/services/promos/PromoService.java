package com.camila.gymkyu.services.promos;

import com.camila.gymkyu.config.ApiResponse;
import com.camila.gymkyu.models.membresias.Membresia;
import com.camila.gymkyu.models.membresias.MembresiaRepo;
import com.camila.gymkyu.models.promos.PromoRepo;
import com.camila.gymkyu.models.promos.Promos;
import com.camila.gymkyu.models.suscripcion.Suscripcion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PromoService {
    private final PromoRepo promoRepo;
    private final MembresiaRepo membresiaRepo;
    public PromoService(PromoRepo promoRepo, MembresiaRepo membresiaRepo) {
        this.promoRepo = promoRepo;
        this.membresiaRepo = membresiaRepo;
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
        Optional<Membresia> foundMembresia = membresiaRepo.findById(promo.getMembresia().get(0).getId());
        promo.setStatus(true);
        if (foundMembresia.isEmpty())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "Membresia No Encontrada"), HttpStatus.BAD_REQUEST);
        foundMembresia.get().getPromos().add(promo);
        membresiaRepo.saveAndFlush(foundMembresia.get());
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


    @Scheduled(cron = "0 0 0 * * ?") // Ejecuta cada día a medianoche
    @Transactional
    public void desactivarSuscripcionesVencidas() {
        List<Promos> promosVencidas = promoRepo.findByFechaFin(LocalDateTime.now());
        for (Promos promos : promosVencidas) {
            promos.setStatus(false);
            promoRepo.saveAndFlush(promos);
        }
        System.out.println("Suscripciones vencidas desactivadas");
    }
}
