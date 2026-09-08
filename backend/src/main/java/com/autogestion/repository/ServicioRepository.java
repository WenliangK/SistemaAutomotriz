package com.autogestion.repository;

import com.autogestion.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
}
