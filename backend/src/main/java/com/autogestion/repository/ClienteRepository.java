package com.autogestion.repository;

import com.autogestion.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDocumento(String documento);
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByTelefono(String telefono);
}
