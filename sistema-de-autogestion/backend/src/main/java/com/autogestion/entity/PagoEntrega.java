package com.autogestion.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago_entrega")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PagoEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_trabajo_id", nullable = false, unique = true)
    private OrdenTrabajo ordenTrabajo;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;
}
