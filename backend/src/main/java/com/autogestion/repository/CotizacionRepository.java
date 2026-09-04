package com.autogestion.repository;

import com.autogestion.entity.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
    List<Cotizacion> findByEstado(String estado);
}
