package com.autogestion.controller;

import com.autogestion.dto.ClienteRequest;
import com.autogestion.entity.Cliente;
import com.autogestion.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.crear(request));
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
