package com.camila.gymkyu.controllers.usuarios.dto;

import com.camila.gymkyu.models.roles.Rol;
import com.camila.gymkyu.models.usuarios.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UsuarioDto {
    private Long id;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private String email;
    private Integer edad;
    private String telefono;
    private String contrasena;
    private String foto;
    private Rol role;

    public Usuario toEntity(){ return new Usuario(nombre, apPaterno, apMaterno, email, edad, telefono, contrasena, foto, role);}
}
