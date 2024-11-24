package com.camila.gymkyu.models.usuarios;

import com.camila.gymkyu.models.clases.Clases;
import com.camila.gymkyu.models.roles.Rol;
import com.camila.gymkyu.models.suscripcion.Suscripcion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@NoArgsConstructor
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String nombre;

    @Column(length = 45, nullable = false)
    private String apPaterno;

    @Column(length = 45, nullable = false)
    private String apMaterno;

    @Column(length = 45, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Integer edad;

    @Column(length = 45, nullable = false)
    private String telefono;

    @Column(length = 170, nullable = false)
    private String contrasena;

    @Column(columnDefinition = "LONGBLOB")
    @Lob()
    private String foto;

    @ManyToOne
    @JoinColumn(name = "role")
    @JsonIgnoreProperties(value = {"usuario"})
    private Rol role;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "BOOL DEFAULT true")
    private Boolean status;

    @Column(columnDefinition = "BOOL DEFAULT true")
    private Boolean blocked;

    private String token;

    @JsonIgnoreProperties(value = {"participantes"})
    @ManyToMany(mappedBy = "participantes", cascade = CascadeType.MERGE)
    private Set<Clases> clases;

    @JsonIgnoreProperties(value = {"usuario","promos"})
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<Suscripcion> suscripciones;


    public Usuario(String nombre, String apPaterno, String apMaterno, String email, Integer edad, String telefono, String contrasena, String foto, Rol role, LocalDateTime createdAt, Boolean status) {
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.email = email;
        this.edad = edad;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.foto = foto;
        this.role = role;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Usuario(Long id, String nombre, String apPaterno, String apMaterno, String email, Integer edad, String telefono, String contrasena, String foto, Rol role, LocalDateTime createdAt, Boolean status) {
        this.id = id;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.email = email;
        this.edad = edad;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.foto = foto;
        this.role = role;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Usuario(Long id, String nombre, String apPaterno, String apMaterno, String email, Integer edad, String telefono, String contrasena, String foto, Rol role, LocalDateTime createdAt, Boolean status, Boolean blocked, String token) {
        this.id = id;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.email = email;
        this.edad = edad;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.foto = foto;
        this.role = role;
        this.createdAt = createdAt;
        this.status = status;
        this.blocked = blocked;
        this.token = token;
    }

    public Usuario(String nombre, String apPaterno, String apMaterno, String email, Integer edad, String telefono, String contrasena, String foto, Rol role) {
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.email = email;
        this.edad = edad;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.foto = foto;
        this.role = role;
    }

    public Usuario(String nombre, String apPaterno, String apMaterno, String email, Integer edad, String telefono, String contrasena, String foto, Rol role, Boolean status) {
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.email = email;
        this.edad = edad;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.foto = foto;
        this.role = role;
        this.status = status;
    }

    public Usuario(String nombre, String apPaterno, String apMaterno, String email, Integer edad, String telefono, String contrasena, String foto, Rol role, Boolean status, Boolean blocked) {
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.email = email;
        this.edad = edad;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.foto = foto;
        this.role = role;
        this.status = status;
        this.blocked = blocked;
    }
}
