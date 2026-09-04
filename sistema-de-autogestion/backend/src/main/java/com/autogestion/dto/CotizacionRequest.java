package com.autogestion.dto;

import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class CotizacionRequest {
    private Long diagnosticoId;
    private List<ServicioCotizacion> servicios;
    private List<ProductoCotizacion> productos;

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ServicioCotizacion {
        private Long servicioId;
        private Double precio;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ProductoCotizacion {
        private Long productoId;
        private Integer cantidadEstimada;
        private Double precioUnitario;
    }
}
