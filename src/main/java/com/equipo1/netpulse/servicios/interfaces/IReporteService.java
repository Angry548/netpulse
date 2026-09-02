package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import com.equipo1.netpulse.modelos.EstadoTicket;
import com.equipo1.netpulse.modelos.Ticket;

import java.time.LocalDate;
import java.util.List;

public interface IReporteService {

    List<Ticket> generarReporte(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoTicket estadoTicket,
            CategoriaIncidencia categoria
    );

    byte[] generarReportePDF(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoTicket estadoTicket,
            CategoriaIncidencia categoria
    );
}