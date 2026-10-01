package com.autogestion.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class ClienteRequest {
    private String nombre;
    private String telefono;
    private String email;
    private String documento;
}
