package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Rol;
import com.equipo1.netpulse.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUsuarioService {

    Usuario registrar(Usuario usuario);

    Usuario buscarPorId(Integer id);

    Usuario buscarPorCorreo(String correo);

    List<Usuario> obtenerTodos();

    Page<Usuario> buscarTodosPaginados(Pageable pageable);

    List<Usuario> obtenerPorRol(Rol rol);

    Usuario actualizar(Usuario usuario);

    Usuario cambiarContrasena(Usuario usuario);

    Usuario activar(Usuario usuario);

    Usuario desactivar(Usuario usuario);

    void eliminarPorId(Integer id);

    Usuario registrarAcceso(Usuario usuario);
}