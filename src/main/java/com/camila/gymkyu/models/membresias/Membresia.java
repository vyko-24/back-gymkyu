package com.camila.gymkyu.models.membresias;

import com.camila.gymkyu.models.promos.Promos;
import com.camila.gymkyu.models.suscripcion.Suscripcion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "membresias")
@Getter
@Setter
@NoArgsConstructor
public class Membresia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String nombre;

    @Column(length = 255, nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @OneToMany(mappedBy = "membresia", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"membresia"})
    private Set<Suscripcion> usuariosSuscritos;

    @ManyToMany
    @JoinTable(
            name = "membresia_promos",
            joinColumns = @JoinColumn(name = "membresia_id"),
            inverseJoinColumns = @JoinColumn(name = "promo_id")
    )
    @JsonIgnoreProperties(value= {"membresia"})
    private List<Promos> promos = new ArrayList<>();

    @Column(columnDefinition = "BOOL DEFAULT true")
    private Boolean status;

    public Membresia(Long id, String nombre, String descripcion, Double precio, List<Promos> promos, Boolean status) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.promos = promos;
        this.status = status;
    }

    public Membresia(String nombre, String descripcion, Double precio, List<Promos> promos, Boolean status) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.promos = promos;
        this.status = status;
    }

    public Membresia(String nombre, String descripcion, Double precio, Boolean status) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.status = status;
    }
}
