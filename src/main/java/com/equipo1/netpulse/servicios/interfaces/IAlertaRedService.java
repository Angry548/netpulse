package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.AlertaRed;
import com.equipo1.netpulse.modelos.Equipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAlertaRedService {

    AlertaRed generar(AlertaRed alerta);

    AlertaRed buscarPorId(Integer id);

    List<AlertaRed> obtenerTodos();

    Page<AlertaRed> buscarTodosPaginados(Pageable pageable);

    List<AlertaRed> obtenerPorEquipo(Equipo equipo);

    List<AlertaRed> obtenerNoNotificadas();

    List<AlertaRed> obtenerPorTipoEvento(String tipoEvento);

    AlertaRed enviarNotificacion(AlertaRed alerta);

    AlertaRed marcarNotificacionEnviada(AlertaRed alerta);

    void eliminarPorId(Integer id);
}

