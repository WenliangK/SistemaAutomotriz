package com.autogestion.service;

import com.autogestion.dto.CotizacionCompletaRequest;
import com.autogestion.dto.CotizacionRequest;
import com.autogestion.dto.CotizacionResponseDTO;
import com.autogestion.entity.*;
import com.autogestion.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public CotizacionResponseDTO crear(CotizacionRequest request) {
        try {
            Diagnostico diagnostico = diagnosticoRepository.findById(request.getDiagnosticoId())
                    .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado: " + request.getDiagnosticoId()));

        
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
        Cotizacion saved = cotizacionRepository.save(cotizacion);

        
        Diagnostico diag = saved.getDiagnostico();
        Recepcion recep = diag.getRecepcion();
        Vehiculo veh = recep.getVehiculo();
        Cliente cli = veh.getCliente();

        List<CotizacionServicio> cotizacionServicios = cotizacionServicioRepository.findByCotizacionId(saved.getId());
        List<CotizacionProducto> cotizacionProductos = cotizacionProductoRepository.findByCotizacionId(saved.getId());

        List<CotizacionResponseDTO.CotizacionServicioDTO> servicios = cotizacionServicios.stream()
            .map(cs -> CotizacionResponseDTO.CotizacionServicioDTO.builder()
                .servicioId(cs.getServicio().getId())
                .servicioNombre(cs.getServicio().getNombre())
                .precio(cs.getPrecio())
                .build())
            .collect(Collectors.toList());

        List<CotizacionResponseDTO.CotizacionProductoDTO> productos = cotizacionProductos.stream()
            .map(cp -> CotizacionResponseDTO.CotizacionProductoDTO.builder()
                .productoId(cp.getProducto().getId())
                .productoNombre(cp.getProducto().getNombre())
                .cantidadEstimada(cp.getCantidadEstimada())
                .precioUnitario(cp.getPrecioUnitario())
                .build())
            .collect(Collectors.toList());

        return CotizacionResponseDTO.builder()
            .id(saved.getId())
            .diagnosticoId(saved.getDiagnostico().getId())
            .diagnosticoDescripcion(saved.getDiagnostico().getDescripcion())
            .recepcionId(String.valueOf(saved.getDiagnostico().getRecepcion().getId()))
            .vehiculoPlaca(saved.getDiagnostico().getRecepcion().getVehiculo().getPlaca())
            .clienteNombre(saved.getDiagnostico().getRecepcion().getVehiculo().getCliente().getNombre())
            .total(saved.getTotal())
            .estado(saved.getEstado())
            .fecha(saved.getFecha())
            .servicios(servicios)
            .productos(productos)
            .build();
        } catch (Exception e) {
            throw new RuntimeException("Error al crear cotización: " + e.getMessage(), e);
        }
    }

    @Transactional
    public CotizacionResponseDTO crearCompleta(CotizacionCompletaRequest request) {
        try {
            Diagnostico diagnostico = diagnosticoRepository.findById(request.getDiagnosticoId())
                    .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado: " + request.getDiagnosticoId()));

        Cotizacion cotizacion = Cotizacion.builder()
                .diagnostico(diagnostico)
                .total(BigDecimal.ZERO)
                .estado("PENDIENTE")
                .fecha(LocalDateTime.now())
                .build();
        cotizacion = cotizacionRepository.save(cotizacion);

        BigDecimal total = BigDecimal.ZERO;

        if (request.getServicios() != null) {
            for (CotizacionCompletaRequest.ServicioItem item : request.getServicios()) {
                Servicio servicio = servicioRepository.findById(item.getServicioId())
                        .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + item.getServicioId()));

                BigDecimal precio = item.getPrecio() != null
                        ? BigDecimal.valueOf(item.getPrecio())
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
            for (CotizacionCompletaRequest.ProductoItem item : request.getProductos()) {
                Producto producto = productoRepository.findById(item.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductoId()));

                BigDecimal precioUnitario = item.getPrecioUnitario() != null
                        ? BigDecimal.valueOf(item.getPrecioUnitario())
                        : producto.getPrecioUnitario();

                BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(item.getCantidadEstimada()));

                CotizacionProducto cp = CotizacionProducto.builder()
                        .cotizacion(cotizacion)
                        .producto(producto)
                        .cantidadEstimada(item.getCantidadEstimada())
                        .precioUnitario(precioUnitario)
                        .build();
                cotizacionProductoRepository.save(cp);
                total = total.add(subtotal);
            }
        }

        cotizacion.setTotal(total);
        cotizacion = cotizacionRepository.save(cotizacion);
        
        
        Diagnostico diag = cotizacion.getDiagnostico();
        Recepcion recep = diag.getRecepcion();
        Vehiculo veh = recep.getVehiculo();
        Cliente cli = veh.getCliente();
        
        List<CotizacionServicio> cotizacionServicios = cotizacionServicioRepository.findByCotizacionId(cotizacion.getId());
        List<CotizacionProducto> cotizacionProductos = cotizacionProductoRepository.findByCotizacionId(cotizacion.getId());
        
        List<CotizacionResponseDTO.CotizacionServicioDTO> servicios = cotizacionServicios.stream()
            .map(cs -> CotizacionResponseDTO.CotizacionServicioDTO.builder()
                .servicioId(cs.getServicio().getId())
                .servicioNombre(cs.getServicio().getNombre())
                .precio(cs.getPrecio())
                .build())
            .collect(Collectors.toList());
        
        List<CotizacionResponseDTO.CotizacionProductoDTO> productos = cotizacionProductos.stream()
            .map(cp -> CotizacionResponseDTO.CotizacionProductoDTO.builder()
                .productoId(cp.getProducto().getId())
                .productoNombre(cp.getProducto().getNombre())
                .cantidadEstimada(cp.getCantidadEstimada())
                .precioUnitario(cp.getPrecioUnitario())
                .build())
            .collect(Collectors.toList());
        
        return CotizacionResponseDTO.builder()
            .id(cotizacion.getId())
            .diagnosticoId(cotizacion.getDiagnostico().getId())
            .diagnosticoDescripcion(cotizacion.getDiagnostico().getDescripcion())
            .recepcionId(String.valueOf(cotizacion.getDiagnostico().getRecepcion().getId()))
            .vehiculoPlaca(cotizacion.getDiagnostico().getRecepcion().getVehiculo().getPlaca())
            .clienteNombre(cotizacion.getDiagnostico().getRecepcion().getVehiculo().getCliente().getNombre())
            .total(cotizacion.getTotal())
            .estado(cotizacion.getEstado())
            .fecha(cotizacion.getFecha())
            .servicios(servicios)
            .productos(productos)
            .build();
        } catch (Exception e) {
            throw new RuntimeException("Error al crear cotización completa: " + e.getMessage(), e);
        }
    }

    @Transactional
    public CotizacionResponseDTO aprobar(Long id) {
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

        Cotizacion saved = cotizacionRepository.findById(cotizacion.getId())
                .orElseThrow(() -> new RuntimeException("Error al obtener cotización aprobada"));
        return toResponseDTO(saved);
    }

    @Transactional
    public CotizacionResponseDTO rechazar(Long id) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));

        if (!"PENDIENTE".equals(cotizacion.getEstado())) {
            throw new RuntimeException("Solo se pueden rechazar cotizaciones pendientes");
        }

        cotizacion.setEstado("RECHAZADA");
        cotizacion = cotizacionRepository.save(cotizacion);

        Cotizacion saved = cotizacionRepository.findById(cotizacion.getId())
                .orElseThrow(() -> new RuntimeException("Error al obtener cotización rechazada"));
        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public CotizacionResponseDTO obtenerPorId(Long id) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));
        return toResponseDTO(cotizacion);
    }

    @Transactional(readOnly = true)
    public List<CotizacionResponseDTO> listar(String estado) {
        List<Cotizacion> cotizaciones = (estado != null && !estado.isEmpty())
                ? cotizacionRepository.findByEstado(estado)
                : cotizacionRepository.findAll();
        return cotizaciones.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CotizacionResponseDTO.CotizacionServicioDTO> listarServicios(Long cotizacionId) {
        return cotizacionServicioRepository.findByCotizacionId(cotizacionId).stream()
                .map(cs -> CotizacionResponseDTO.CotizacionServicioDTO.builder()
                        .servicioId(cs.getServicio().getId())
                        .servicioNombre(cs.getServicio().getNombre())
                        .precio(cs.getPrecio())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CotizacionResponseDTO.CotizacionProductoDTO> listarProductos(Long cotizacionId) {
        return cotizacionProductoRepository.findByCotizacionId(cotizacionId).stream()
                .map(cp -> CotizacionResponseDTO.CotizacionProductoDTO.builder()
                        .productoId(cp.getProducto().getId())
                        .productoNombre(cp.getProducto().getNombre())
                        .cantidadEstimada(cp.getCantidadEstimada())
                        .precioUnitario(cp.getPrecioUnitario())
                        .build())
                .toList();
    }

    private CotizacionResponseDTO toResponseDTO(Cotizacion cotizacion) {
        Diagnostico diag = cotizacion.getDiagnostico();
        Recepcion recep = diag.getRecepcion();
        Vehiculo veh = recep.getVehiculo();

        List<CotizacionServicio> cotizacionServicios = cotizacionServicioRepository.findByCotizacionId(cotizacion.getId());
        List<CotizacionProducto> cotizacionProductos = cotizacionProductoRepository.findByCotizacionId(cotizacion.getId());

        List<CotizacionResponseDTO.CotizacionServicioDTO> servicios = cotizacionServicios.stream()
            .map(cs -> CotizacionResponseDTO.CotizacionServicioDTO.builder()
                .servicioId(cs.getServicio().getId())
                .servicioNombre(cs.getServicio().getNombre())
                .precio(cs.getPrecio())
                .build())
            .collect(Collectors.toList());

        List<CotizacionResponseDTO.CotizacionProductoDTO> productos = cotizacionProductos.stream()
            .map(cp -> CotizacionResponseDTO.CotizacionProductoDTO.builder()
                .productoId(cp.getProducto().getId())
                .productoNombre(cp.getProducto().getNombre())
                .cantidadEstimada(cp.getCantidadEstimada())
                .precioUnitario(cp.getPrecioUnitario())
                .build())
            .collect(Collectors.toList());

        return CotizacionResponseDTO.builder()
            .id(cotizacion.getId())
            .diagnosticoId(cotizacion.getDiagnostico().getId())
            .diagnosticoDescripcion(cotizacion.getDiagnostico().getDescripcion())
            .recepcionId(String.valueOf(cotizacion.getDiagnostico().getRecepcion().getId()))
            .vehiculoPlaca(cotizacion.getDiagnostico().getRecepcion().getVehiculo().getPlaca())
            .clienteNombre(cotizacion.getDiagnostico().getRecepcion().getVehiculo().getCliente().getNombre())
            .total(cotizacion.getTotal())
            .estado(cotizacion.getEstado())
            .fecha(cotizacion.getFecha())
            .servicios(servicios)
            .productos(productos)
            .build();
    }
}