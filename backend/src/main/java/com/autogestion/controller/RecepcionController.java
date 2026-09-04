package com.autogestion.controller;

import com.autogestion.dto.RecepcionRequest;
import com.autogestion.entity.Recepcion;
import com.autogestion.service.RecepcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recepciones")
@RequiredArgsConstructor
public class RecepcionController {

    private final RecepcionService recepcionService;

    @PostMapping
    public ResponseEntity<Recepcion> crear(@RequestBody RecepcionRequest request) {
        return ResponseEntity.ok(recepcionService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<Recepcion>> listar(
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(recepcionService.listar(estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recepcion> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(recepcionService.obtenerPorId(id));
    }
}
