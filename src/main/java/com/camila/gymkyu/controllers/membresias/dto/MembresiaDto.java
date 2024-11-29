package com.camila.gymkyu.controllers.membresias.dto;

import com.camila.gymkyu.models.membresias.Membresia;
import com.camila.gymkyu.models.promos.Promos;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MembresiaDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private List<Promos> promos;
    private Boolean status;
    private Double precioOriginal;

    public MembresiaDto(Long id, String nombre, String descripcion, Double precio, Double precioOriginal, List<Promos> promos, Boolean status) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.precioOriginal = precioOriginal;
        this.promos = promos;
        this.status = status;
    }

    public MembresiaDto(String nombre, String descripcion, Double precio, List<Promos> promos, Boolean status) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.promos = promos;
        this.status = status;
    }

    public Membresia toEntity(){
        return new Membresia(nombre, descripcion, precio, promos, status);
    }
}
