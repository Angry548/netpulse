package com.equipo1.netpulse.servicios.interfaces;

import java.time.LocalDate;
import java.util.Map;

public interface IDashboardService {

    long obtenerTotalTickets(
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    Map<String, Long> obtenerTicketsPorEstado(
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    Map<String, Long> obtenerTicketsPorCategoria(
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    Map<String, Long> obtenerTicketsPorPrioridad(
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

}