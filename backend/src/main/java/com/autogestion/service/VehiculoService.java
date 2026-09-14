package com.autogestion.service;

import com.autogestion.dto.VehiculoRequest;
import com.autogestion.dto.VehiculoResponseDTO;
import com.autogestion.entity.Cliente;
import com.autogestion.entity.Vehiculo;
import com.autogestion.repository.ClienteRepository;
import com.autogestion.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public VehiculoResponseDTO crear(VehiculoRequest request) {
        if (vehiculoRepository.findByPlaca(request.getPlaca()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un vehículo registrado con la placa " + request.getPlaca());
        }

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        Vehiculo vehiculo = Vehiculo.builder()
                .cliente(cliente)
                .placa(request.getPlaca())
                .marca(request.getMarca())
                .modelo(request.getModelo())
                .anio(request.getAnio())
                .build();
        Vehiculo saved = vehiculoRepository.save(vehiculo);
        return VehiculoResponseDTO.builder()
                .id(saved.getId())
                .placa(saved.getPlaca())
                .marca(saved.getMarca())
                .modelo(saved.getModelo())
                .anio(saved.getAnio())
                .clienteId(saved.getCliente().getId())
                .clienteNombre(saved.getCliente().getNombre())
                .build();
    }

    @Transactional(readOnly = true)
    public List<VehiculoResponseDTO> listar() {
        return vehiculoRepository.findAll().stream()
                .map(v -> VehiculoResponseDTO.builder()
                        .id(v.getId())
                        .placa(v.getPlaca())
                        .marca(v.getMarca())
                        .modelo(v.getModelo())
                        .anio(v.getAnio())
                        .clienteId(v.getCliente().getId())
                        .clienteNombre(v.getCliente().getNombre())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public VehiculoResponseDTO obtenerPorId(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));
        return VehiculoResponseDTO.builder()
                .id(vehiculo.getId())
                .placa(vehiculo.getPlaca())
                .marca(vehiculo.getMarca())
                .modelo(vehiculo.getModelo())
                .anio(vehiculo.getAnio())
                .clienteId(vehiculo.getCliente().getId())
                .clienteNombre(vehiculo.getCliente().getNombre())
                .build();
    }
}
