package com.autogestion.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CotizacionResponseDTO {
    private Long id;
    private Long diagnosticoId;
    private String diagnosticoDescripcion;
    private String recepcionId;
    private String vehiculoPlaca;
    private String clienteNombre;
    private BigDecimal total;
    private String estado;
    private LocalDateTime fecha;
    
    private List<CotizacionServicioDTO> servicios;
    private List<CotizacionProductoDTO> productos;
    
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CotizacionServicioDTO {
        private Long servicioId;
        private String servicioNombre;
        private BigDecimal precio;
    }
    
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CotizacionProductoDTO {
        private Long productoId;
        private String productoNombre;
        private Integer cantidadEstimada;
        private BigDecimal precioUnitario;
    }
}