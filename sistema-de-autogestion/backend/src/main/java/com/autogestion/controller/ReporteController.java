package com.autogestion.controller;

import com.autogestion.dto.IndicadoresResponse;
import com.autogestion.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/indicadores")
    public ResponseEntity<IndicadoresResponse> obtenerIndicadores() {
        return ResponseEntity.ok(reporteService.obtenerIndicadores());
    }
}
