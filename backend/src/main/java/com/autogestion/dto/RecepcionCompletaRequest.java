package com.autogestion.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class RecepcionCompletaRequest {

    @NotNull
    @Size(min = 2, max = 150)
    private String clienteNombre;

    private String clienteTelefono;

    private String clienteEmail;

    private String clienteDocumento;

    @NotNull
    @Size(min = 6, max = 10)
    private String vehiculoPlaca;

    private String vehiculoMarca;

    private String vehiculoModelo;

    private Integer vehiculoAnio;

    @NotNull
    @Size(min = 5, max = 2000)
    private String problemaReportado;
}