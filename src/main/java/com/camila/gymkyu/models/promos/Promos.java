package com.camila.gymkyu.models.promos;

import com.camila.gymkyu.models.membresias.Membresia;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "promos")
@Getter
@Setter
@NoArgsConstructor
public class Promos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(columnDefinition = "LONGBLOB")
    @Lob()
    private String imagen;

    @Column(nullable = false)
    private Integer porcentaje;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaInicio;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaFin;

    @Column(columnDefinition = "BOOL DEFAULT true")
    private Boolean status;

    @JsonIgnoreProperties(value = {"promos", "usuariosSuscritos"})
    @ManyToMany(mappedBy = "promos", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Membresia> membresia;

    public Promos(Long id, String nombre, String descripcion, String imagen, Integer porcentaje, LocalDateTime fechaInicio, LocalDateTime fechaFin, Boolean status, List<Membresia> membresia) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.porcentaje = porcentaje;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.status = status;
        this.membresia = membresia;
    }

    public Promos(String nombre, String descripcion, String imagen, Integer porcentaje, LocalDateTime fechaInicio, LocalDateTime fechaFin, Boolean status, List<Membresia> membresia) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.porcentaje = porcentaje;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.status = status;
        this.membresia = membresia;
    }

    public Promos(String nombre, String descripcion, String imagen, Integer porcentaje, LocalDateTime fechaInicio, LocalDateTime fechaFin, Boolean status) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.porcentaje = porcentaje;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.status = status;
    }
}
