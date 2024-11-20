package com.camila.gymkyu.controllers.auth.dto;

import com.camila.gymkyu.models.roles.Rol;
import com.camila.gymkyu.models.usuarios.Usuario;
import lombok.Value;

import javax.management.relation.Role;
import java.util.List;

@Value
public class SignedDto {
    String token;
    String tokenType;
    Usuario user;
    Rol role;
}
