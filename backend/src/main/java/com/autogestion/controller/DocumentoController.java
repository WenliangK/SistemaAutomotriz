package com.autogestion.controller;

import com.autogestion.entity.TipoDocumento;
import com.autogestion.util.DocumentoValidator;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Usa EL MISMO validador que la emisión: lo que dice este endpoint
 * es lo que el backend va a aceptar. El frontend lo usa para el ✔/✖ en vivo.
 */
@RolesAllowed({"ADMIN", "RECEPCIONISTA", "MECANICO"})
@RestController
@RequestMapping("/api/documentos")
@RequiredArgsConstructor
public class DocumentoController {

    @GetMapping("/validar")
    public ResponseEntity<Map<String, Object>> validar(@RequestParam String tipo,
                                                       @RequestParam String numero) {
        TipoDocumento td = TipoDocumento.desde(tipo);
        boolean valido = td != null && DocumentoValidator.valido(td, numero);
        String mensaje = valido ? tipo.toUpperCase() + " válido."
                : DocumentoValidator.mensajeError(td, numero);
        Map<String, Object> resp = new HashMap<>();
        resp.put("tipo", tipo.toUpperCase());
        resp.put("numero", DocumentoValidator.normalizar(numero));
        resp.put("valido", valido);
        resp.put("mensaje", mensaje);
        if (td == TipoDocumento.RUC && !valido) {
            int dv = DocumentoValidator.digitoVerificadorRuc(numero);
            if (dv >= 0) resp.put("digitoEsperado", dv);
        }
        return ResponseEntity.ok(resp);
    }
}
