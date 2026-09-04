package com.autogestion.repository;

import com.autogestion.entity.Recepcion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecepcionRepository extends JpaRepository<Recepcion, Long> {
    List<Recepcion> findByEstado(String estado);
}
