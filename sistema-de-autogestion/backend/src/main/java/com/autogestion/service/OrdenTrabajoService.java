package com.autogestion.service;

import com.autogestion.dto.OrdenTrabajoRequest;
import com.autogestion.dto.ProductoUsadoRequest;
import com.autogestion.entity.*;
import com.autogestion.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdenTrabajoService {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OtProductoUsadoRepository otProductoUsadoRepository;
    private final CotizacionRepository cotizacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final InventarioMovimientoRepository inventarioMovimientoRepository;
    private final RecepcionRepository recepcionRepository;

    @Transactional
    public OrdenTrabajo crear(OrdenTrabajoRequest request) {
        Cotizacion cotizacion = cotizacionRepository.findById(request.getCotizacionId())
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));

        
        if (!"APROBADA".equals(cotizacion.getEstado())) {
            throw new RuntimeException("Solo se puede crear OT con cotización aprobada");
        }

        Usuario mecanico = usuarioRepository.findById(request.getMecanicoId())
                .orElseThrow(() -> new RuntimeException("Mecánico no encontrado"));

        OrdenTrabajo ot = OrdenTrabajo.builder()
                .cotizacion(cotizacion)
                .mecanico(mecanico)
                .estado("PENDIENTE")
                .fechaCreacion(LocalDateTime.now())
                .build();
        ot = ordenTrabajoRepository.save(ot);

        
        Diagnostico diagnostico = cotizacion.getDiagnostico();
        Recepcion recepcion = diagnostico.getRecepcion();
        recepcion.setEstado("EN_TRABAJO");
        recepcionRepository.save(recepcion);

        return ot;
    }

    @Transactional
    public OrdenTrabajo cambiarEstado(Long id, String nuevoEstado) {
        OrdenTrabajo ot = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada"));

        
        String estadoActual = ot.getEstado();
        boolean transicionValida = switch (estadoActual) {
            case "PENDIENTE" -> "EN_PROCESO".equals(nuevoEstado) || "CANCELADA".equals(nuevoEstado);
            case "EN_PROCESO" -> "EN_PRUEBA".equals(nuevoEstado) || "FINALIZADA".equals(nuevoEstado) || "CANCELADA".equals(nuevoEstado);
            case "EN_PRUEBA" -> "FINALIZADA".equals(nuevoEstado) || "EN_PROCESO".equals(nuevoEstado) || "CANCELADA".equals(nuevoEstado);
            default -> false;
        };

        if (!transicionValida) {
            throw new RuntimeException("Transición de estado no válida: " + estadoActual + " → " + nuevoEstado);
        }

        ot.setEstado(nuevoEstado);
        if ("FINALIZADA".equals(nuevoEstado)) {
            ot.setFechaFin(LocalDateTime.now());
        }

        
        if ("FINALIZADA".equals(nuevoEstado)) {
            Recepcion recepcion = ot.getCotizacion().getDiagnostico().getRecepcion();
            recepcion.setEstado("FINALIZADA");
            recepcionRepository.save(recepcion);
        }

        return ordenTrabajoRepository.save(ot);
    }

    @Transactional
    public OtProductoUsado registrarProductoUsado(Long ordenTrabajoId, ProductoUsadoRequest request) {
        OrdenTrabajo ot = ordenTrabajoRepository.findById(ordenTrabajoId)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada"));

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        
        if (producto.getStockActual() < request.getCantidadUsada()) {
            throw new RuntimeException("Stock insuficiente para: " + producto.getNombre()
                    + " (disponible: " + producto.getStockActual() + ")");
        }

        
        OtProductoUsado uso = OtProductoUsado.builder()
                .ordenTrabajo(ot)
                .producto(producto)
                .cantidadUsada(request.getCantidadUsada())
                .build();
        uso = otProductoUsadoRepository.save(uso);

        
        InventarioMovimiento movimiento = InventarioMovimiento.builder()
                .producto(producto)
                .tipo("CONSUMO")
                .cantidad(request.getCantidadUsada())
                .motivo("OT #" + ot.getId() + " - " + producto.getNombre())
                .fecha(LocalDateTime.now())
                .build();
        inventarioMovimientoRepository.save(movimiento);

        
        producto.setStockActual(producto.getStockActual() - request.getCantidadUsada());
        productoRepository.save(producto);

        
        if (producto.getStockActual() < producto.getStockMinimo()) {
            
        }

        return uso;
    }

    public List<OrdenTrabajo> listar(String estado) {
        if (estado != null && !estado.isEmpty()) {
            return ordenTrabajoRepository.findByEstado(estado);
        }
        return ordenTrabajoRepository.findAll();
    }

    public List<OrdenTrabajo> listarPorMecanico(Long mecanicoId) {
        return ordenTrabajoRepository.findByMecanicoId(mecanicoId);
    }

    public OrdenTrabajo obtenerPorId(Long id) {
        return ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada"));
    }

    public List<OtProductoUsado> listarProductosUsados(Long ordenTrabajoId) {
        return otProductoUsadoRepository.findByOrdenTrabajoId(ordenTrabajoId);
    }
}
