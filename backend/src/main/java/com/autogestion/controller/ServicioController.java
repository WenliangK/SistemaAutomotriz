package com.autogestion.controller;

import com.autogestion.entity.Servicio;
import com.autogestion.repository.ServicioRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RolesAllowed({"ADMIN", "RECEPCIONISTA", "MECANICO", "ALMACENERO"})
@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioRepository servicioRepository;

    @GetMapping
    public ResponseEntity<List<Servicio>> listar() {
        return ResponseEntity.ok(servicioRepository.findAll());
    }
}