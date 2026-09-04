package com.autogestion.controller;

import com.autogestion.dto.VehiculoRequest;
import com.autogestion.entity.Vehiculo;
import com.autogestion.service.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PostMapping
    public ResponseEntity<Vehiculo> crear(@RequestBody VehiculoRequest request) {
        return ResponseEntity.ok(vehiculoService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<Vehiculo>> listar() {
        return ResponseEntity.ok(vehiculoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.obtenerPorId(id));
    }
}
