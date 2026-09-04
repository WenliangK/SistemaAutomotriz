package com.autogestion.service;

import com.autogestion.dto.ClienteRequest;
import com.autogestion.entity.Cliente;
import com.autogestion.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Cliente crear(ClienteRequest request) {
        Cliente cliente = Cliente.builder()
                .nombre(request.getNombre())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .documento(request.getDocumento())
                .build();
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }
}
