package com.autogestion.repository;

import com.autogestion.entity.CategoriaGasto;
import com.autogestion.entity.Gasto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface GastoRepository extends JpaRepository<Gasto, Long> {

    @Query("""
        SELECT g FROM Gasto g
        WHERE (:categoria IS NULL OR g.categoria = :categoria)
          AND g.fecha >= :desde AND g.fecha <= :hasta
        """)
    Page<Gasto> filtrar(CategoriaGasto categoria, LocalDate desde, LocalDate hasta, Pageable pageable);

    @Query("""
        SELECT COALESCE(SUM(g.monto), 0) FROM Gasto g
        WHERE g.fecha >= :desde AND g.fecha <= :hasta
        """)
    java.math.BigDecimal totalEntre(LocalDate desde, LocalDate hasta);

    @Query("""
        SELECT g.categoria, COUNT(g), COALESCE(SUM(g.monto), 0) FROM Gasto g
        WHERE g.fecha >= :desde AND g.fecha <= :hasta
        GROUP BY g.categoria ORDER BY 3 DESC
        """)
    List<Object[]> porCategoria(LocalDate desde, LocalDate hasta);
}
