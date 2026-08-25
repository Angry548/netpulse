package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Usuario;

import java.util.List;

public interface IUsuarioService {

    Usuario registrar(Usuario usuario);

    Usuario buscarPorId(Integer id);

    Usuario buscarPorCorreo(String correo);

    List<Usuario> obtenerTodos();

    Usuario actualizar(Usuario usuario);

    Usuario cambiarContrasena(Usuario usuario);

    Usuario activar(Usuario usuario);

    Usuario desactivar(Usuario usuario);

    void eliminarPorId(Integer id);

    Usuario registrarAcceso(Usuario usuario);
}