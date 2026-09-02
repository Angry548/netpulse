package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import com.equipo1.netpulse.modelos.EstadoTicket;
import com.equipo1.netpulse.modelos.Ticket;

import com.equipo1.netpulse.repositorios.ICategoriaIncidenciaRepository;
import com.equipo1.netpulse.repositorios.IEstadoTicketRepository;

import com.equipo1.netpulse.servicios.interfaces.IReporteService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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


    /*
     * ============================================================
     * REPORTE DE INCIDENCIAS
     * ============================================================
     */

    @GetMapping
    public String index() {
        return "redirect:/reportes/incidencias";
    }

    @GetMapping("/incidencias")
    public String mostrarReporte(

            Model model,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaFin,

            @RequestParam(required = false)
            Integer estadoId,

            @RequestParam(required = false)
            Integer categoriaId) {


        /*
         * ========================================================
         * CARGAR CATÁLOGOS
         * ========================================================
         */

        model.addAttribute(
                "estados",
                estadoTicketRepository.findAll()
        );

        model.addAttribute(
                "categorias",
                categoriaRepository.findAll()
        );


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

        model.addAttribute(
                "estadoId",
                estadoId
        );

        model.addAttribute(
                "categoriaId",
                categoriaId
        );


        /*
         * ========================================================
         * DETERMINAR SI SE DEBE GENERAR EL REPORTE
         * ========================================================
         */

        boolean generarReporte =
                fechaInicio != null
                        || fechaFin != null
                        || estadoId != null
                        || categoriaId != null;


        if (!generarReporte) {

            model.addAttribute(
                    "reporteGenerado",
                    false
            );

            return "reportes/incidencias";
        }


        try {

            /*
             * ====================================================
             * OBTENER ESTADO
             * ====================================================
             */

            EstadoTicket estadoTicket = null;

            if (estadoId != null) {

                estadoTicket =
                        estadoTicketRepository
                                .findById(estadoId)
                                .orElse(null);
            }


            /*
             * ====================================================
             * OBTENER CATEGORÍA
             * ====================================================
             */

            CategoriaIncidencia categoria = null;

            if (categoriaId != null) {

                categoria =
                        categoriaRepository
                                .findById(categoriaId)
                                .orElse(null);
            }


            /*
             * ====================================================
             * GENERAR REPORTE
             * ====================================================
             */

            List<Ticket> tickets =
                    reporteService.generarReporte(
                            fechaInicio,
                            fechaFin,
                            estadoTicket,
                            categoria
                    );


            /*
             * ====================================================
             * ENVIAR RESULTADOS A LA VISTA
             * ====================================================
             */

            model.addAttribute(
                    "tickets",
                    tickets
            );

            model.addAttribute(
                    "totalIncidencias",
                    tickets.size()
            );


            /*
             * ====================================================
             * FECHA Y HORA DE GENERACIÓN
             * ====================================================
             */

            model.addAttribute(
                    "fechaGeneracion",
                    LocalDateTime.now()
            );


            model.addAttribute(
                    "reporteGenerado",
                    true
            );


        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "error",
                    e.getMessage()
            );

            model.addAttribute(
                    "reporteGenerado",
                    false
            );
        }


        return "reportes/incidencias";
    }


    /*
     * ============================================================
     * EXPORTAR REPORTE PDF
     * ============================================================
     */

    @GetMapping("/incidencias/pdf")
    public ResponseEntity<byte[]> exportarPDF(

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaInicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaFin,

            @RequestParam(required = false)
            Integer estadoId,

            @RequestParam(required = false)
            Integer categoriaId) {


        try {

            /*
             * ====================================================
             * OBTENER ESTADO
             * ====================================================
             */

            EstadoTicket estadoTicket = null;

            if (estadoId != null) {

                estadoTicket =
                        estadoTicketRepository
                                .findById(estadoId)
                                .orElse(null);
            }


            /*
             * ====================================================
             * OBTENER CATEGORÍA
             * ====================================================
             */

            CategoriaIncidencia categoria = null;

            if (categoriaId != null) {

                categoria =
                        categoriaRepository
                                .findById(categoriaId)
                                .orElse(null);
            }


            /*
             * ====================================================
             * GENERAR PDF
             * ====================================================
             */

            byte[] pdf =
                    reporteService.generarReportePDF(
                            fechaInicio,
                            fechaFin,
                            estadoTicket,
                            categoria
                    );


            /*
             * ====================================================
             * NOMBRE DEL ARCHIVO
             * ====================================================
             */

            String nombreArchivo =
                    "reporte-incidencias-"
                            + LocalDate.now()
                            + ".pdf";


            /*
             * ====================================================
             * HEADERS
             * ====================================================
             */

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_PDF
            );

            headers.setContentDisposition(
                    ContentDisposition
                            .attachment()
                            .filename(nombreArchivo)
                            .build()
            );

            headers.setContentLength(
                    pdf.length
            );


            /*
             * ====================================================
             * RESPUESTA
             * ====================================================
             */

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(pdf);


        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .build();

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }
}
