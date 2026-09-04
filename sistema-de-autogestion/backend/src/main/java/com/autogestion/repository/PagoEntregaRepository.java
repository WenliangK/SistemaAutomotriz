package com.autogestion.repository;

import com.autogestion.entity.PagoEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PagoEntregaRepository extends JpaRepository<PagoEntrega, Long> {
    Optional<PagoEntrega> findByOrdenTrabajoId(Long ordenTrabajoId);
}
