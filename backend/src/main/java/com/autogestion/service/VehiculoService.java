package com.autogestion.service;

import com.autogestion.dto.VehiculoRequest;
import com.autogestion.entity.Cliente;
import com.autogestion.entity.Vehiculo;
import com.autogestion.repository.ClienteRepository;
import com.autogestion.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;

    public Vehiculo crear(VehiculoRequest request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Vehiculo vehiculo = Vehiculo.builder()
                .cliente(cliente)
                .placa(request.getPlaca())
                .marca(request.getMarca())
                .modelo(request.getModelo())
                .anio(request.getAnio())
                .build();
        return vehiculoRepository.save(vehiculo);
    }

    public List<Vehiculo> listar() {
        return vehiculoRepository.findAll();
    }

    public Vehiculo obtenerPorId(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
    }
}
