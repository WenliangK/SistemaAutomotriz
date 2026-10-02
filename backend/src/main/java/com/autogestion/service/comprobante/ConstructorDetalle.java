package com.autogestion.service.comprobante;

import com.autogestion.config.EmpresaProperties;
import com.autogestion.entity.Comprobante;
import com.autogestion.entity.ComprobanteDetalle;
import com.autogestion.entity.OrdenTrabajo;
import com.autogestion.exception.BusinessException;
import com.autogestion.repository.CotizacionProductoRepository;
import com.autogestion.repository.CotizacionServicioRepository;
import com.autogestion.util.NumeroALetras;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * SRP: única razón de cambio = cómo se arma el detalle y los totales.
 * Los precios de la cotización INCLUYEN IGV, así que:
 * valor_unitario = precio / 1.18. El total del comprobante es la suma
 * de líneas (= cotización.total por construcción); el IGV se deriva
 * (total − gravado) para que total = gravado + IGV al centavo.
 */
@Component
@RequiredArgsConstructor
public class ConstructorDetalle {

    private final CotizacionServicioRepository cotizacionServicioRepository;
    private final CotizacionProductoRepository cotizacionProductoRepository;
    private final EmpresaProperties empresa;

    /** Construye las líneas y fija gravado, IGV, total y total en letras. */
    public void construir(Comprobante c, OrdenTrabajo ot, BigDecimal total) {
        BigDecimal divisor = BigDecimal.ONE.add(empresa.getIgv());
        List<ComprobanteDetalle> lineas = new ArrayList<>();
        int item = 1;
        for (var s : cotizacionServicioRepository.findByCotizacionId(ot.getCotizacion().getId())) {
            lineas.add(linea(c, item++, "S" + s.getServicio().getId(), s.getServicio().getNombre(),
                    "ZZ", BigDecimal.ONE, s.getPrecio(), divisor));
        }
        for (var p : cotizacionProductoRepository.findByCotizacionId(ot.getCotizacion().getId())) {
            lineas.add(linea(c, item++, "P" + p.getProducto().getId(), p.getProducto().getNombre(),
                    "NIU", new BigDecimal(p.getCantidadEstimada()), p.getPrecioUnitario(), divisor));
        }
        if (lineas.isEmpty()) {
            throw new BusinessException("La cotización no tiene líneas para facturar.", "ordenTrabajoId",
                    "Agrega servicios o productos a la cotización.");
        }
        BigDecimal gravado = lineas.stream().map(ComprobanteDetalle::getValorVenta)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal igv = total.subtract(gravado).setScale(2, RoundingMode.HALF_UP);
        c.setTotalGravado(gravado);
        c.setIgv(igv);
        c.setTotal(gravado.add(igv));
        c.setTotalLetras(NumeroALetras.convertir(c.getTotal()));
        c.getDetalle().addAll(lineas);
        lineas.forEach(l -> l.setComprobante(c));
    }

    private ComprobanteDetalle linea(Comprobante c, int item, String codigo, String descripcion,
                                     String um, BigDecimal cantidad, BigDecimal precioConIgv, BigDecimal divisor) {
        BigDecimal valorUnitario = precioConIgv.divide(divisor, 4, RoundingMode.HALF_UP);
        BigDecimal valorVenta = valorUnitario.multiply(cantidad).setScale(2, RoundingMode.HALF_UP);
        BigDecimal importe = precioConIgv.multiply(cantidad).setScale(2, RoundingMode.HALF_UP);
        return ComprobanteDetalle.builder()
                .comprobante(c)
                .item(item)
                .codigo(codigo)
                .descripcion(descripcion)
                .unidadMedida(um)
                .cantidad(cantidad.setScale(3, RoundingMode.HALF_UP))
                .valorUnitario(valorUnitario)
                .precioUnitario(precioConIgv.setScale(4, RoundingMode.HALF_UP))
                .tipoAfectacion("10")
                .valorVenta(valorVenta)
                .igv(importe.subtract(valorVenta))
                .importeTotal(importe)
                .build();
    }
}
