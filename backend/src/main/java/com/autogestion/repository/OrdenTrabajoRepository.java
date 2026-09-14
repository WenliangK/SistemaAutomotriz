package com.autogestion.repository;

import com.autogestion.entity.OrdenTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Long> {

    @Query("SELECT ot FROM OrdenTrabajo ot LEFT JOIN FETCH ot.cotizacion c LEFT JOIN FETCH c.diagnostico d LEFT JOIN FETCH d.recepcion r LEFT JOIN FETCH r.vehiculo v LEFT JOIN FETCH v.cliente LEFT JOIN FETCH ot.mecanico WHERE ot.estado = :estado")
    List<OrdenTrabajo> findByEstado(String estado);

    @Query("SELECT ot FROM OrdenTrabajo ot LEFT JOIN FETCH ot.cotizacion c LEFT JOIN FETCH c.diagnostico d LEFT JOIN FETCH d.recepcion r LEFT JOIN FETCH r.vehiculo v LEFT JOIN FETCH v.cliente LEFT JOIN FETCH ot.mecanico")
    List<OrdenTrabajo> findAll();

    @Query("SELECT ot FROM OrdenTrabajo ot LEFT JOIN FETCH ot.cotizacion c LEFT JOIN FETCH c.diagnostico d LEFT JOIN FETCH d.recepcion r LEFT JOIN FETCH r.vehiculo v LEFT JOIN FETCH v.cliente LEFT JOIN FETCH ot.mecanico WHERE ot.mecanico.id = :mecanicoId")
    List<OrdenTrabajo> findByMecanicoId(Long mecanicoId);

    @Query("SELECT ot FROM OrdenTrabajo ot LEFT JOIN FETCH ot.cotizacion c LEFT JOIN FETCH c.diagnostico d LEFT JOIN FETCH d.recepcion r LEFT JOIN FETCH r.vehiculo v LEFT JOIN FETCH v.cliente LEFT JOIN FETCH ot.mecanico WHERE ot.id = :id")
    Optional<OrdenTrabajo> findById(Long id);

    @Query("SELECT COUNT(ot) FROM OrdenTrabajo ot WHERE ot.estado = 'FINALIZADA' AND ot.fechaFin IS NOT NULL")
    long countFinalizadas();
}