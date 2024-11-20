package com.camila.gymkyu.controllers.promos.dto;

import com.camila.gymkyu.models.promos.Promos;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class PromoDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private Integer porcentaje;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Boolean status;

    public Promos toEntity(){
        return new Promos(nombre, descripcion, imagen, porcentaje, fechaInicio, fechaFin, status);
    }
}
