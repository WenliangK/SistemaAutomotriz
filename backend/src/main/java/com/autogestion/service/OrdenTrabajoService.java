package com.autogestion.service;

import com.autogestion.dto.OrdenTrabajoCompletaRequest;
import com.autogestion.dto.OrdenTrabajoFinalizadaDTO;
import com.autogestion.dto.OrdenTrabajoRequest;
import com.autogestion.dto.OrdenTrabajoResponseDTO;
import com.autogestion.dto.OtProductoUsadoResponseDTO;
import com.autogestion.dto.PagoEntregaCompletaRequest;
import com.autogestion.dto.PagoEntregaResponseDTO;
import com.autogestion.dto.ProductoUsadoRequest;
import com.autogestion.entity.*;
import com.autogestion.repository.*;
import com.autogestion.service.PagoEntregaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdenTrabajoService {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OtProductoUsadoRepository otProductoUsadoRepository;
    private final CotizacionRepository cotizacionRepository;
    private final PagoEntregaService pagoEntregaService;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final InventarioMovimientoRepository inventarioMovimientoRepository;
    private final RecepcionRepository recepcionRepository;
    private final PagoEntregaRepository pagoEntregaRepository;

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
        
        cotizacion.setEstado("CONVERTIDA");
        cotizacionRepository.save(cotizacion);
        
        return ot;
    }

    @Transactional
    public OrdenTrabajo crearCompleta(OrdenTrabajoCompletaRequest request) {
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

        
        if (request.getProductosUsados() != null) {
            for (OrdenTrabajoCompletaRequest.ProductoUsadoItem item : request.getProductosUsados()) {
                registrarProductoUsadoInterno(ot, item.getProductoId(), item.getCantidadUsada());
            }
        }

        
        if (request.getPagoEntrega() != null && request.getPagoEntrega().getMonto() != null) {
            registrarPagoInterno(ot, request.getPagoEntrega().getMonto());
            if (Boolean.TRUE.equals(request.getPagoEntrega().getRegistrarEntrega())) {
                registrarEntregaInterno(ot);
            }
        }

        cotizacion.setEstado("CONVERTIDA");
        cotizacionRepository.save(cotizacion);
        
        return ordenTrabajoRepository.save(ot);
    }

    private void registrarProductoUsadoInterno(OrdenTrabajo ot, Long productoId, Integer cantidad) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente para: " + producto.getNombre()
                    + " (disponible: " + producto.getStockActual() + ")");
        }

        OtProductoUsado uso = OtProductoUsado.builder()
                .ordenTrabajo(ot)
                .producto(producto)
                .cantidadUsada(cantidad)
                .build();
        otProductoUsadoRepository.save(uso);

        InventarioMovimiento movimiento = InventarioMovimiento.builder()
                .producto(producto)
                .tipo("CONSUMO")
                .cantidad(cantidad)
                .motivo("OT #" + ot.getId() + " - " + producto.getNombre())
                .fecha(LocalDateTime.now())
                .build();
        inventarioMovimientoRepository.save(movimiento);

        producto.setStockActual(producto.getStockActual() - cantidad);
        productoRepository.save(producto);
    }

    private void registrarPagoInterno(OrdenTrabajo ot, Double monto) {
        PagoEntrega pago = pagoEntregaRepository.findByOrdenTrabajoId(ot.getId()).orElse(null);
        if (pago == null) {
            pago = PagoEntrega.builder()
                    .ordenTrabajo(ot)
                    .build();
        } else if (pago.getFechaPago() != null) {
            throw new RuntimeException("Esta OT ya tiene un pago registrado el " + pago.getFechaPago());
        }
        pago.setMonto(BigDecimal.valueOf(monto));
        pago.setFechaPago(LocalDateTime.now());
        pagoEntregaRepository.save(pago);
    }

    private void registrarEntregaInterno(OrdenTrabajo ot) {
        PagoEntrega pago = pagoEntregaRepository.findByOrdenTrabajoId(ot.getId()).orElse(null);
        if (pago == null) {
            pago = PagoEntrega.builder()
                    .ordenTrabajo(ot)
                    .monto(BigDecimal.ZERO)
                    .build();
        }
        pago.setFechaEntrega(LocalDateTime.now());
        pagoEntregaRepository.save(pago);

        ot.setEstado("FINALIZADA");
        ot.setFechaFin(LocalDateTime.now());

        Recepcion recepcion = ot.getCotizacion().getDiagnostico().getRecepcion();
        recepcion.setEstado("FINALIZADA");
        recepcionRepository.save(recepcion);
    }

    @Transactional
    public PagoEntregaResponseDTO registrarPagoEntregaCompleto(PagoEntregaCompletaRequest request) {
        OrdenTrabajo ot = ordenTrabajoRepository.findById(request.getOrdenTrabajoId())
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada"));

        if (request.getMonto() != null) {
            registrarPagoInterno(ot, request.getMonto());
        }
        if (Boolean.TRUE.equals(request.getRegistrarEntrega())) {
            registrarEntregaInterno(ot);
        }
        PagoEntrega pago = pagoEntregaRepository.findByOrdenTrabajoId(request.getOrdenTrabajoId())
                .orElseThrow(() -> new RuntimeException("Error al obtener pago/entrega"));
        return pagoEntregaService.toResponseDTO(pago);
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajoFinalizadaDTO> listarFinalizadasConPago() {
        return ordenTrabajoRepository.findByEstado("FINALIZADA").stream()
                .map(ot -> {
                    BigDecimal monto = pagoEntregaService.obtenerMonto(ot.getId());
                    PagoEntrega pago = pagoEntregaRepository.findByOrdenTrabajoId(ot.getId()).orElse(null);
                    boolean tienePago = pago != null && pago.getFechaPago() != null;
                    boolean tieneEntrega = pago != null && pago.getFechaEntrega() != null;
                    return new OrdenTrabajoFinalizadaDTO(
                            ot.getId(),
                            ot.getCotizacion().getId(),
                            ot.getMecanico().getNombre(),
                            ot.getFechaCreacion(),
                            monto,
                            tienePago,
                            tieneEntrega
                    );
                })
                .collect(Collectors.toList());
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
    public OtProductoUsadoResponseDTO registrarProductoUsado(Long ordenTrabajoId, ProductoUsadoRequest request) {
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

        return toProductoUsadoResponseDTO(uso);
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajoResponseDTO> listar(String estado) {
        List<OrdenTrabajo> ots = (estado != null && !estado.isEmpty())
                ? ordenTrabajoRepository.findByEstado(estado)
                : ordenTrabajoRepository.findAll();
        return ots.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajoResponseDTO> listarPorMecanico(Long mecanicoId) {
        return ordenTrabajoRepository.findByMecanicoId(mecanicoId).stream()
                .map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrdenTrabajoResponseDTO obtenerPorId(Long id) {
        OrdenTrabajo ot = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada"));
        return toResponseDTO(ot);
    }

    @Transactional(readOnly = true)
    public List<OtProductoUsadoResponseDTO> listarProductosUsados(Long ordenTrabajoId) {
        return otProductoUsadoRepository.findByOrdenTrabajoId(ordenTrabajoId).stream()
                .map(this::toProductoUsadoResponseDTO)
                .collect(Collectors.toList());
    }

    private OrdenTrabajoResponseDTO toResponseDTO(OrdenTrabajo ot) {
        Cotizacion cotizacion = ot.getCotizacion();
        Diagnostico diagnostico = cotizacion.getDiagnostico();
        Recepcion recepcion = diagnostico.getRecepcion();
        Vehiculo vehiculo = recepcion.getVehiculo();
        Cliente cliente = vehiculo.getCliente();
        Usuario mecanico = ot.getMecanico();

        return OrdenTrabajoResponseDTO.builder()
                .id(ot.getId())
                .cotizacionId(cotizacion.getId())
                .mecanicoId(mecanico.getId())
                .mecanicoNombre(mecanico.getNombre())
                .estado(ot.getEstado())
                .fechaCreacion(ot.getFechaCreacion())
                .fechaFin(ot.getFechaFin())
                .vehiculoPlaca(vehiculo.getPlaca())
                .clienteNombre(cliente.getNombre())
                .build();
    }

    private OtProductoUsadoResponseDTO toProductoUsadoResponseDTO(OtProductoUsado uso) {
        Producto producto = uso.getProducto();
        return OtProductoUsadoResponseDTO.builder()
                .id(uso.getId())
                .ordenTrabajoId(uso.getOrdenTrabajo().getId())
                .productoId(producto.getId())
                .productoNombre(producto.getNombre())
                .cantidadUsada(uso.getCantidadUsada())
                .build();
    }

}
