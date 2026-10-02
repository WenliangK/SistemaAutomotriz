package com.autogestion.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre completo ("Luis Alberto Ramírez Torres"). Siempre sincronizado con nombres+apellidos. */
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String nombres;

    @Column(length = 100)
    private String apellidos;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 10)
    private TipoDocumento tipoDocumento = TipoDocumento.DNI;

    @Column(length = 12)
    private String documento;

    @Column(length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Especialidad especialidad;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false, length = 20)
    private String rol;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

    @Builder.Default
    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();

    /** Nombre para mostrar en OT, comprobantes y reportes. Nunca "Mecánico 1". */
    public String getNombreCompleto() {
        String n = ((nombres != null ? nombres : "") + " " + (apellidos != null ? apellidos : "")).trim();
        return n.isEmpty() ? nombre : n;
    }

    /** Iniciales para el avatar (LR, JC...). */
    public String getIniciales() {
        String base = getNombreCompleto().trim();
        if (base.isEmpty()) return "?";
        String[] partes = base.split("\\s+");
        String ini = partes[0].substring(0, 1);
        if (partes.length > 1) ini += partes[partes.length - 1].substring(0, 1);
        return ini.toUpperCase();
    }
}
