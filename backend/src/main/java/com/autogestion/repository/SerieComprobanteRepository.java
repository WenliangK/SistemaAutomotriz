package com.autogestion.repository;

import com.autogestion.entity.SerieComprobante;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SerieComprobanteRepository extends JpaRepository<SerieComprobante, String> {

    /** Bloqueo pesimista: quien emite una serie espera su turno. Sin saltos ni repetidos. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SerieComprobante s WHERE s.serie = :serie")
    Optional<SerieComprobante> bloquearPorSerie(String serie);
}
