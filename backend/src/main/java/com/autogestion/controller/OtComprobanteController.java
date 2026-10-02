package com.autogestion.controller;

import com.autogestion.dto.ComprobanteResponseDTO;
import com.autogestion.service.ComprobanteService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Comprobante vigente de una OT (para "Ver comprobante" y reimprimir). */
@RolesAllowed({"ADMIN", "RECEPCIONISTA"})
@RestController
@RequestMapping("/api/ordenes-trabajo")
@RequiredArgsConstructor
public class OtComprobanteController {

    private final ComprobanteService comprobanteService;

    @GetMapping("/{id}/comprobante")
    public ResponseEntity<ComprobanteResponseDTO> vigente(@PathVariable Long id) {
        return comprobanteService.vigentePorOT(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
