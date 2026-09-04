package com.autogestion.repository;

import com.autogestion.entity.CotizacionProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CotizacionProductoRepository extends JpaRepository<CotizacionProducto, Long> {
    List<CotizacionProducto> findByCotizacionId(Long cotizacionId);
}
