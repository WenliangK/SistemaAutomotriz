package com.autogestion.service;

import com.autogestion.dto.VehiculoRequest;
import com.autogestion.dto.VehiculoResponseDTO;
import com.autogestion.entity.Cliente;
import com.autogestion.entity.Vehiculo;
import com.autogestion.exception.BusinessException;
import com.autogestion.repository.ClienteRepository;
import com.autogestion.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        String placa = request.placaNormalizada();
        if (vehiculoRepository.findByPlaca(placa).isPresent()) {
            throw new BusinessException("Ya existe un vehículo con la placa " + placa + ".", "placa",
                    "Si es de otro dueño, no puedes registrarlo de nuevo.", HttpStatus.CONFLICT);
        }

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new BusinessException("Cliente no encontrado.", "clienteId",
                        null, HttpStatus.NOT_FOUND));

        Vehiculo vehiculo = Vehiculo.builder()
                .cliente(cliente)
                .placa(placa)
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
                .orElseThrow(() -> new BusinessException("Vehículo no encontrado.", null,
                        null, HttpStatus.NOT_FOUND));
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
