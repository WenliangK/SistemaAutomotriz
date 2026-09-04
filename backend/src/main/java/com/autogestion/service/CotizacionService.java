package com.autogestion.service;

import com.autogestion.dto.CotizacionRequest;
import com.autogestion.entity.*;
import com.autogestion.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CotizacionService {

    private final CotizacionRepository cotizacionRepository;
    private final CotizacionServicioRepository cotizacionServicioRepository;
    private final CotizacionProductoRepository cotizacionProductoRepository;
    private final DiagnosticoRepository diagnosticoRepository;
    private final ServicioRepository servicioRepository;
    private final ProductoRepository productoRepository;
    private final RecepcionRepository recepcionRepository;

    @Transactional
    public Cotizacion crear(CotizacionRequest request) {
        Diagnostico diagnostico = diagnosticoRepository.findById(request.getDiagnosticoId())
                .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado"));

        
        Cotizacion cotizacion = Cotizacion.builder()
                .diagnostico(diagnostico)
                .total(BigDecimal.ZERO)
                .estado("PENDIENTE")
                .fecha(LocalDateTime.now())
                .build();
        cotizacion = cotizacionRepository.save(cotizacion);

        BigDecimal total = BigDecimal.ZERO;

        
        if (request.getServicios() != null) {
            for (CotizacionRequest.ServicioCotizacion sc : request.getServicios()) {
                Servicio servicio = servicioRepository.findById(sc.getServicioId())
                        .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + sc.getServicioId()));

                BigDecimal precio = sc.getPrecio() != null
                        ? BigDecimal.valueOf(sc.getPrecio())
                        : servicio.getPrecioBase();

                CotizacionServicio cs = CotizacionServicio.builder()
                        .cotizacion(cotizacion)
                        .servicio(servicio)
                        .precio(precio)
                        .build();
                cotizacionServicioRepository.save(cs);
                total = total.add(precio);
            }
        }

        
        if (request.getProductos() != null) {
            for (CotizacionRequest.ProductoCotizacion pc : request.getProductos()) {
                Producto producto = productoRepository.findById(pc.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + pc.getProductoId()));

                BigDecimal precioUnitario = pc.getPrecioUnitario() != null
                        ? BigDecimal.valueOf(pc.getPrecioUnitario())
                        : producto.getPrecioUnitario();

                BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(pc.getCantidadEstimada()));

                CotizacionProducto cp = CotizacionProducto.builder()
                        .cotizacion(cotizacion)
                        .producto(producto)
                        .cantidadEstimada(pc.getCantidadEstimada())
                        .precioUnitario(precioUnitario)
                        .build();
                cotizacionProductoRepository.save(cp);
                total = total.add(subtotal);
            }
        }

        
        cotizacion.setTotal(total);
        return cotizacionRepository.save(cotizacion);
    }

    @Transactional
    public Cotizacion aprobar(Long id) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));

        if (!"PENDIENTE".equals(cotizacion.getEstado())) {
            throw new RuntimeException("Solo se pueden aprobar cotizaciones pendientes");
        }

        cotizacion.setEstado("APROBADA");
        cotizacion = cotizacionRepository.save(cotizacion);

        
        Diagnostico diagnostico = cotizacion.getDiagnostico();
        Recepcion recepcion = diagnostico.getRecepcion();
        recepcion.setEstado("COTIZADA");
        recepcionRepository.save(recepcion);

        return cotizacion;
    }

    @Transactional
    public Cotizacion rechazar(Long id) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));

        if (!"PENDIENTE".equals(cotizacion.getEstado())) {
            throw new RuntimeException("Solo se pueden rechazar cotizaciones pendientes");
        }

        cotizacion.setEstado("RECHAZADA");
        return cotizacionRepository.save(cotizacion);
    }

    public Cotizacion obtenerPorId(Long id) {
        return cotizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));
    }

    public List<Cotizacion> listar(String estado) {
        if (estado != null && !estado.isEmpty()) {
            return cotizacionRepository.findByEstado(estado);
        }
        return cotizacionRepository.findAll();
    }

    public List<CotizacionServicio> listarServicios(Long cotizacionId) {
        return cotizacionServicioRepository.findByCotizacionId(cotizacionId);
    }

    public List<CotizacionProducto> listarProductos(Long cotizacionId) {
        return cotizacionProductoRepository.findByCotizacionId(cotizacionId);
    }
}
