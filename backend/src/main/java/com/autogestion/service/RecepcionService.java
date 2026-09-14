package com.autogestion.service;

import com.autogestion.dto.RecepcionCompletaRequest;
import com.autogestion.dto.RecepcionRequest;
import com.autogestion.dto.RecepcionResponseDTO;
import com.autogestion.entity.Cliente;
import com.autogestion.entity.Recepcion;
import com.autogestion.entity.Vehiculo;
import com.autogestion.repository.ClienteRepository;
import com.autogestion.repository.RecepcionRepository;
import com.autogestion.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecepcionService {

    private final RecepcionRepository recepcionRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public RecepcionResponseDTO crear(RecepcionRequest request) {
        try {
            Vehiculo vehiculo = vehiculoRepository.findById(request.getVehiculoId())
                    .orElseThrow(() -> new RuntimeException("Vehículo no encontrado: " + request.getVehiculoId()));

            Recepcion recepcion = Recepcion.builder()
                    .vehiculo(vehiculo)
                    .problemaReportado(request.getProblemaReportado())
                    .fechaIngreso(LocalDateTime.now())
                    .estado("PENDIENTE")
                    .build();
            Recepcion saved = recepcionRepository.save(recepcion);
            return RecepcionResponseDTO.builder()
                    .id(saved.getId())
                    .problemaReportado(saved.getProblemaReportado())
                    .fechaIngreso(saved.getFechaIngreso())
                    .estado(saved.getEstado())
                    .vehiculoId(saved.getVehiculo().getId())
                    .vehiculoPlaca(saved.getVehiculo().getPlaca())
                    .clienteId(saved.getVehiculo().getCliente().getId())
                    .clienteNombre(saved.getVehiculo().getCliente().getNombre())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error al crear recepción: " + e.getMessage(), e);
        }
    }

    @Transactional
    public RecepcionResponseDTO crearCompleto(RecepcionCompletaRequest request) {
        Cliente cliente = findOrCreateCliente(request);

        Vehiculo vehiculo = findOrCreateVehiculo(cliente, request);

        Recepcion recepcion = Recepcion.builder()
                .vehiculo(vehiculo)
                .problemaReportado(request.getProblemaReportado())
                .fechaIngreso(LocalDateTime.now())
                .estado("PENDIENTE")
                .build();
        Recepcion saved = recepcionRepository.save(recepcion);
        return RecepcionResponseDTO.builder()
                .id(saved.getId())
                .problemaReportado(saved.getProblemaReportado())
                .fechaIngreso(saved.getFechaIngreso())
                .estado(saved.getEstado())
                .vehiculoId(saved.getVehiculo().getId())
                .vehiculoPlaca(saved.getVehiculo().getPlaca())
                .clienteId(saved.getVehiculo().getCliente().getId())
                .clienteNombre(saved.getVehiculo().getCliente().getNombre())
                .build();
    }

    private Cliente findOrCreateCliente(RecepcionCompletaRequest request) {
        if (request.getClienteDocumento() != null && !request.getClienteDocumento().isBlank()) {
            Optional<Cliente> existing = clienteRepository.findByDocumento(request.getClienteDocumento());
            if (existing.isPresent()) {
                return existing.get();
            }
        }
        if (request.getClienteEmail() != null && !request.getClienteEmail().isBlank()) {
            Optional<Cliente> existing = clienteRepository.findByEmail(request.getClienteEmail());
            if (existing.isPresent()) {
                return existing.get();
            }
        }
        if (request.getClienteTelefono() != null && !request.getClienteTelefono().isBlank()) {
            Optional<Cliente> existing = clienteRepository.findByTelefono(request.getClienteTelefono());
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        Cliente cliente = Cliente.builder()
                .nombre(request.getClienteNombre())
                .telefono(request.getClienteTelefono())
                .email(request.getClienteEmail())
                .documento(request.getClienteDocumento())
                .build();
        return clienteRepository.save(cliente);
    }

    private Vehiculo findOrCreateVehiculo(Cliente cliente, RecepcionCompletaRequest request) {
        if (request.getVehiculoPlaca() == null || request.getVehiculoPlaca().isBlank()) {
            throw new RuntimeException("Vehículo placa es obligatoria");
        }
        String placa = request.getVehiculoPlaca().toUpperCase().trim();
        Optional<Vehiculo> existing = vehiculoRepository.findByPlaca(placa);
        if (existing.isPresent()) {
            return existing.get();
        }

        Vehiculo vehiculo = Vehiculo.builder()
                .cliente(cliente)
                .placa(placa)
                .marca(request.getVehiculoMarca())
                .modelo(request.getVehiculoModelo())
                .anio(request.getVehiculoAnio())
                .build();
        return vehiculoRepository.save(vehiculo);
    }

    @Transactional(readOnly = true)
    public List<RecepcionResponseDTO> listar(String estado) {
        List<Recepcion> recepciones = (estado != null && !estado.isEmpty())
                ? recepcionRepository.findByEstado(estado)
                : recepcionRepository.findAll();
        return recepciones.stream().map(r -> RecepcionResponseDTO.builder()
                .id(r.getId())
                .problemaReportado(r.getProblemaReportado())
                .fechaIngreso(r.getFechaIngreso())
                .estado(r.getEstado())
                .vehiculoId(r.getVehiculo().getId())
                .vehiculoPlaca(r.getVehiculo().getPlaca())
                .clienteId(r.getVehiculo().getCliente().getId())
                .clienteNombre(r.getVehiculo().getCliente().getNombre())
                .build()).toList();
    }

    @Transactional(readOnly = true)
    public RecepcionResponseDTO obtenerPorId(Long id) {
        Recepcion recepcion = recepcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada"));
        return RecepcionResponseDTO.builder()
                .id(recepcion.getId())
                .problemaReportado(recepcion.getProblemaReportado())
                .fechaIngreso(recepcion.getFechaIngreso())
                .estado(recepcion.getEstado())
                .vehiculoId(recepcion.getVehiculo().getId())
                .vehiculoPlaca(recepcion.getVehiculo().getPlaca())
                .clienteId(recepcion.getVehiculo().getCliente().getId())
                .clienteNombre(recepcion.getVehiculo().getCliente().getNombre())
                .build();
    }
}
