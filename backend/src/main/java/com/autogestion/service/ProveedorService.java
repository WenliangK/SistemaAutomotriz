package com.autogestion.service;

import com.autogestion.dto.ProveedorRequest;
import com.autogestion.entity.Proveedor;
import com.autogestion.exception.BusinessException;
import com.autogestion.repository.ProveedorRepository;
import com.autogestion.util.DocumentoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Transactional(readOnly = true)
    public List<Proveedor> listar() {
        return proveedorRepository.findByActivoOrderByNombre(true);
    }

    @Transactional
    public Proveedor crear(ProveedorRequest req) {
        validarRuc(req.getRuc());
        Proveedor p = Proveedor.builder()
                .nombre(req.getNombre().trim())
                .ruc(rucLimpio(req.getRuc()))
                .telefono(vacioANulo(req.getTelefono()))
                .email(vacioANulo(req.getEmail()))
                .build();
        return proveedorRepository.save(p);
    }

    @Transactional
    public Proveedor actualizar(Long id, ProveedorRequest req) {
        Proveedor p = proveedorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Proveedor no encontrado.", null,
                        null, HttpStatus.NOT_FOUND));
        validarRuc(req.getRuc());
        p.setNombre(req.getNombre().trim());
        p.setRuc(rucLimpio(req.getRuc()));
        p.setTelefono(vacioANulo(req.getTelefono()));
        p.setEmail(vacioANulo(req.getEmail()));
        return proveedorRepository.save(p);
    }

    private void validarRuc(String ruc) {
        if (ruc == null || ruc.isBlank()) return;
        if (!DocumentoValidator.rucValido(ruc)) {
            throw new BusinessException(DocumentoValidator.mensajeError(
                    com.autogestion.entity.TipoDocumento.RUC, ruc), "ruc",
                    "Pídelo de su factura o búscalo en SUNAT.");
        }
    }

    private String rucLimpio(String ruc) {
        if (ruc == null || ruc.isBlank()) return null;
        return DocumentoValidator.normalizar(ruc);
    }

    private String vacioANulo(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}