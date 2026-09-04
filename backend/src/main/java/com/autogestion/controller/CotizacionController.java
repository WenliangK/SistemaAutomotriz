package com.autogestion.controller;

import com.autogestion.dto.CotizacionRequest;
import com.autogestion.entity.Cotizacion;
import com.autogestion.entity.CotizacionProducto;
import com.autogestion.entity.CotizacionServicio;
import com.autogestion.service.CotizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cotizaciones")
@RequiredArgsConstructor
public class CotizacionController {

    private final CotizacionService cotizacionService;

    @PostMapping
    public ResponseEntity<Cotizacion> crear(@RequestBody CotizacionRequest request) {
        return ResponseEntity.ok(cotizacionService.crear(request));
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<Cotizacion> aprobar(@PathVariable Long id) {
        return ResponseEntity.ok(cotizacionService.aprobar(id));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<Cotizacion> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(cotizacionService.rechazar(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cotizacion> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(cotizacionService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Cotizacion>> listar(
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(cotizacionService.listar(estado));
    }

    @GetMapping("/{id}/servicios")
    public ResponseEntity<List<CotizacionServicio>> listarServicios(@PathVariable Long id) {
        return ResponseEntity.ok(cotizacionService.listarServicios(id));
    }

    @GetMapping("/{id}/productos")
    public ResponseEntity<List<CotizacionProducto>> listarProductos(@PathVariable Long id) {
        return ResponseEntity.ok(cotizacionService.listarProductos(id));
    }
}
