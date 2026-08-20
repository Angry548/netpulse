package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    Page<Usuario> obtenerTodosPaginados(Pageable pageable);

    List<Usuario> obtenerTodos();

    Usuario obtenerPorId(Integer id);

    Optional<Usuario> obtenerPorCorreo(String correo);

    Usuario crearOEditar(Usuario usuario);

    void eliminarPorId(Integer id);
}