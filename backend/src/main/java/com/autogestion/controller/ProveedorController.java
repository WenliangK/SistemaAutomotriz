package com.autogestion.controller;

import com.autogestion.dto.ProveedorRequest;
import com.autogestion.dto.ProveedorResponseDTO;
import com.autogestion.entity.Proveedor;
import com.autogestion.service.ProveedorService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RolesAllowed({"ADMIN", "ALMACENERO"})
@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<Proveedor>> listar() {
        return ResponseEntity.ok(proveedorService.listar());
    }

    @PostMapping
    public ResponseEntity<Proveedor> crear(@Valid @RequestBody ProveedorRequest request) {
        return ResponseEntity.ok(proveedorService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id,
                                                 @Valid @RequestBody ProveedorRequest request) {
        return ResponseEntity.ok(proveedorService.actualizar(id, request));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ProveedorResponseDTO>> listarActivos() {
        return ResponseEntity.ok(proveedorService.listar().stream()
                .map(p -> ProveedorResponseDTO.builder()
                        .id(p.getId())
                        .nombre(p.getNombre())
                        .ruc(p.getRuc())
                        .telefono(p.getTelefono())
                        .email(p.getEmail())
                        .activo(p.getActivo())
                        .creadoEn(p.getCreadoEn())
                        .build())
                .toList());
    }
}