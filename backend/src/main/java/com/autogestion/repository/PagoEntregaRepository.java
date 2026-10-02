package com.autogestion.repository;

import com.autogestion.entity.PagoEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PagoEntregaRepository extends JpaRepository<PagoEntrega, Long> {

    @Query("SELECT p FROM PagoEntrega p LEFT JOIN FETCH p.ordenTrabajo WHERE p.ordenTrabajo.id = :ordenTrabajoId")
    Optional<PagoEntrega> findByOrdenTrabajoId(Long ordenTrabajoId);

    @Query("""
        SELECT CAST(p.fechaEntrega AS date), COUNT(p) FROM PagoEntrega p
        WHERE p.fechaEntrega >= :desde AND p.fechaEntrega < :hasta
        GROUP BY CAST(p.fechaEntrega AS date) ORDER BY CAST(p.fechaEntrega AS date)
        """)
    List<Object[]> entregasPorDia(LocalDateTime desde, LocalDateTime hasta);
}
