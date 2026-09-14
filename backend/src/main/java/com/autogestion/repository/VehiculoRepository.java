package com.autogestion.repository;

import com.autogestion.entity.Vehiculo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    @EntityGraph(attributePaths = {"cliente"})
    Optional<Vehiculo> findByPlaca(String placa);

    @EntityGraph(attributePaths = {"cliente"})
    List<Vehiculo> findAll();
}
