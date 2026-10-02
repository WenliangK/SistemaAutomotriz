package com.autogestion.dto;

import lombok.*;

import java.time.LocalDate;

/** Ficha de personal. Nunca incluye el hash de la contraseña. */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioResponseDTO {
    private Long id;
    private String nombreCompleto;
    private String nombres;
    private String apellidos;
    private String iniciales;
    private String tipoDocumento;
    private String documento;
    private String telefono;
    private String email;
    private String rol;
    private String especialidad;
    private String especialidadEtiqueta;
    private Boolean activo;
    private LocalDate fechaIngreso;
}
