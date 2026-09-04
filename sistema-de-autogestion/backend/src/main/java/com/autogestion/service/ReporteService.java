package com.autogestion.service;

import com.autogestion.dto.IndicadoresResponse;
import com.autogestion.entity.OrdenTrabajo;
import com.autogestion.repository.OrdenTrabajoRepository;
import com.autogestion.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final ProductoRepository productoRepository;

    public IndicadoresResponse obtenerIndicadores() {
        
        long otCompletadas = ordenTrabajoRepository.countFinalizadas();

        
        List<OrdenTrabajo> finalizadas = ordenTrabajoRepository.findByEstado("FINALIZADA");
        double tiempoPromedio = finalizadas.stream()
                .filter(ot -> ot.getFechaFin() != null)
                .mapToLong(ot -> Duration.between(ot.getFechaCreacion(), ot.getFechaFin()).toHours())
                .average()
                .orElse(0.0);

        
        double ingresos = finalizadas.stream()
                .mapToDouble(ot -> ot.getCotizacion().getTotal().doubleValue())
                .sum();

        
        long productosBajoStock = productoRepository.findConStockBajo().size();

        return IndicadoresResponse.builder()
                .otCompletadasMes(otCompletadas)
                .tiempoPromedioHoras(Math.round(tiempoPromedio * 10.0) / 10.0)
                .ingresosMes(Math.round(ingresos * 100.0) / 100.0)
                .productosBajoStock(productosBajoStock)
                .build();
    }
}
