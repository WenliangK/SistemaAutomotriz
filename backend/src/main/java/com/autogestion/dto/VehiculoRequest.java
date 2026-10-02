package com.autogestion.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class VehiculoRequest {

    @NotNull(message = "Falta el cliente del vehículo")
    private Long clienteId;

    @NotBlank(message = "La placa es obligatoria")
    @Pattern(regexp = "^[A-Za-z0-9]{3}-?[A-Za-z0-9]{3}$",
            message = "Placa peruana: 3 letras/números, guion opcional y 3 más (ej. ABC-123)")
    private String placa;

    @Size(max = 50, message = "La marca es muy larga")
    private String marca;

    @Size(max = 50, message = "El modelo es muy largo")
    private String modelo;

    @Min(value = 1950, message = "El año no puede ser anterior a 1950")
    @Max(value = 2100, message = "El año no es válido")
    private Integer anio;

    /** Placa en mayúsculas y sin espacios, como se guarda en BD. */
    public String placaNormalizada() {
        return placa == null ? null : placa.toUpperCase().trim().replaceAll("\\s+", "");
    }
}
