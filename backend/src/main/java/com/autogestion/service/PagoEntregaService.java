package com.autogestion.service;

import com.autogestion.dto.PagoEntregaResponseDTO;
import com.autogestion.dto.PagoRequest;
import com.autogestion.entity.OrdenTrabajo;
import com.autogestion.entity.PagoEntrega;
import com.autogestion.repository.OrdenTrabajoRepository;
import com.autogestion.repository.PagoEntregaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagoEntregaService {

    private final PagoEntregaRepository pagoEntregaRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;

    @Transactional
    public PagoEntregaResponseDTO registrarPago(PagoRequest request) {
        OrdenTrabajo ot = ordenTrabajoRepository.findById(request.getOrdenTrabajoId())
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada"));

        Optional<PagoEntrega> existente = pagoEntregaRepository.findByOrdenTrabajoId(ot.getId());
        if (existente.isPresent()) {
            throw new RuntimeException("Ya existe un registro de pago para esta OT");
        }

        PagoEntrega pago = PagoEntrega.builder()
                .ordenTrabajo(ot)
                .monto(BigDecimal.valueOf(request.getMonto()))
                .fechaPago(LocalDateTime.now())
                .build();
        pago = pagoEntregaRepository.save(pago);
        return toResponseDTO(pago);
    }

    @Transactional
    public PagoEntregaResponseDTO registrarEntrega(Long ordenTrabajoId) {
        OrdenTrabajo ot = ordenTrabajoRepository.findById(ordenTrabajoId)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada"));

        PagoEntrega pago = pagoEntregaRepository.findByOrdenTrabajoId(ot.getId())
                .orElseThrow(() -> new RuntimeException("Debe registrar pago antes de entrega"));

        if (pago.getFechaEntrega() != null) {
            throw new RuntimeException("Esta OT ya fue entregada");
        }

        pago.setFechaEntrega(LocalDateTime.now());

        var recepcion = ot.getCotizacion().getDiagnostico().getRecepcion();
        recepcion.setEstado("ENTREGADA");

        pago = pagoEntregaRepository.save(pago);
        return toResponseDTO(pago);
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerMonto(Long ordenTrabajoId) {
        OrdenTrabajo ot = ordenTrabajoRepository.findById(ordenTrabajoId)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada"));
        return ot.getCotizacion().getTotal();
    }

    @Transactional(readOnly = true)
    public Optional<PagoEntregaResponseDTO> obtenerPorOT(Long ordenTrabajoId) {
        return pagoEntregaRepository.findByOrdenTrabajoId(ordenTrabajoId)
                .map(this::toResponseDTO);
    }

    public PagoEntregaResponseDTO toResponseDTO(PagoEntrega pago) {
        return PagoEntregaResponseDTO.builder()
                .id(pago.getId())
                .ordenTrabajoId(pago.getOrdenTrabajo().getId())
                .monto(pago.getMonto())
                .fechaPago(pago.getFechaPago())
                .fechaEntrega(pago.getFechaEntrega())
                .build();
    }
}