package com.autogestion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProveedorRequest {

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Size(max = 150, message = "Nombre muy largo")
    private String nombre;

    private String ruc;

    @Size(max = 30, message = "Teléfono muy largo")
    private String telefono;

    @Email(message = "El email no tiene formato válido")
    private String email;
}