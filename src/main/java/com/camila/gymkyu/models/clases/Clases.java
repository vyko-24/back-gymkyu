package com.camila.gymkyu.models.clases;

import com.camila.gymkyu.models.usuarios.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clases")
@Getter
@Setter
@NoArgsConstructor
public class Clases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(columnDefinition = "LONGBLOB")
    @Lob()
    private String foto;

    @Column(columnDefinition = "BOOL DEFAULT true")
    private Boolean status;

    /*@ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnoreProperties(value = {"usuario"})
    private Set<Role> roles;*/
    @ManyToMany
    @JoinTable(
            name = "clase_usuarios",
            joinColumns = @JoinColumn(name = "clase_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonIgnoreProperties(value = {"clases","suscripciones","contrasena","foto","role"})
    private Set<Usuario> participantes = new HashSet<>();

    public Clases(String nombre, String descripcion, String foto, Boolean status, Set<Usuario> participantes) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.status = status;
        this.participantes = participantes;
    }

    public Clases(Long id, String nombre, String descripcion, String foto, Boolean status, Set<Usuario> participantes) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.status = status;
        this.participantes = participantes;
    }

    public Clases(String nombre, String descripcion, String foto, Boolean status) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.status = status;
    }
}
