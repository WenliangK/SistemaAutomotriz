package com.autogestion.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cotizacion_producto")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CotizacionProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cotizacion_id", nullable = false)
    private Cotizacion cotizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "cantidad_estimada", nullable = false)
    private Integer cantidadEstimada;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;
}
