package com.autogestion.repository;

import com.autogestion.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT p FROM Producto p WHERE p.stockActual < p.stockMinimo")
    List<Producto> findConStockBajo();

    @Query("SELECT COUNT(p) FROM Producto p WHERE p.stockActual < p.stockMinimo")
    long countConStockBajo();

    /** Dinero quieto en el estante: stock × último costo. */
    @Query("SELECT COALESCE(SUM(p.stockActual * p.costoUnitario), 0) FROM Producto p")
    java.math.BigDecimal valorInventario();

    @Query("""
        SELECT p.nombre, p.precioUnitario, p.costoUnitario
        FROM Producto p WHERE p.activo = true ORDER BY p.nombre
        """)
    List<Object[]> preciosVsCostos();
}
