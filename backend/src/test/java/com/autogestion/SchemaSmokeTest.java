package com.autogestion;

import com.autogestion.repository.ClienteRepository;
import com.autogestion.repository.SerieComprobanteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Arranca el contexto completo con H2: valida schema-h2.sql, entidades,
 * EmpresaProperties (RUC emisor) y datos semilla. Si algo del modelo falla,
 * falla aquí antes de llegar a Docker.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SchemaSmokeTest {

    @Autowired
    private ClienteRepository clientes;

    @Autowired
    private SerieComprobanteRepository series;

    @Test
    void contextoArrancaYSeriesExisten() {
        assertTrue(clientes.count() >= 3, "La semilla debe traer al menos 3 clientes");
        assertTrue(series.findById("B001").isPresent(), "Falta serie B001");
        assertTrue(series.findById("F001").isPresent(), "Falta serie F001");
    }
}
