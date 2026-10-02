package com.autogestion.service;

import com.autogestion.dto.GastoRequest;
import com.autogestion.dto.GastoResponseDTO;
import com.autogestion.entity.CategoriaGasto;
import com.autogestion.entity.Gasto;
import com.autogestion.entity.Proveedor;
import com.autogestion.entity.Usuario;
import com.autogestion.exception.BusinessException;
import com.autogestion.repository.GastoRepository;
import com.autogestion.repository.ProveedorRepository;
import com.autogestion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import com.autogestion.util.AppTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GastoService {

    private final GastoRepository gastoRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public GastoResponseDTO crear(GastoRequest req, Long usuarioId) {
        CategoriaGasto cat = CategoriaGasto.desde(req.getCategoria());
        if (cat == null) {
            throw new BusinessException("Categoría inválida.", "categoria",
                    "Usa alquiler, servicios, sueldos, herramientas, limpieza, transporte u otros.");
        }
        Proveedor prov = null;
        if (req.getProveedorId() != null) {
            prov = proveedorRepository.findById(req.getProveedorId())
                    .orElseThrow(() -> new BusinessException("Proveedor no encontrado.", "proveedorId",
                            null, HttpStatus.NOT_FOUND));
        }
        Usuario u = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Sesión inválida.", null,
                        null, HttpStatus.UNAUTHORIZED));
        Gasto g = Gasto.builder()
                .fecha(req.getFecha() != null ? req.getFecha() : com.autogestion.util.AppTime.hoy())
                .categoria(cat)
                .descripcion(req.getDescripcion().trim())
                .monto(req.getMonto())
                .proveedor(prov)
                .documentoRef(req.getDocumentoRef() != null && !req.getDocumentoRef().isBlank()
                        ? req.getDocumentoRef().trim() : null)
                .registradoPor(u)
                .build();
        return toDTO(gastoRepository.save(g));
    }

    @Transactional(readOnly = true)
    public Page<GastoResponseDTO> listar(String categoriaCodigo, LocalDate desde, LocalDate hasta, Pageable pageable) {
        CategoriaGasto cat = categoriaCodigo != null && !categoriaCodigo.isBlank()
                ? CategoriaGasto.desde(categoriaCodigo) : null;
        return gastoRepository.filtrar(cat, desde, hasta, pageable).map(this::toDTO);
    }

    private GastoResponseDTO toDTO(Gasto g) {
        return GastoResponseDTO.builder()
                .id(g.getId())
                .fecha(g.getFecha())
                .categoria(g.getCategoria().name())
                .categoriaEtiqueta(g.getCategoria().getEtiqueta())
                .descripcion(g.getDescripcion())
                .monto(g.getMonto())
                .proveedorId(g.getProveedor() != null ? g.getProveedor().getId() : null)
                .proveedorNombre(g.getProveedor() != null ? g.getProveedor().getNombre() : null)
                .documentoRef(g.getDocumentoRef())
                .registradoPorNombre(g.getRegistradoPor() != null ? g.getRegistradoPor().getNombreCompleto() : null)
                .build();
    }
}
