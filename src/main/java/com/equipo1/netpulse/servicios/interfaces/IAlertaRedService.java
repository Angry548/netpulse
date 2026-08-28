package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.AlertaRed;

import java.util.List;

public interface IAlertaRedService {

    AlertaRed generar(AlertaRed alerta);

    AlertaRed buscarPorId(Integer id);

    List<AlertaRed> obtenerTodos();

    AlertaRed enviarNotificacion(AlertaRed alerta);

    AlertaRed marcarNotificacionEnviada(AlertaRed alerta);

    void eliminarPorId(Integer id);
}
