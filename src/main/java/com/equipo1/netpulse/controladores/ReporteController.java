package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import com.equipo1.netpulse.modelos.EstadoTicket;
import com.equipo1.netpulse.modelos.Ticket;
import com.equipo1.netpulse.repositorios.ICategoriaIncidenciaRepository;
import com.equipo1.netpulse.repositorios.IEstadoTicketRepository;
import com.equipo1.netpulse.servicios.interfaces.IReporteService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final IReporteService reporteService;
    private final ICategoriaIncidenciaRepository categoriaRepository;
    private final IEstadoTicketRepository estadoTicketRepository;

    public ReporteController(
            IReporteService reporteService,
            ICategoriaIncidenciaRepository categoriaRepository,
            IEstadoTicketRepository estadoTicketRepository) {

        this.reporteService = reporteService;
        this.categoriaRepository = categoriaRepository;
        this.estadoTicketRepository = estadoTicketRepository;
    }

    /**
     * Muestra la pantalla del generador de reportes de incidencias.
     */
    @GetMapping("/incidencias")
    public String mostrarReporte(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaFin,

            @RequestParam(required = false)
            Integer estadoId,

            @RequestParam(required = false)
            Integer categoriaId,

            Model model) {

        // Cargar catálogos para los filtros
        model.addAttribute("estados", estadoTicketRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());

        // Mantener los valores seleccionados en el formulario
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("estadoId", estadoId);
        model.addAttribute("categoriaId", categoriaId);

        // Si no se solicitaron filtros, solamente mostrar el formulario
        boolean generarReporte =
                fechaInicio != null ||
                        fechaFin != null ||
                        estadoId != null ||
                        categoriaId != null;

        if (!generarReporte) {
            return "reportes/incidencias";
        }

        try {

            // Buscar el estado seleccionado
            EstadoTicket estadoTicket = null;

            if (estadoId != null) {
                estadoTicket = estadoTicketRepository.findById(estadoId).orElse(null);
            }

            // Buscar la categoría seleccionada
            CategoriaIncidencia categoria = null;

            if (categoriaId != null) {
                categoria = categoriaRepository.findById(categoriaId).orElse(null);
            }

            // Generar el reporte
            List<Ticket> tickets = reporteService.generarReporte(
                    fechaInicio,
                    fechaFin,
                    estadoTicket,
                    categoria
            );

            // Agregar resultados al modelo
            model.addAttribute("tickets", tickets);
            model.addAttribute("totalIncidencias", tickets.size());

            // Registrar fecha y hora de generación
            model.addAttribute("fechaGeneracion", LocalDateTime.now());

            // Indicar que el reporte fue generado
            model.addAttribute("reporteGenerado", true);

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());
            model.addAttribute("reporteGenerado", false);

        }

        return "reportes/incidencias";
    }
}