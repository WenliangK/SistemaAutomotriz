package com.autogestion.repository;

import com.autogestion.entity.Diagnostico;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

    @EntityGraph(attributePaths = {"recepcion", "recepcion.vehiculo", "recepcion.vehiculo.cliente", "mecanico"})
    List<Diagnostico> findByRecepcionId(Long recepcionId);

    @EntityGraph(attributePaths = {"recepcion", "recepcion.vehiculo", "recepcion.vehiculo.cliente", "mecanico"})
    List<Diagnostico> findAll();
}