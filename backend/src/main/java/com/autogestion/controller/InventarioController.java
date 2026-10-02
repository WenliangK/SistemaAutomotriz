package com.autogestion.controller;

import com.autogestion.dto.MovimientoInventarioRequest;
import com.autogestion.dto.MovimientoResponseDTO;
import com.autogestion.dto.ProductoRequest;
import com.autogestion.entity.InventarioMovimiento;
import com.autogestion.entity.Producto;
import com.autogestion.service.InventarioService;
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

import java.time.LocalDateTime;
import java.util.List;

@RolesAllowed({"ADMIN", "ALMACENERO", "MECANICO"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    @RolesAllowed({"ADMIN", "ALMACENERO"})
    @PostMapping("/productos")
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(inventarioService.crear(request));
    }

    @RolesAllowed({"ADMIN", "ALMACENERO"})
    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(inventarioService.actualizar(id, request));
    }

    @RolesAllowed({"ADMIN", "ALMACENERO"})
    @PostMapping("/inventario/movimientos")
    public ResponseEntity<MovimientoResponseDTO> registrarMovimiento(
            @Valid @RequestBody MovimientoInventarioRequest request,
            Authentication auth) {
        return ResponseEntity.ok(inventarioService.registrarMovimiento(request, Long.valueOf(auth.getName())));
    }

    @GetMapping("/inventario/alertas")
    public ResponseEntity<List<Producto>> alertasStock() {
        return ResponseEntity.ok(inventarioService.alertasStock());
    }

    @GetMapping("/inventario/movimientos")
    public ResponseEntity<Page<MovimientoResponseDTO>> listarMovimientos(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @PageableDefault(size = 20) Pageable pageable) {
        LocalDateTime d = Rangos.inicio(desde);
        LocalDateTime h = Rangos.fin(hasta);
        return ResponseEntity.ok(inventarioService.listarMovimientos(productoId, tipo, usuarioId, d, h, pageable));
    }

    /** Compatibilidad con pantallas viejas: devuelve entidades (usar el paginado de arriba). */
    @GetMapping("/inventario/movimientos-todos")
    public ResponseEntity<List<InventarioMovimiento>> listarMovimientosTodos(
            @RequestParam(required = false) Long productoId) {
        return ResponseEntity.ok(inventarioService.listarMovimientos(productoId));
    }
}
