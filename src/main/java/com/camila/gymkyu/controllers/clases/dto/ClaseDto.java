package com.camila.gymkyu.controllers.clases.dto;

import com.camila.gymkyu.models.clases.Clases;
import com.camila.gymkyu.models.usuarios.Usuario;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class ClaseDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private String foto;
    private Boolean status;
    private Set<Usuario> participantes;

    public Clases toEntity(){ return new Clases(nombre, descripcion, foto, status, participantes);}
}
