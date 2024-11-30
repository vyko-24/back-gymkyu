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
import java.util.ArrayList;
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
        existingPromo.setImagen(promo.getImagen());

        List<Membresia> membresias = new ArrayList<>();
        for (Membresia membresia : promo.getMembresia()) {
            Optional<Membresia> foundMembresia = membresiaRepo.findById(membresia.getId());
            if (foundMembresia.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true,
                        "Membresia no encontrada: " + membresia.getId()), HttpStatus.BAD_REQUEST);
            }
            membresias.add(foundMembresia.get());
        }
        existingPromo.setMembresia(membresias);

        return new ResponseEntity<>(new ApiResponse(promoRepo.save(existingPromo), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<ApiResponse> save(Promos promo){
        // Asegurar que todas las membresías existen
        List<Membresia> membresias = new ArrayList<>();
        for (Membresia membresia : promo.getMembresia()) {
            Optional<Membresia> foundMembresia = membresiaRepo.findById(membresia.getId());
            if (foundMembresia.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true,
                        "Membresia no encontrada: " + membresia.getId()), HttpStatus.BAD_REQUEST);
            }
            membresias.add(foundMembresia.get());
        }

        // Asociar las membresías con la promoción
        promo.setMembresia(membresias);
        promo.setStatus(true);

        // Guardar la promoción
        Promos savedPromo = promoRepo.save(promo);
        savedPromo.setImagen(promo.getImagen());
        savedPromo= promoRepo.save(savedPromo);

        // Actualizar la relación en cada membresía
        for (Membresia membresia : membresias) {
            membresia.getPromos().add(savedPromo);
            membresiaRepo.save(membresia);
        }

        return new ResponseEntity<>(new ApiResponse(savedPromo, HttpStatus.OK), HttpStatus.OK);
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
