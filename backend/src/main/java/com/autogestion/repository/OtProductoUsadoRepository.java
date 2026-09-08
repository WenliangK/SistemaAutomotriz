package com.autogestion.repository;

import com.autogestion.entity.OtProductoUsado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OtProductoUsadoRepository extends JpaRepository<OtProductoUsado, Long> {
    List<OtProductoUsado> findByOrdenTrabajoId(Long ordenTrabajoId);
}
