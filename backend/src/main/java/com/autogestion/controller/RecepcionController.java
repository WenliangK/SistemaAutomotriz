package com.autogestion.controller;

import com.autogestion.dto.RecepcionCompletaRequest;
import com.autogestion.dto.RecepcionRequest;
import com.autogestion.dto.RecepcionResponseDTO;
import com.autogestion.service.RecepcionService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recepciones")
@RequiredArgsConstructor
public class RecepcionController {

    private final RecepcionService recepcionService;

    @PermitAll
    @PostMapping
    public ResponseEntity<RecepcionResponseDTO> crear(@RequestBody RecepcionRequest request) {
        return ResponseEntity.ok(recepcionService.crear(request));
    }

    @PermitAll
    @PostMapping("/completa")
    public ResponseEntity<RecepcionResponseDTO> crearCompleta(@RequestBody RecepcionCompletaRequest request) {
        return ResponseEntity.ok(recepcionService.crearCompleto(request));
    }

    @PermitAll
    @GetMapping
    public ResponseEntity<List<RecepcionResponseDTO>> listar(
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(recepcionService.listar(estado));
    }

    @PermitAll
    @GetMapping("/{id}")
    public ResponseEntity<RecepcionResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(recepcionService.obtenerPorId(id));
    }
}
