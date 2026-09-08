package com.autogestion.controller;

import com.autogestion.dto.DiagnosticoRequest;
import com.autogestion.entity.Diagnostico;
import com.autogestion.service.DiagnosticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnosticos")
@RequiredArgsConstructor
public class DiagnosticoController {

    private final DiagnosticoService diagnosticoService;

    @PostMapping
    public ResponseEntity<Diagnostico> crear(@RequestBody DiagnosticoRequest request) {
        return ResponseEntity.ok(diagnosticoService.crear(request));
    }

    @GetMapping("/recepcion/{recepcionId}")
    public ResponseEntity<List<Diagnostico>> listarPorRecepcion(@PathVariable Long recepcionId) {
        return ResponseEntity.ok(diagnosticoService.listarPorRecepcion(recepcionId));
    }
}
