package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import com.equipo1.netpulse.modelos.EstadoTicket;
import com.equipo1.netpulse.modelos.Ticket;
import com.equipo1.netpulse.repositorios.ITicketRepository;
import com.equipo1.netpulse.servicios.interfaces.IReporteService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReporteService implements IReporteService {

    private final ITicketRepository ticketRepository;

    public ReporteService(ITicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> generarReporte(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoTicket estadoTicket,
            CategoriaIncidencia categoria) {

        // Validar que la fecha inicial no sea posterior a la fecha final
        if (fechaInicio != null && fechaFin != null && fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException(
                    "La fecha inicial no puede ser posterior a la fecha final."
            );
        }

        // Convertir la fecha inicial al comienzo del día
        LocalDateTime fechaInicioDateTime = null;

        if (fechaInicio != null) {
            fechaInicioDateTime = fechaInicio.atStartOfDay();
        }

        // Convertir la fecha final al final del día
        LocalDateTime fechaFinDateTime = null;

        if (fechaFin != null) {
            fechaFinDateTime = fechaFin.atTime(LocalTime.MAX);
        }

        // Obtener los tickets aplicando los filtros seleccionados
        return ticketRepository.buscarParaReporte(
                fechaInicioDateTime,
                fechaFinDateTime,
                estadoTicket,
                categoria
        );
    }
}