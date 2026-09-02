package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.AlertaRed;
import com.equipo1.netpulse.servicios.interfaces.IAlertaRedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/alertas-red")
public class AlertaRedController {

    private final IAlertaRedService alertaRedService;

    public AlertaRedController(
            IAlertaRedService alertaRedService) {

        this.alertaRedService = alertaRedService;
    }

    /*
     * ==========================================================
     * LISTADO
     * ==========================================================
     */

    @GetMapping
    public String index(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("id") Optional<Integer> id,
            @RequestParam("tipoEvento") Optional<String> tipoEvento) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        Pageable pageable =
                PageRequest.of(
                        currentPage,
                        pageSize
                );

        String filtroTipoEvento =
                tipoEvento.orElse("").trim();

        Page<AlertaRed> alertasRed;

        if (id.isPresent()) {

            alertasRed =
                    alertaRedService.buscarPorIdPaginado(
                            id.get(),
                            pageable
                    );

        } else if (!filtroTipoEvento.isEmpty()) {

            alertasRed =
                    alertaRedService.buscarPorTipoEvento(
                            filtroTipoEvento,
                            pageable
                    );

        } else {

            alertasRed =
                    alertaRedService.buscarTodosPaginados(
                            pageable
                    );
        }

        model.addAttribute(
                "alertasRed",
                alertasRed
        );

        model.addAttribute(
                "id",
                id.orElse(null)
        );

        model.addAttribute(
                "tipoEvento",
                filtroTipoEvento
        );

        agregarNumerosDePagina(
                model,
                alertasRed
        );

        return "alertas-red/index";
    }

    /*
     * ==========================================================
     * DETALLES
     * ==========================================================
     */

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        AlertaRed alerta =
                alertaRedService.buscarPorId(id);

        if (alerta == null) {

            attributes.addFlashAttribute(
                    "error",
                    "La alerta de red solicitada no existe."
            );

            return "redirect:/alertas-red";
        }

        model.addAttribute(
                "alerta",
                alerta
        );

        return "alertas-red/details";
    }

    /*
     * ==========================================================
     * ELIMINAR - CONFIRMACIÓN
     * ==========================================================
     */

    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        AlertaRed alerta =
                alertaRedService.buscarPorId(id);

        if (alerta == null) {

            attributes.addFlashAttribute(
                    "error",
                    "La alerta de red que intenta eliminar no existe."
            );

            return "redirect:/alertas-red";
        }

        model.addAttribute(
                "alerta",
                alerta
        );

        return "alertas-red/delete";
    }

    /*
     * ==========================================================
     * ELIMINAR
     * ==========================================================
     */

    @PostMapping("/delete")
    public String delete(
            AlertaRed alerta,
            RedirectAttributes attributes) {

        if (alerta.getId() == null) {

            attributes.addFlashAttribute(
                    "error",
                    "No se pudo identificar la alerta de red."
            );

            return "redirect:/alertas-red";
        }

        AlertaRed alertaExistente =
                alertaRedService.buscarPorId(
                        alerta.getId()
                );

        if (alertaExistente == null) {

            attributes.addFlashAttribute(
                    "error",
                    "La alerta de red que intenta eliminar no existe."
            );

            return "redirect:/alertas-red";
        }

        alertaRedService.eliminarPorId(
                alerta.getId()
        );

        attributes.addFlashAttribute(
                "msg",
                "Alerta de red eliminada correctamente"
        );

        return "redirect:/alertas-red";
    }

    /*
     * ==========================================================
     * PAGINACIÓN
     * ==========================================================
     */

    private void agregarNumerosDePagina(
            Model model,
            Page<AlertaRed> alertasRed) {

        if (alertasRed.getTotalPages() > 0) {

            List<Integer> pageNumbers =
                    IntStream.rangeClosed(
                                    1,
                                    alertasRed.getTotalPages()
                            )
                            .boxed()
                            .collect(Collectors.toList());

            model.addAttribute(
                    "pageNumbers",
                    pageNumbers
            );
        }
    }
}