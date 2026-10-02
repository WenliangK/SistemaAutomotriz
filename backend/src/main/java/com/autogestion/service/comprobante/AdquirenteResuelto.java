package com.autogestion.service.comprobante;

import com.autogestion.entity.Cliente;

/**
 * Snapshot validado del adquirente que queda impreso en el comprobante.
 * Puede no ligarse a un cliente de la base (caso CLIENTES VARIOS).
 */
public record AdquirenteResuelto(Cliente cliente, String tipoDoc, String numDoc,
                                 String nombre, String direccion) { }
