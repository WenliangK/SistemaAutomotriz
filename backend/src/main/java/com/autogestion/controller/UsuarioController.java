package com.autogestion.controller;

import com.autogestion.dto.MecanicoResumenDTO;
import com.autogestion.dto.UsuarioRequest;
import com.autogestion.dto.UsuarioResponseDTO;
import com.autogestion.service.UsuarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Equipo de trabajo. Todo es ADMIN, salvo la lista corta de mecánicos
 * (para los selects de cotización y OT) y el perfil propio.
 */
@RolesAllowed("ADMIN")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> listar(
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listar(rol, activo, q, pageable));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }

    @PatchMapping("/{id}/activo")
    public ResponseEntity<UsuarioResponseDTO> cambiarActivo(@PathVariable Long id,
                                                            @RequestBody Map<String, Boolean> body,
                                                            Authentication auth) {
        boolean activo = Boolean.TRUE.equals(body.get("activo"));
        return ResponseEntity.ok(usuarioService.cambiarActivo(id, activo, Long.valueOf(auth.getName())));
    }

    @PostMapping("/{id}/restablecer-clave")
    public ResponseEntity<Map<String, String>> restablecerClave(@PathVariable Long id,
                                                                @RequestBody Map<String, String> body) {
        usuarioService.restablecerClave(id, body.get("nuevaClave"));
        return ResponseEntity.ok(Map.of("mensaje", "Clave actualizada."));
    }

    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    @GetMapping("/mecanicos")
    public ResponseEntity<List<MecanicoResumenDTO>> mecanicos(
            @RequestParam(defaultValue = "true") boolean soloActivos) {
        return ResponseEntity.ok(usuarioService.mecanicos(soloActivos));
    }

    @RolesAllowed({"ADMIN", "MECANICO", "ALMACENERO", "RECEPCIONISTA"})
    @GetMapping("/yo")
    public ResponseEntity<UsuarioResponseDTO> yo(Authentication auth) {
        return ResponseEntity.ok(usuarioService.yo(Long.valueOf(auth.getName())));
    }
}
