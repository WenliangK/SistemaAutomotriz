package com.autogestion.service;

import com.autogestion.dto.DiagnosticoRequest;
import com.autogestion.entity.Diagnostico;
import com.autogestion.entity.Recepcion;
import com.autogestion.entity.Usuario;
import com.autogestion.repository.DiagnosticoRepository;
import com.autogestion.repository.RecepcionRepository;
import com.autogestion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final RecepcionRepository recepcionRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Diagnostico crear(DiagnosticoRequest request) {
        Recepcion recepcion = recepcionRepository.findById(request.getRecepcionId())
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada"));

        Usuario mecanico = usuarioRepository.findById(request.getMecanicoId())
                .orElseThrow(() -> new RuntimeException("Mecánico no encontrado"));

        
        recepcion.setEstado("EN_DIAGNOSTICO");
        recepcionRepository.save(recepcion);

        Diagnostico diagnostico = Diagnostico.builder()
                .recepcion(recepcion)
                .mecanico(mecanico)
                .descripcion(request.getDescripcion())
                .fecha(LocalDateTime.now())
                .build();
        return diagnosticoRepository.save(diagnostico);
    }

    public List<Diagnostico> listarPorRecepcion(Long recepcionId) {
        return diagnosticoRepository.findByRecepcionId(recepcionId);
    }
}
