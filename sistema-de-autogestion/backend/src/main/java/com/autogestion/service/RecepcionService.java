package com.autogestion.service;

import com.autogestion.dto.RecepcionRequest;
import com.autogestion.entity.Recepcion;
import com.autogestion.entity.Vehiculo;
import com.autogestion.repository.RecepcionRepository;
import com.autogestion.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecepcionService {

    private final RecepcionRepository recepcionRepository;
    private final VehiculoRepository vehiculoRepository;

    public Recepcion crear(RecepcionRequest request) {
        Vehiculo vehiculo = vehiculoRepository.findById(request.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        Recepcion recepcion = Recepcion.builder()
                .vehiculo(vehiculo)
                .problemaReportado(request.getProblemaReportado())
                .fechaIngreso(LocalDateTime.now())
                .estado("PENDIENTE")
                .build();
        return recepcionRepository.save(recepcion);
    }

    public List<Recepcion> listar(String estado) {
        if (estado != null && !estado.isEmpty()) {
            return recepcionRepository.findByEstado(estado);
        }
        return recepcionRepository.findAll();
    }

    public Recepcion obtenerPorId(Long id) {
        return recepcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada"));
    }
}
