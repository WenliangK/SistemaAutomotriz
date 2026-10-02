package com.autogestion.controller;

import com.autogestion.dto.GastoRequest;
import com.autogestion.dto.GastoResponseDTO;
import com.autogestion.service.GastoService;
import com.autogestion.util.Rangos;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/** Gastos operativos (alquiler, luz, sueldos...). Solo ADMIN los registra. */
@RolesAllowed("ADMIN")
@RestController
@RequestMapping("/api/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService gastoService;

    @PostMapping
    public ResponseEntity<GastoResponseDTO> crear(@Valid @RequestBody GastoRequest request,
                                                  Authentication auth) {
        return ResponseEntity.ok(gastoService.crear(request, Long.valueOf(auth.getName())));
    }

    @GetMapping
    public ResponseEntity<Page<GastoResponseDTO>> listar(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @PageableDefault(size = 20) Pageable pageable) {
        LocalDate d = Rangos.inicioDia(desde);
        LocalDate h = Rangos.finDiaInclusivo(hasta);
        return ResponseEntity.ok(gastoService.listar(categoria, d, h, pageable));
    }
}
