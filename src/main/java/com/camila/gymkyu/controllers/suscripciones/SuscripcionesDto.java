package com.camila.gymkyu.controllers.suscripciones;


import com.camila.gymkyu.models.membresias.Membresia;
import com.camila.gymkyu.models.suscripcion.Suscripcion;
import com.camila.gymkyu.models.usuarios.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class SuscripcionesDto {
    private Long id;
    private Usuario usuario;
    private Membresia membresia;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Boolean status;
    private Double precio;

    public SuscripcionesDto(LocalDateTime fechaInicio, Double precio) {
        this.fechaInicio = fechaInicio;
        this.precio = precio;
    }

    public Suscripcion toEntity(){return new Suscripcion(usuario, membresia, fechaInicio, fechaFin, status, precio);}
}
