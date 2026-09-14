package com.autogestion.repository;

import com.autogestion.entity.Recepcion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecepcionRepository extends JpaRepository<Recepcion, Long> {

    @EntityGraph(attributePaths = {"vehiculo", "vehiculo.cliente"})
    List<Recepcion> findByEstado(String estado);

    @EntityGraph(attributePaths = {"vehiculo", "vehiculo.cliente"})
    List<Recepcion> findAll();
}
