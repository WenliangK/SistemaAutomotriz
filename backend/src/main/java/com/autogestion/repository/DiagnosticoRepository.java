package com.autogestion.repository;

import com.autogestion.entity.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {
    List<Diagnostico> findByRecepcionId(Long recepcionId);
}
