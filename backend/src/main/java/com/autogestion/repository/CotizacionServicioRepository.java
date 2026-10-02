package com.autogestion.repository;

import com.autogestion.entity.CotizacionServicio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CotizacionServicioRepository extends JpaRepository<CotizacionServicio, Long> {
    List<CotizacionServicio> findByCotizacionId(Long cotizacionId);

    @Query("""
        SELECT s.nombre, COUNT(cs), COALESCE(SUM(cs.precio), 0)
        FROM CotizacionServicio cs JOIN cs.servicio s JOIN cs.cotizacion c
        WHERE c.fecha >= :desde AND c.fecha < :hasta
        GROUP BY s.nombre ORDER BY COUNT(cs) DESC
        """)
    List<Object[]> topServicios(LocalDateTime desde, LocalDateTime hasta, Pageable pageable);
}
