package com.autogestion.repository;

import com.autogestion.entity.PagoEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface PagoEntregaRepository extends JpaRepository<PagoEntrega, Long> {

    @Query("SELECT p FROM PagoEntrega p LEFT JOIN FETCH p.ordenTrabajo WHERE p.ordenTrabajo.id = :ordenTrabajoId")
    Optional<PagoEntrega> findByOrdenTrabajoId(Long ordenTrabajoId);
}
