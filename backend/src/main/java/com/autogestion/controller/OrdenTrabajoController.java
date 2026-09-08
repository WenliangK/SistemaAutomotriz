package com.autogestion.controller;

import com.autogestion.dto.OrdenTrabajoRequest;
import com.autogestion.dto.ProductoUsadoRequest;
import com.autogestion.entity.OrdenTrabajo;
import com.autogestion.entity.OtProductoUsado;
import com.autogestion.service.OrdenTrabajoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordenes-trabajo")
@RequiredArgsConstructor
public class OrdenTrabajoController {

    private final OrdenTrabajoService ordenTrabajoService;

    @PostMapping
    public ResponseEntity<OrdenTrabajo> crear(@RequestBody OrdenTrabajoRequest request) {
        return ResponseEntity.ok(ordenTrabajoService.crear(request));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<OrdenTrabajo> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ordenTrabajoService.cambiarEstado(id, body.get("estado")));
    }

    @PostMapping("/{id}/productos-usados")
    public ResponseEntity<OtProductoUsado> registrarProductoUsado(
            @PathVariable Long id,
            @RequestBody ProductoUsadoRequest request) {
        return ResponseEntity.ok(ordenTrabajoService.registrarProductoUsado(id, request));
    }

    @GetMapping
    public ResponseEntity<List<OrdenTrabajo>> listar(
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(ordenTrabajoService.listar(estado));
    }

    @GetMapping("/mecanico/{mecanicoId}")
    public ResponseEntity<List<OrdenTrabajo>> listarPorMecanico(@PathVariable Long mecanicoId) {
        return ResponseEntity.ok(ordenTrabajoService.listarPorMecanico(mecanicoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenTrabajo> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ordenTrabajoService.obtenerPorId(id));
    }

    @GetMapping("/{id}/productos-usados")
    public ResponseEntity<List<OtProductoUsado>> listarProductosUsados(@PathVariable Long id) {
        return ResponseEntity.ok(ordenTrabajoService.listarProductosUsados(id));
    }
}
