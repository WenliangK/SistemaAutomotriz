package com.autogestion.controller;

import com.autogestion.dto.PagoRequest;
import com.autogestion.entity.PagoEntrega;
import com.autogestion.service.PagoEntregaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PagoEntregaController {

    private final PagoEntregaService pagoEntregaService;

    @GetMapping("/ordenes-trabajo/{id}/monto")
    public ResponseEntity<Map<String, BigDecimal>> obtenerMonto(@PathVariable Long id) {
        BigDecimal monto = pagoEntregaService.obtenerMonto(id);
        return ResponseEntity.ok(Map.of("monto", monto));
    }

    @PostMapping("/pagos")
    public ResponseEntity<PagoEntrega> registrarPago(@RequestBody PagoRequest request) {
        return ResponseEntity.ok(pagoEntregaService.registrarPago(request));
    }

    @PostMapping("/entregas/{ordenTrabajoId}")
    public ResponseEntity<PagoEntrega> registrarEntrega(@PathVariable Long ordenTrabajoId) {
        return ResponseEntity.ok(pagoEntregaService.registrarEntrega(ordenTrabajoId));
    }

    @GetMapping("/ordenes-trabajo/{id}/pago")
    public ResponseEntity<?> obtenerPago(@PathVariable Long id) {
        Optional<PagoEntrega> pago = pagoEntregaService.obtenerPorOT(id);
        return pago.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
