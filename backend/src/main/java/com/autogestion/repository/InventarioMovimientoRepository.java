package com.autogestion.repository;

import com.autogestion.entity.InventarioMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventarioMovimientoRepository extends JpaRepository<InventarioMovimiento, Long> {
    List<InventarioMovimiento> findByProductoId(Long productoId);
    List<InventarioMovimiento> findByTipo(String tipo);
}
