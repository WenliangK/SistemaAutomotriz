package com.autogestion.repository;

import com.autogestion.entity.OtProductoUsado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OtProductoUsadoRepository extends JpaRepository<OtProductoUsado, Long> {

    @Query("SELECT opu FROM OtProductoUsado opu LEFT JOIN FETCH opu.ordenTrabajo LEFT JOIN FETCH opu.producto WHERE opu.ordenTrabajo.id = :ordenTrabajoId")
    List<OtProductoUsado> findByOrdenTrabajoId(Long ordenTrabajoId);
}