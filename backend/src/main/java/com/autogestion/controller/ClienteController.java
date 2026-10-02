package com.autogestion.controller;

import com.autogestion.dto.ClienteDetalleDTO;
import com.autogestion.dto.ClienteRequest;
import com.autogestion.entity.Cliente;
import com.autogestion.entity.Vehiculo;
import com.autogestion.service.ClienteService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RolesAllowed({"ADMIN", "RECEPCIONISTA", "MECANICO"})
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> crear(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id,
                                              @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.actualizar(id, request));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<Cliente>> buscar(@RequestParam(required = false) String q,
                                                @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(clienteService.buscar(q, pageable));
    }

    @GetMapping("/{id}/vehiculos")
    public ResponseEntity<List<Vehiculo>> vehiculos(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.vehiculosDe(id));
    }

    @GetMapping("/{id}/resumen")
    public ResponseEntity<ClienteDetalleDTO> resumen(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.resumen(id));
    }

    @PatchMapping("/{id}/activo")
    public ResponseEntity<Cliente> cambiarActivo(@PathVariable Long id,
                                                 @RequestBody java.util.Map<String, Boolean> body) {
        return ResponseEntity.ok(clienteService.cambiarActivo(id, Boolean.TRUE.equals(body.get("activo"))));
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }
}
