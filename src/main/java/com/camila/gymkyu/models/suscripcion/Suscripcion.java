package com.camila.gymkyu.models.suscripcion;

import com.camila.gymkyu.models.membresias.Membresia;
import com.camila.gymkyu.models.usuarios.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "suscripcion")
@Getter
@Setter
@NoArgsConstructor
public class Suscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties(value = {"suscripcion"})
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "membresia_id", nullable = false)
    @JsonIgnoreProperties(value = {"usuariosSuscritos"})
    private Membresia membresia;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()", insertable = false)
    private LocalDateTime fechaInicio;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime fechaFin;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean status;

    public Suscripcion(Long id, Usuario usuario, Membresia membresia, LocalDateTime fechaInicio, LocalDateTime fechaFin, Boolean status) {
        this.id = id;
        this.usuario = usuario;
        this.membresia = membresia;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.status = status;
    }

    public Suscripcion(Usuario usuario, Membresia membresia, LocalDateTime fechaInicio, LocalDateTime fechaFin, Boolean status) {
        this.usuario = usuario;
        this.membresia = membresia;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.status = status;
    }

    public Suscripcion(Long id, Usuario usuario, Membresia membresia, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.id = id;
        this.usuario = usuario;
        this.membresia = membresia;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Suscripcion(Usuario usuario, Membresia membresia, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.usuario = usuario;
        this.membresia = membresia;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
}
