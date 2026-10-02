package com.autogestion.repository;

import com.autogestion.entity.Cliente;
import com.autogestion.entity.TipoDocumento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDocumento(String documento);
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByTelefono(String telefono);

    /** Identidad de una persona: tipo + número de documento. */
    Optional<Cliente> findByTipoDocumentoAndDocumento(TipoDocumento tipoDocumento, String documento);

    boolean existsByTipoDocumentoAndDocumento(TipoDocumento tipoDocumento, String documento);

    /**
     * :q es el patrón ya armado ({@link com.autogestion.util.Like#patron}).
     * CONCAT('%', :q, '%') haría que Hibernate lo atara como bytea y Postgres
     * fallara con "lower(bytea) does not exist".
     */
    @Query("""
        SELECT c FROM Cliente c
        WHERE LOWER(c.nombre) LIKE :q
           OR LOWER(c.documento) LIKE :q
           OR LOWER(c.telefono) LIKE :q
           OR LOWER(c.email) LIKE :q
           OR (c.razonSocial IS NOT NULL AND LOWER(c.razonSocial) LIKE :q)
        """)
    Page<Cliente> buscar(String q, Pageable pageable);
}
