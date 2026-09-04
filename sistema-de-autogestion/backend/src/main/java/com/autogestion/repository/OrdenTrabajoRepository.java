package com.autogestion.repository;

import com.autogestion.entity.OrdenTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Long> {
    List<OrdenTrabajo> findByMecanicoId(Long mecanicoId);
    List<OrdenTrabajo> findByEstado(String estado);

    @Query("SELECT COUNT(ot) FROM OrdenTrabajo ot WHERE ot.estado = 'FINALIZADA' AND ot.fechaFin IS NOT NULL")
    long countFinalizadas();
}
