package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.AlertaRed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAlertaRedService {

    List<AlertaRed> obtenerTodos();

    Page<AlertaRed> buscarTodosPaginados(Pageable pageable);

    AlertaRed buscarPorId(Integer id);

    AlertaRed crearOEditar(AlertaRed alertaRed);

    void eliminarPorId(Integer id);
}
