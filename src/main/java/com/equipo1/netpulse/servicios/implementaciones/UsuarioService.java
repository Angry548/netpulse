package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Rol;
import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.repositorios.IUsuarioRepository;
import com.equipo1.netpulse.servicios.interfaces.IUsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            IUsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario registrar(Usuario usuario) {

        if (usuario.getContrasena() != null
                && !usuario.getContrasena().isBlank()) {

            usuario.setContrasena(
                    passwordEncoder.encode(
                            usuario.getContrasena()
                    )
            );
        }

        if (usuario.getFechaCreacion() == null) {
            usuario.setFechaCreacion(
                    LocalDateTime.now()
            );
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Integer id) {

        return usuarioRepository
                .findByIdWithRol(id)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorCorreo(String correo) {

        return usuarioRepository
                .findByCorreo(correo)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {

        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> buscarTodosPaginados(Pageable pageable) {

        return usuarioRepository.findAllWithRol(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerPorRol(Rol rol) {

        return usuarioRepository.findByRol(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> buscarPorNombre(
            String nombre,
            Pageable pageable) {

        return usuarioRepository
                .findByNombreContainingIgnoreCaseWithRol(
                        nombre,
                        pageable
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> buscarPorCorreoPaginado(
            String correo,
            Pageable pageable) {

        return usuarioRepository
                .findByCorreoContainingIgnoreCaseWithRol(
                        correo,
                        pageable
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> buscarPorIdPaginado(
            Integer id,
            Pageable pageable) {

        return usuarioRepository
                .findByIdWithRol(
                        id,
                        pageable
                );
    }


    @Override
    public Usuario actualizar(Usuario usuario) {

        if (usuario.getContrasena() != null
                && !usuario.getContrasena().isBlank()
                && !esBCrypt(usuario.getContrasena())) {

            usuario.setContrasena(
                    passwordEncoder.encode(
                            usuario.getContrasena()
                    )
            );
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario cambiarContrasena(Usuario usuario) {

        if (usuario.getContrasena() != null
                && !usuario.getContrasena().isBlank()) {

            usuario.setContrasena(
                    passwordEncoder.encode(
                            usuario.getContrasena()
                    )
            );
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario activar(Usuario usuario) {

        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario desactivar(Usuario usuario) {

        usuario.setActivo(false);

        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarPorId(Integer id) {

        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario registrarAcceso(Usuario usuario) {

        usuario.setUltimoAcceso(
                LocalDateTime.now()
        );

        return usuarioRepository.save(usuario);
    }

    private boolean esBCrypt(String contrasena) {

        return contrasena.matches(
                "^\\$2[aby]\\$\\d{2}\\$.{53}$"
        );
    }
}