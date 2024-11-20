package com.camila.gymkyu.services.roles;

import com.camila.gymkyu.config.ApiResponse;
import com.camila.gymkyu.models.roles.RoleRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {
    private final RoleRepo repository;

    public RoleService(RoleRepo repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll(){
        return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }
}
