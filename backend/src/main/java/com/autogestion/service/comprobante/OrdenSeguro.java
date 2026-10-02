package com.autogestion.service.comprobante;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SRP: única razón de cambio = blindar el ORDER BY de consultas nativas.
 * {@code filtrar()} es SQL nativo: Spring Data mete el Sort en el ORDER BY
 * sin traducir nombres de propiedad, así que "fechaEmision" llegaría tal cual
 * a PostgreSQL y chocaría con la columna {@code fecha_emision}. Solo pasan
 * las columnas de la tabla (whitelist), que además evita inyección vía ?sort=.
 */
public final class OrdenSeguro {

    private OrdenSeguro() { }

    /**
     * Traduce el orden pedido a columnas reales; si no queda nada válido,
     * aplica el orden determinista por defecto (la paginación lo exige).
     */
    public static Pageable traducir(Pageable pageable, Map<String, String> columnas,
                                    Sort.Order... porDefecto) {
        List<Sort.Order> orden = new ArrayList<>();
        for (Sort.Order o : pageable.getSort()) {
            String columna = columnas.get(o.getProperty());
            if (columna != null) {
                orden.add(new Sort.Order(o.getDirection(), columna, o.getNullHandling()));
            }
        }
        if (orden.isEmpty()) {
            orden.addAll(List.of(porDefecto));
        }
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orden));
    }
}
