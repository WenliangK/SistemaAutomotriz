package com.autogestion.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cotizacion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagnostico_id", nullable = false)
    private Diagnostico diagnostico;

    @Column(nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}
