package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.repositorios.ITicketRepository;
import com.equipo1.netpulse.servicios.interfaces.IDashboardService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class DashboardService implements IDashboardService {

    private final ITicketRepository ticketRepository;

    public DashboardService(ITicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }


    /*
     * ============================================================
     * TOTAL DE TICKETS
     * ============================================================
     */

    @Override
    @Transactional(readOnly = true)
    public long obtenerTotalTickets(
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        validarFechas(fechaInicio, fechaFin);

        LocalDateTime inicio = convertirFechaInicio(fechaInicio);
        LocalDateTime fin = convertirFechaFin(fechaFin);

        return ticketRepository.contarTicketsParaDashboard(
                inicio,
                fin
        );
    }


    /*
     * ============================================================
     * TICKETS POR ESTADO
     * ============================================================
     */

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> obtenerTicketsPorEstado(
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        validarFechas(fechaInicio, fechaFin);

        LocalDateTime inicio = convertirFechaInicio(fechaInicio);
        LocalDateTime fin = convertirFechaFin(fechaFin);

        List<Object[]> resultados =
                ticketRepository.contarTicketsPorEstadoParaDashboard(
                        inicio,
                        fin
                );

        Map<String, Long> estados =
                new LinkedHashMap<>();

        for (Object[] resultado : resultados) {

            String nombre =
                    String.valueOf(resultado[0]);

            Long cantidad =
                    ((Number) resultado[1]).longValue();

            estados.put(
                    nombre,
                    cantidad
            );
        }

        return estados;
    }


    /*
     * ============================================================
     * TICKETS POR CATEGORÍA
     * ============================================================
     */

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> obtenerTicketsPorCategoria(
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        validarFechas(fechaInicio, fechaFin);

        LocalDateTime inicio = convertirFechaInicio(fechaInicio);
        LocalDateTime fin = convertirFechaFin(fechaFin);

        List<Object[]> resultados =
                ticketRepository.contarTicketsPorCategoriaParaDashboard(
                        inicio,
                        fin
                );

        Map<String, Long> categorias =
                new LinkedHashMap<>();

        for (Object[] resultado : resultados) {

            String nombre =
                    String.valueOf(resultado[0]);

            Long cantidad =
                    ((Number) resultado[1]).longValue();

            categorias.put(
                    nombre,
                    cantidad
            );
        }

        return categorias;
    }


    /*
     * ============================================================
     * TICKETS POR PRIORIDAD
     * ============================================================
     */

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> obtenerTicketsPorPrioridad(
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        validarFechas(fechaInicio, fechaFin);

        LocalDateTime inicio = convertirFechaInicio(fechaInicio);
        LocalDateTime fin = convertirFechaFin(fechaFin);

        List<Object[]> resultados =
                ticketRepository.contarTicketsPorPrioridadParaDashboard(
                        inicio,
                        fin
                );

        Map<String, Long> prioridades =
                new LinkedHashMap<>();

        for (Object[] resultado : resultados) {

            String nombre =
                    String.valueOf(resultado[0]);

            Long cantidad =
                    ((Number) resultado[1]).longValue();

            prioridades.put(
                    nombre,
                    cantidad
            );
        }

        return prioridades;
    }


    /*
     * ============================================================
     * CONVERSIÓN DE FECHAS
     * ============================================================
     */

    private LocalDateTime convertirFechaInicio(
            LocalDate fecha) {

        if (fecha == null) {
            return null;
        }

        return fecha.atStartOfDay();
    }


    private LocalDateTime convertirFechaFin(
            LocalDate fecha) {

        if (fecha == null) {
            return null;
        }

        return fecha.atTime(LocalTime.MAX);
    }


    /*
     * ============================================================
     * VALIDACIÓN
     * ============================================================
     */

    private void validarFechas(
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        if (fechaInicio != null
                && fechaFin != null
                && fechaInicio.isAfter(fechaFin)) {

            throw new IllegalArgumentException(
                    "La fecha inicial no puede ser posterior a la fecha final."
            );
        }
    }

}
