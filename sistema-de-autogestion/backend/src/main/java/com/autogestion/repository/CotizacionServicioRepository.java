package com.autogestion.repository;

import com.autogestion.entity.CotizacionServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CotizacionServicioRepository extends JpaRepository<CotizacionServicio, Long> {
    List<CotizacionServicio> findByCotizacionId(Long cotizacionId);
}
