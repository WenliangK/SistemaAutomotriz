package com.autogestion.controller;

import com.autogestion.dto.MovimientoInventarioRequest;
import com.autogestion.dto.ProductoRequest;
import com.autogestion.entity.InventarioMovimiento;
import com.autogestion.entity.Producto;
import com.autogestion.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    @PostMapping("/productos")
    public ResponseEntity<Producto> crearProducto(@RequestBody ProductoRequest request) {
        return ResponseEntity.ok(inventarioService.crear(request));
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(inventarioService.actualizar(id, request));
    }

    @PostMapping("/inventario/movimientos")
    public ResponseEntity<InventarioMovimiento> registrarMovimiento(
            @RequestBody MovimientoInventarioRequest request) {
        return ResponseEntity.ok(inventarioService.registrarMovimiento(request));
    }

    @GetMapping("/inventario/alertas")
    public ResponseEntity<List<Producto>> alertasStock() {
        return ResponseEntity.ok(inventarioService.alertasStock());
    }

    @GetMapping("/inventario/movimientos")
    public ResponseEntity<List<InventarioMovimiento>> listarMovimientos(
            @RequestParam(required = false) Long productoId) {
        return ResponseEntity.ok(inventarioService.listarMovimientos(productoId));
    }
}
