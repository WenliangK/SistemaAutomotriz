package com.autogestion.util;

import java.util.Locale;

/**
 * Patrón LIKE preparado en el backend: el parámetro ya llega con los comodines
 * y en minúsculas, listo para comparar contra LOWER(columna).
 *
 * <p>Por qué existe: Hibernate no sabe inferir el tipo de un parámetro dentro de
 * {@code CONCAT('%', :q, '%')} y con PostgreSQL lo ata como <i>bytea</i>. La
 * consulta revienta con {@code function lower(bytea) does not exist} (pero solo
 * cuando la búsqueda viene vacía: ahí el servicio pasa {@code null} y el parámetro
 * queda sin tipo). Además el patrón se arma una sola vez en Java en vez de
 * concatenarlo tres veces en SQL.</p>
 */
public final class Like {

    private Like() { }

    /** {@code %texto%} en minúsculas; sin filtro (solo comodín) si no hay texto. */
    public static String patron(String q) {
        if (q == null || q.isBlank()) return "%";
        return "%" + q.trim().toLowerCase(Locale.ROOT) + "%";
    }
}
