package com.autogestion.service;

import com.autogestion.dto.MovimientoInventarioRequest;
import com.autogestion.dto.ProductoRequest;
import com.autogestion.entity.InventarioMovimiento;
import com.autogestion.entity.Producto;
import com.autogestion.repository.InventarioMovimientoRepository;
import com.autogestion.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final ProductoRepository productoRepository;
    private final InventarioMovimientoRepository inventarioMovimientoRepository;

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Producto crear(ProductoRequest request) {
        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .tipo(request.getTipo())
                .precioUnitario(java.math.BigDecimal.valueOf(request.getPrecioUnitario()))
                .stockActual(request.getStockActual() != null ? request.getStockActual() : 0)
                .stockMinimo(request.getStockMinimo() != null ? request.getStockMinimo() : 0)
                .build();
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizar(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (request.getNombre() != null) producto.setNombre(request.getNombre());
        if (request.getTipo() != null) producto.setTipo(request.getTipo());
        if (request.getPrecioUnitario() != null)
            producto.setPrecioUnitario(java.math.BigDecimal.valueOf(request.getPrecioUnitario()));
        if (request.getStockMinimo() != null) producto.setStockMinimo(request.getStockMinimo());

        return productoRepository.save(producto);
    }

    @Transactional
    public InventarioMovimiento registrarMovimiento(MovimientoInventarioRequest request) {
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        InventarioMovimiento movimiento = InventarioMovimiento.builder()
                .producto(producto)
                .tipo(request.getTipo())
                .cantidad(request.getCantidad())
                .motivo(request.getMotivo())
                .fecha(LocalDateTime.now())
                .build();

        
        switch (request.getTipo()) {
            case "ENTRADA":
                producto.setStockActual(producto.getStockActual() + request.getCantidad());
                break;
            case "AJUSTE":
                
                producto.setStockActual(producto.getStockActual() + request.getCantidad());
                break;
            case "CONSUMO":
                if (producto.getStockActual() < request.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para consumo");
                }
                producto.setStockActual(producto.getStockActual() - request.getCantidad());
                break;
        }

        productoRepository.save(producto);
        return inventarioMovimientoRepository.save(movimiento);
    }

    public List<Producto> alertasStock() {
        return productoRepository.findConStockBajo();
    }

    public List<InventarioMovimiento> listarMovimientos(Long productoId) {
        if (productoId != null) {
            return inventarioMovimientoRepository.findByProductoId(productoId);
        }
        return inventarioMovimientoRepository.findAll();
    }
}
