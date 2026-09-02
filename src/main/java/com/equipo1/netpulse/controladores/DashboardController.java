package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.servicios.interfaces.IDashboardService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final IDashboardService dashboardService;

    public DashboardController(
            IDashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }


    /*
     * ============================================================
     * PANEL EJECUTIVO DE KPIs
     * ============================================================
     */

    @GetMapping
    public String mostrarDashboard(

            Model model,

            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate fechaInicio,

            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate fechaFin) {


        /*
         * ========================================================
         * MANTENER FILTROS
         * ========================================================
         */

        model.addAttribute(
                "fechaInicio",
                fechaInicio
        );

        model.addAttribute(
                "fechaFin",
                fechaFin
        );


        try {

            /*
             * ====================================================
             * TOTAL DE TICKETS
             * ====================================================
             */

            long totalTickets =
                    dashboardService.obtenerTotalTickets(
                            fechaInicio,
                            fechaFin
                    );


            /*
             * ====================================================
             * TICKETS POR ESTADO
             * ====================================================
             */

            Map<String, Long> ticketsPorEstado =
                    dashboardService.obtenerTicketsPorEstado(
                            fechaInicio,
                            fechaFin
                    );


            /*
             * ====================================================
             * TICKETS POR CATEGORÍA
             * ====================================================
             */

            Map<String, Long> ticketsPorCategoria =
                    dashboardService.obtenerTicketsPorCategoria(
                            fechaInicio,
                            fechaFin
                    );


            /*
             * ====================================================
             * TICKETS POR PRIORIDAD
             * ====================================================
             */

            Map<String, Long> ticketsPorPrioridad =
                    dashboardService.obtenerTicketsPorPrioridad(
                            fechaInicio,
                            fechaFin
                    );


            /*
             * ====================================================
             * CALCULAR ESTADOS PRINCIPALES
             * ====================================================
             */

            long abiertos =
                    obtenerCantidadPorEstado(
                            ticketsPorEstado,
                            "abierto"
                    );

            long enProceso =
                    obtenerCantidadPorEstado(
                            ticketsPorEstado,
                            "proceso"
                    );

            long cerrados =
                    obtenerCantidadPorEstado(
                            ticketsPorEstado,
                            "cerrado"
                    );


            /*
             * ====================================================
             * ENVIAR INFORMACIÓN A LA VISTA
             * ====================================================
             */

            model.addAttribute(
                    "totalTickets",
                    totalTickets
            );

            model.addAttribute(
                    "ticketsAbiertos",
                    abiertos
            );

            model.addAttribute(
                    "ticketsEnProceso",
                    enProceso
            );

            model.addAttribute(
                    "ticketsCerrados",
                    cerrados
            );

            model.addAttribute(
                    "ticketsPorEstado",
                    ticketsPorEstado
            );

            model.addAttribute(
                    "ticketsPorCategoria",
                    ticketsPorCategoria
            );

            model.addAttribute(
                    "ticketsPorPrioridad",
                    ticketsPorPrioridad
            );

            model.addAttribute(
                    "dashboardGenerado",
                    true
            );


        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "error",
                    e.getMessage()
            );

            model.addAttribute(
                    "dashboardGenerado",
                    false
            );

            model.addAttribute(
                    "totalTickets",
                    0L
            );

            model.addAttribute(
                    "ticketsAbiertos",
                    0L
            );

            model.addAttribute(
                    "ticketsEnProceso",
                    0L
            );

            model.addAttribute(
                    "ticketsCerrados",
                    0L
            );

            model.addAttribute(
                    "ticketsPorEstado",
                    Map.of()
            );

            model.addAttribute(
                    "ticketsPorCategoria",
                    Map.of()
            );

            model.addAttribute(
                    "ticketsPorPrioridad",
                    Map.of()
            );
        }


        return "dashboard/kpis";
    }


    /*
     * ============================================================
     * BUSCAR ESTADO
     * ============================================================
     */

    private long obtenerCantidadPorEstado(
            Map<String, Long> estados,
            String estadoBuscado) {

        String buscado =
                estadoBuscado
                        .toLowerCase(Locale.ROOT)
                        .trim();

        for (Map.Entry<String, Long> entry :
                estados.entrySet()) {

            String nombre =
                    entry.getKey()
                            .toLowerCase(Locale.ROOT)
                            .trim();

            if (nombre.equals(buscado)
                    || nombre.contains(buscado)) {

                return entry.getValue();
            }
        }

        return 0L;
    }

}