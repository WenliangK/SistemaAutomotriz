package com.autogestion.service;

import com.autogestion.dto.MecanicoResumenDTO;
import com.autogestion.dto.UsuarioRequest;
import com.autogestion.dto.UsuarioResponseDTO;
import com.autogestion.entity.Especialidad;
import com.autogestion.entity.EstadoOT;
import com.autogestion.entity.TipoDocumento;
import com.autogestion.entity.Usuario;
import com.autogestion.exception.BusinessException;
import com.autogestion.repository.OrdenTrabajoRepository;
import com.autogestion.repository.UsuarioRepository;
import com.autogestion.util.DocumentoValidator;
import com.autogestion.util.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Set<String> ROLES = Set.of("ADMIN", "MECANICO", "ALMACENERO", "RECEPCIONISTA");

    private final UsuarioRepository usuarioRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDTO crear(UsuarioRequest req) {
        String rol = rolValido(req.getRol());
        if (usuarioRepository.existsByEmail(req.getEmail().trim())) {
            throw new BusinessException("Ese email ya está registrado.", "email",
                    "Usa otro correo.", HttpStatus.CONFLICT);
        }
        if (req.getPassword() == null || req.getPassword().length() < 8) {
            throw new BusinessException("La contraseña inicial debe tener al menos 8 caracteres.", "password",
                    "El usuario la podrá cambiar luego.");
        }
        TipoDocumento tipo = tipoDoc(req);
        String doc = docNormalizado(req);
        if (doc != null && usuarioRepository.existsByTipoDocumentoAndDocumento(tipo, doc)) {
            throw new BusinessException("Ese documento ya está registrado.", "documento",
                    "Revisa el número.", HttpStatus.CONFLICT);
        }
        Usuario u = Usuario.builder()
                .nombre(completo(req.getNombres(), req.getApellidos()))
                .nombres(req.getNombres().trim())
                .apellidos(req.getApellidos().trim())
                .tipoDocumento(tipo)
                .documento(doc)
                .telefono(vacioANulo(req.getTelefono()))
                .email(req.getEmail().trim())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .rol(rol)
                .especialidad(Especialidad.desde(req.getEspecialidad()))
                .activo(true)
                .build();
        return toDTO(usuarioRepository.save(u));
    }

    @Transactional
    public UsuarioResponseDTO actualizar(Long id, UsuarioRequest req) {
        Usuario u = obtenerEntidad(id);
        String rol = rolValido(req.getRol());
        if (!u.getEmail().equalsIgnoreCase(req.getEmail().trim())
                && usuarioRepository.existsByEmail(req.getEmail().trim())) {
            throw new BusinessException("Ese email ya está registrado.", "email",
                    "Usa otro correo.", HttpStatus.CONFLICT);
        }
        if ("ADMIN".equals(u.getRol()) && !"ADMIN".equals(rol)) {
            exigirQueQuedeAdmin(u.getId(), rol);
        }
        TipoDocumento tipo = tipoDoc(req);
        String doc = docNormalizado(req);
        u.setNombre(completo(req.getNombres(), req.getApellidos()));
        u.setNombres(req.getNombres().trim());
        u.setApellidos(req.getApellidos().trim());
        u.setTipoDocumento(tipo);
        u.setDocumento(doc);
        u.setTelefono(vacioANulo(req.getTelefono()));
        u.setEmail(req.getEmail().trim());
        u.setRol(rol);
        u.setEspecialidad(Especialidad.desde(req.getEspecialidad()));
        return toDTO(usuarioRepository.save(u));
    }

    /** Activa/desactiva sin borrar. Protege: a ti mismo, al último ADMIN y al mecánico con OT en curso. */
    @Transactional
    public UsuarioResponseDTO cambiarActivo(Long id, boolean activo, Long solicitanteId) {
        Usuario u = obtenerEntidad(id);
        if (!activo && u.getId().equals(solicitanteId)) {
            throw new BusinessException("No puedes desactivarte a ti mismo.", null,
                    "Pide a otro administrador que lo haga.", HttpStatus.CONFLICT);
        }
        if (!activo && "ADMIN".equals(u.getRol())) {
            exigirQueQuedeAdmin(u.getId(), "OTRO");
        }
        if (!activo && "MECANICO".equals(u.getRol())) {
            long enCurso = ordenTrabajoRepository.countByMecanicoIdAndEstadoNotIn(
                    u.getId(), List.of(EstadoOT.FINALIZADA, EstadoOT.CANCELADA));
            if (enCurso > 0) {
                throw new BusinessException("Tiene " + enCurso + " OT en proceso.", null,
                        "Reasigna sus órdenes antes de desactivarlo.", HttpStatus.CONFLICT);
            }
        }
        u.setActivo(activo);
        return toDTO(usuarioRepository.save(u));
    }

    @Transactional
    public void restablecerClave(Long id, String nueva) {
        Usuario u = obtenerEntidad(id);
        if (nueva == null || nueva.length() < 8) {
            throw new BusinessException("La nueva clave debe tener al menos 8 caracteres.", "nuevaClave",
                    "Anótala y entrégala al usuario.");
        }
        u.setPasswordHash(passwordEncoder.encode(nueva));
        usuarioRepository.save(u);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> listar(String rol, Boolean activo, String q, Pageable pageable) {
        return usuarioRepository.filtrar(
                (rol == null || rol.isBlank()) ? null : rol.trim().toUpperCase(),
                activo, Like.patron(q), pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<MecanicoResumenDTO> mecanicos(boolean soloActivos) {
        LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        return usuarioRepository.findByRolAndActivo("MECANICO", soloActivos).stream()
                .map(u -> MecanicoResumenDTO.builder()
                        .id(u.getId())
                        .nombreCompleto(u.getNombreCompleto())
                        .especialidad(u.getEspecialidad() != null ? u.getEspecialidad().name() : null)
                        .especialidadEtiqueta(u.getEspecialidad() != null ? u.getEspecialidad().getEtiqueta() : null)
                        .otActivas(ordenTrabajoRepository.countByMecanicoIdAndEstadoNotIn(
                                u.getId(), List.of(EstadoOT.FINALIZADA, EstadoOT.CANCELADA)))
                        .otFinalizadasMes(ordenTrabajoRepository.countFinalizadasDesde(u.getId(), inicioMes))
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO yo(Long id) {
        return toDTO(obtenerEntidad(id));
    }

    private void exigirQueQuedeAdmin(Long afectadoId, String rolNuevo) {
        boolean afectadoEsAdmin = usuarioRepository.findById(afectadoId)
                .map(u -> "ADMIN".equals(u.getRol())).orElse(false);
        boolean quedariaSinAdmin = afectadoEsAdmin && !"ADMIN".equals(rolNuevo)
                && usuarioRepository.countByRolAndActivo("ADMIN", true) <= 1;
        if (quedariaSinAdmin) {
            throw new BusinessException("Es el último administrador activo.", "rol",
                    "Nombra otro ADMIN primero.", HttpStatus.CONFLICT);
        }
    }

    private String rolValido(String rol) {
        if (rol == null || !ROLES.contains(rol.trim().toUpperCase())) {
            throw new BusinessException("Rol inválido.", "rol",
                    "Usa ADMIN, MECANICO, ALMACENERO o RECEPCIONISTA.");
        }
        return rol.trim().toUpperCase();
    }

    private TipoDocumento tipoDoc(UsuarioRequest req) {
        if (req.getTipoDocumento() == null || req.getTipoDocumento().isBlank()) return TipoDocumento.DNI;
        TipoDocumento td = TipoDocumento.desde(req.getTipoDocumento());
        if (td == null) throw new BusinessException("Tipo de documento inválido.", "tipoDocumento",
                "Usa DNI, RUC, CE o PASAPORTE.");
        return td;
    }

    private String docNormalizado(UsuarioRequest req) {
        if (req.getDocumento() == null || req.getDocumento().isBlank()) return null;
        return DocumentoValidator.normalizar(req.getDocumento());
    }

    private String completo(String nombres, String apellidos) {
        return (nombres.trim() + " " + apellidos.trim()).trim();
    }

    private String vacioANulo(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private Usuario obtenerEntidad(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado.", null,
                        null, HttpStatus.NOT_FOUND));
    }

    public UsuarioResponseDTO toDTO(Usuario u) {
        return UsuarioResponseDTO.builder()
                .id(u.getId())
                .nombreCompleto(u.getNombreCompleto())
                .nombres(u.getNombres())
                .apellidos(u.getApellidos())
                .iniciales(u.getIniciales())
                .tipoDocumento(u.getTipoDocumento() != null ? u.getTipoDocumento().name() : null)
                .documento(u.getDocumento())
                .telefono(u.getTelefono())
                .email(u.getEmail())
                .rol(u.getRol())
                .especialidad(u.getEspecialidad() != null ? u.getEspecialidad().name() : null)
                .especialidadEtiqueta(u.getEspecialidad() != null ? u.getEspecialidad().getEtiqueta() : null)
                .activo(u.getActivo())
                .fechaIngreso(u.getFechaIngreso())
                .build();
    }
}
