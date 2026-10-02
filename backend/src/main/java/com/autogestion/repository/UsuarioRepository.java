package com.autogestion.repository;

import com.autogestion.entity.TipoDocumento;
import com.autogestion.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByTipoDocumentoAndDocumento(TipoDocumento tipoDocumento, String documento);

    List<Usuario> findByRolAndActivo(String rol, boolean activo);

    long countByRolAndActivo(String rol, boolean activo);

    /**
     * :q es el patrón ya armado ({@link com.autogestion.util.Like#patron});
     * concat parámetro a parámetro (CONCAT('%', :q, '%')) haría que Hibernate
     * lo atara como bytea y Postgres fallara con "lower(bytea) does not exist".
     */
    @Query("""
        SELECT u FROM Usuario u
        WHERE (:rol IS NULL OR u.rol = :rol)
          AND (:activo IS NULL OR u.activo = :activo)
          AND (LOWER(u.nombre) LIKE :q
               OR LOWER(u.email) LIKE :q
               OR (u.documento IS NOT NULL AND LOWER(u.documento) LIKE :q))
        """)
    Page<Usuario> filtrar(String rol, Boolean activo, String q, Pageable pageable);
}
