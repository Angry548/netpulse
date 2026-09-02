package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import com.equipo1.netpulse.modelos.EstadoTicket;
import com.equipo1.netpulse.modelos.Ticket;
import com.equipo1.netpulse.repositorios.ITicketRepository;
import com.equipo1.netpulse.servicios.interfaces.IReporteService;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReporteService implements IReporteService {

    private final ITicketRepository ticketRepository;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public ReporteService(ITicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /*
     * ============================================================
     * GENERAR REPORTE
     * ============================================================
     */

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> generarReporte(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoTicket estadoTicket,
            CategoriaIncidencia categoria) {

        validarFechas(fechaInicio, fechaFin);

        LocalDateTime fechaInicioDateTime = null;
        LocalDateTime fechaFinDateTime = null;

        /*
         * La fecha inicial comienza a las 00:00:00
         */
        if (fechaInicio != null) {
            fechaInicioDateTime = fechaInicio.atStartOfDay();
        }

        /*
         * La fecha final termina a las 23:59:59.999999999
         */
        if (fechaFin != null) {
            fechaFinDateTime = fechaFin.atTime(LocalTime.MAX);
        }

        return ticketRepository.buscarParaReporte(
                fechaInicioDateTime,
                fechaFinDateTime,
                estadoTicket,
                categoria
        );
    }


    /*
     * ============================================================
     * GENERAR REPORTE PDF
     * ============================================================
     */

    @Override
    @Transactional(readOnly = true)
    public byte[] generarReportePDF(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoTicket estadoTicket,
            CategoriaIncidencia categoria) {

        /*
         * Obtener exactamente los mismos resultados
         * que se muestran en la pantalla.
         */
        List<Ticket> tickets = generarReporte(
                fechaInicio,
                fechaFin,
                estadoTicket,
                categoria
        );

        LocalDateTime fechaGeneracion = LocalDateTime.now();

        try {

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            Document document = new Document(
                    PageSize.A4.rotate(),
                    25,
                    25,
                    30,
                    30
            );

            PdfWriter.getInstance(
                    document,
                    outputStream
            );

            document.open();


            /*
             * ====================================================
             * FUENTES
             * ====================================================
             */

            Font tituloFont = new Font(
                    Font.HELVETICA,
                    18,
                    Font.BOLD
            );

            Font subtituloFont = new Font(
                    Font.HELVETICA,
                    10,
                    Font.NORMAL
            );

            Font encabezadoFont = new Font(
                    Font.HELVETICA,
                    9,
                    Font.BOLD,
                    Color.WHITE
            );

            Font contenidoFont = new Font(
                    Font.HELVETICA,
                    8,
                    Font.NORMAL
            );

            Font resumenFont = new Font(
                    Font.HELVETICA,
                    10,
                    Font.BOLD
            );


            /*
             * ====================================================
             * TÍTULO
             * ====================================================
             */

            Paragraph titulo = new Paragraph(
                    "NETPULSE",
                    tituloFont
            );

            titulo.setAlignment(Element.ALIGN_CENTER);

            document.add(titulo);


            Paragraph subtitulo = new Paragraph(
                    "Reporte de Incidencias",
                    new Font(
                            Font.HELVETICA,
                            14,
                            Font.BOLD
                    )
            );

            subtitulo.setAlignment(Element.ALIGN_CENTER);

            document.add(subtitulo);


            document.add(
                    new Paragraph(" ")
            );


            /*
             * ====================================================
             * INFORMACIÓN DEL REPORTE
             * ====================================================
             */

            PdfPTable informacion =
                    new PdfPTable(2);

            informacion.setWidthPercentage(100);

            informacion.setWidths(
                    new float[]{50, 50}
            );


            String rangoFechas;

            if (fechaInicio != null && fechaFin != null) {

                rangoFechas =
                        fechaInicio.format(FORMATO_FECHA)
                                + " - "
                                + fechaFin.format(FORMATO_FECHA);

            } else if (fechaInicio != null) {

                rangoFechas =
                        "Desde "
                                + fechaInicio.format(FORMATO_FECHA);

            } else if (fechaFin != null) {

                rangoFechas =
                        "Hasta "
                                + fechaFin.format(FORMATO_FECHA);

            } else {

                rangoFechas = "Todas las fechas";
            }


            String estadoTexto =
                    estadoTicket != null
                            ? estadoTicket.getNombre()
                            : "Todos los estados";


            String categoriaTexto =
                    categoria != null
                            ? categoria.getNombre()
                            : "Todas las categorías";


            informacion.addCell(
                    crearCeldaInformacion(
                            "Fecha del reporte:",
                            fechaGeneracion.format(FORMATO_FECHA_HORA)
                    )
            );

            informacion.addCell(
                    crearCeldaInformacion(
                            "Total de incidencias:",
                            String.valueOf(tickets.size())
                    )
            );

            informacion.addCell(
                    crearCeldaInformacion(
                            "Rango de fechas:",
                            rangoFechas
                    )
            );

            informacion.addCell(
                    crearCeldaInformacion(
                            "Estado:",
                            estadoTexto
                    )
            );

            informacion.addCell(
                    crearCeldaInformacion(
                            "Tipo de incidencia:",
                            categoriaTexto
                    )
            );

            informacion.addCell(
                    crearCeldaInformacion(
                            "Sistema:",
                            "NetPulse - Gestión de Incidencias"
                    )
            );


            document.add(informacion);

            document.add(
                    new Paragraph(" ")
            );


            /*
             * ====================================================
             * TABLA DE INCIDENCIAS
             * ====================================================
             */

            PdfPTable tabla =
                    new PdfPTable(9);

            tabla.setWidthPercentage(100);

            tabla.setWidths(
                    new float[]{
                            5,
                            12,
                            12,
                            12,
                            12,
                            10,
                            10,
                            18,
                            12
                    }
            );


            /*
             * ENCABEZADOS
             */

            agregarEncabezado(
                    tabla,
                    "#",
                    encabezadoFont
            );

            agregarEncabezado(
                    tabla,
                    "Equipo",
                    encabezadoFont
            );

            agregarEncabezado(
                    tabla,
                    "Usuario",
                    encabezadoFont
            );

            agregarEncabezado(
                    tabla,
                    "Técnico",
                    encabezadoFont
            );

            agregarEncabezado(
                    tabla,
                    "Categoría",
                    encabezadoFont
            );

            agregarEncabezado(
                    tabla,
                    "Prioridad",
                    encabezadoFont
            );

            agregarEncabezado(
                    tabla,
                    "Estado",
                    encabezadoFont
            );

            agregarEncabezado(
                    tabla,
                    "Descripción",
                    encabezadoFont
            );

            agregarEncabezado(
                    tabla,
                    "Fecha creación",
                    encabezadoFont
            );


            /*
             * ====================================================
             * FILAS
             * ====================================================
             */

            int contador = 1;

            for (Ticket ticket : tickets) {

                agregarCelda(
                        tabla,
                        String.valueOf(contador),
                        contenidoFont,
                        Element.ALIGN_CENTER
                );


                /*
                 * EQUIPO
                 */
                String equipo = "Sin equipo";

                if (ticket.getEquipo() != null) {
                    equipo = obtenerTexto(
                            ticket.getEquipo().getNombre(),
                            "Sin equipo"
                    );
                }

                agregarCelda(
                        tabla,
                        equipo,
                        contenidoFont,
                        Element.ALIGN_LEFT
                );


                /*
                 * USUARIO
                 */
                String usuario = "Sin usuario";

                if (ticket.getUsuarioReporta() != null) {
                    usuario = obtenerTexto(
                            ticket.getUsuarioReporta().getNombre(),
                            "Sin usuario"
                    );
                }

                agregarCelda(
                        tabla,
                        usuario,
                        contenidoFont,
                        Element.ALIGN_LEFT
                );


                /*
                 * TÉCNICO
                 */
                String tecnico = "Sin asignar";

                if (ticket.getTecnico() != null) {
                    tecnico = obtenerTexto(
                            ticket.getTecnico().getNombre(),
                            "Sin asignar"
                    );
                }

                agregarCelda(
                        tabla,
                        tecnico,
                        contenidoFont,
                        Element.ALIGN_LEFT
                );


                /*
                 * CATEGORÍA
                 */
                String categoriaTextoFila =
                        "Sin categoría";

                if (ticket.getCategoria() != null) {
                    categoriaTextoFila =
                            obtenerTexto(
                                    ticket.getCategoria().getNombre(),
                                    "Sin categoría"
                            );
                }

                agregarCelda(
                        tabla,
                        categoriaTextoFila,
                        contenidoFont,
                        Element.ALIGN_LEFT
                );


                /*
                 * PRIORIDAD
                 */
                String prioridad = "Sin prioridad";

                if (ticket.getPrioridad() != null) {
                    prioridad =
                            obtenerTexto(
                                    ticket.getPrioridad().getNombre(),
                                    "Sin prioridad"
                            );
                }

                agregarCelda(
                        tabla,
                        prioridad,
                        contenidoFont,
                        Element.ALIGN_CENTER
                );


                /*
                 * ESTADO
                 */
                String estado = "Sin estado";

                if (ticket.getEstadoTicket() != null) {
                    estado =
                            obtenerTexto(
                                    ticket.getEstadoTicket().getNombre(),
                                    "Sin estado"
                            );
                }

                agregarCelda(
                        tabla,
                        estado,
                        contenidoFont,
                        Element.ALIGN_CENTER
                );


                /*
                 * DESCRIPCIÓN
                 */
                agregarCelda(
                        tabla,
                        obtenerTexto(
                                ticket.getDescripcion(),
                                "Sin descripción"
                        ),
                        contenidoFont,
                        Element.ALIGN_LEFT
                );


                /*
                 * FECHA
                 */
                String fechaCreacion = "Sin fecha";

                if (ticket.getFechaCreacion() != null) {

                    fechaCreacion =
                            ticket.getFechaCreacion()
                                    .format(FORMATO_FECHA_HORA);
                }

                agregarCelda(
                        tabla,
                        fechaCreacion,
                        contenidoFont,
                        Element.ALIGN_CENTER
                );


                contador++;
            }


            /*
             * ====================================================
             * CUANDO NO EXISTEN RESULTADOS
             * ====================================================
             */

            if (tickets.isEmpty()) {

                PdfPCell celdaSinResultados =
                        new PdfPCell(
                                new Phrase(
                                        "No se encontraron incidencias con los filtros seleccionados.",
                                        contenidoFont
                                )
                        );

                celdaSinResultados.setColspan(9);

                celdaSinResultados.setHorizontalAlignment(
                        Element.ALIGN_CENTER
                );

                celdaSinResultados.setPadding(10);

                tabla.addCell(celdaSinResultados);
            }


            document.add(tabla);


            /*
             * ====================================================
             * TOTAL
             * ====================================================
             */

            document.add(
                    new Paragraph(" ")
            );

            Paragraph total =
                    new Paragraph(
                            "Total de incidencias encontradas: "
                                    + tickets.size(),
                            resumenFont
                    );

            total.setAlignment(
                    Element.ALIGN_RIGHT
            );

            document.add(total);


            /*
             * ====================================================
             * PIE DEL REPORTE
             * ====================================================
             */

            document.add(
                    new Paragraph(" ")
            );

            Paragraph pie =
                    new Paragraph(
                            "Reporte generado automáticamente por NetPulse el "
                                    + fechaGeneracion.format(FORMATO_FECHA_HORA),
                            subtituloFont
                    );

            pie.setAlignment(
                    Element.ALIGN_CENTER
            );

            document.add(pie);


            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "No fue posible generar el reporte PDF.",
                    e
            );
        }
    }


    /*
     * ============================================================
     * VALIDACIÓN DE FECHAS
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


    /*
     * ============================================================
     * CELDA DE INFORMACIÓN
     * ============================================================
     */

    private PdfPCell crearCeldaInformacion(
            String titulo,
            String valor) {

        PdfPCell celda =
                new PdfPCell();

        Paragraph contenido =
                new Paragraph();

        contenido.add(
                new Phrase(
                        titulo + " ",
                        new Font(
                                Font.HELVETICA,
                                9,
                                Font.BOLD
                        )
                )
        );

        contenido.add(
                new Phrase(
                        valor,
                        new Font(
                                Font.HELVETICA,
                                9,
                                Font.NORMAL
                        )
                )
        );

        celda.addElement(contenido);

        celda.setBorder(
                Rectangle.BOX
        );

        celda.setPadding(6);

        return celda;
    }


    /*
     * ============================================================
     * ENCABEZADO DE TABLA
     * ============================================================
     */

    private void agregarEncabezado(
            PdfPTable tabla,
            String texto,
            Font fuente) {

        PdfPCell celda =
                new PdfPCell(
                        new Phrase(
                                texto,
                                fuente
                        )
                );

        celda.setBackgroundColor(
                new Color(52, 58, 64)
        );

        celda.setHorizontalAlignment(
                Element.ALIGN_CENTER
        );

        celda.setVerticalAlignment(
                Element.ALIGN_MIDDLE
        );

        celda.setPadding(5);

        tabla.addCell(celda);
    }


    /*
     * ============================================================
     * CELDA DE CONTENIDO
     * ============================================================
     */

    private void agregarCelda(
            PdfPTable tabla,
            String texto,
            Font fuente,
            int alineacion) {

        PdfPCell celda =
                new PdfPCell(
                        new Phrase(
                                texto,
                                fuente
                        )
                );

        celda.setHorizontalAlignment(
                alineacion
        );

        celda.setVerticalAlignment(
                Element.ALIGN_MIDDLE
        );

        celda.setPadding(4);

        tabla.addCell(celda);
    }


    /*
     * ============================================================
     * TEXTO SEGURO
     * ============================================================
     */

    private String obtenerTexto(
            String texto,
            String valorPorDefecto) {

        if (texto == null || texto.trim().isEmpty()) {
            return valorPorDefecto;
        }

        return texto;
    }
}