package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.PrioridadTicket;
import com.equipo1.netpulse.servicios.interfaces.IPrioridadTicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
@RequestMapping("/prioridades-ticket")
public class PrioridadTicketController {

    private final IPrioridadTicketService prioridadTicketService;

    public PrioridadTicketController(
            IPrioridadTicketService prioridadTicketService) {

        this.prioridadTicketService = prioridadTicketService;
    }

    @GetMapping
    public String index(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("id") Optional<Integer> id,
            @RequestParam("nombre") Optional<String> nombre) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        Pageable pageable =
                PageRequest.of(currentPage, pageSize);

        String filtroNombre =
                nombre.orElse("").trim();

        Page<PrioridadTicket> prioridades;

        /*
         * ==========================================================
         * BUSCAR POR ID
         * ==========================================================
         */
        if (id.isPresent()) {

            PrioridadTicket prioridad =
                    prioridadTicketService.buscarPorId(id.get());

            if (prioridad != null) {

                prioridades = new PageImpl<>(
                        List.of(prioridad),
                        pageable,
                        1
                );

            } else {

                prioridades = new PageImpl<>(
                        List.of(),
                        pageable,
                        0
                );
            }

            /*
             * ==========================================================
             * BUSCAR POR NOMBRE
             * ==========================================================
             */
        } else if (!filtroNombre.isEmpty()) {

            PrioridadTicket prioridad =
                    prioridadTicketService.buscarPorNombre(
                            filtroNombre
                    );

            if (prioridad != null) {

                prioridades = new PageImpl<>(
                        List.of(prioridad),
                        pageable,
                        1
                );

            } else {

                prioridades = new PageImpl<>(
                        List.of(),
                        pageable,
                        0
                );
            }

            /*
             * ==========================================================
             * MOSTRAR TODAS
             * ==========================================================
             */
        } else {

            prioridades =
                    prioridadTicketService.buscarTodosPaginados(
                            pageable
                    );
        }

        model.addAttribute(
                "prioridades",
                prioridades
        );

        model.addAttribute(
                "id",
                id.orElse(null)
        );

        model.addAttribute(
                "nombre",
                filtroNombre
        );

        agregarNumerosDePagina(
                model,
                prioridades
        );

        return "prioridades-ticket/index";
    }

    @GetMapping("/create")
    public String create(Model model) {

        PrioridadTicket prioridad =
                new PrioridadTicket();

        model.addAttribute(
                "prioridad",
                prioridad
        );

        return "prioridades-ticket/create";
    }

    @PostMapping("/save")
    public String save(
            PrioridadTicket prioridad,
            BindingResult result,
            Model model,
            RedirectAttributes attributes) {

        boolean esEdicion =
                prioridad.getId() != null;

        /*
         * ==========================================================
         * VALIDACIÓN
         * ==========================================================
         */
        if (result.hasErrors()) {

            if (esEdicion) {

                return "prioridades-ticket/edit";
            }

            return "prioridades-ticket/create";
        }

        /*
         * ==========================================================
         * VALIDAR NOMBRE DUPLICADO
         * ==========================================================
         */
        PrioridadTicket prioridadExistente =
                prioridadTicketService.buscarPorNombre(
                        prioridad.getNombre()
                );

        if (prioridadExistente != null
                && !prioridadExistente.getId()
                .equals(prioridad.getId())) {

            model.addAttribute(
                    "error",
                    "Ya existe una prioridad con ese nombre."
            );

            if (esEdicion) {

                return "prioridades-ticket/edit";
            }

            return "prioridades-ticket/create";
        }

        /*
         * ==========================================================
         * EDITAR PRIORIDAD
         * ==========================================================
         */
        if (esEdicion) {

            PrioridadTicket prioridadActual =
                    prioridadTicketService.buscarPorId(
                            prioridad.getId()
                    );

            if (prioridadActual == null) {

                attributes.addFlashAttribute(
                        "error",
                        "La prioridad que intenta editar no existe."
                );

                return "redirect:/prioridades-ticket";
            }

            prioridadActual.setNombre(
                    prioridad.getNombre()
            );

            prioridadTicketService.actualizar(
                    prioridadActual
            );

            attributes.addFlashAttribute(
                    "msg",
                    "Prioridad actualizada correctamente"
            );

            return "redirect:/prioridades-ticket";
        }

        /*
         * ==========================================================
         * CREAR PRIORIDAD
         * ==========================================================
         */
        prioridadTicketService.crear(
                prioridad
        );

        attributes.addFlashAttribute(
                "msg",
                "Prioridad guardada correctamente"
        );

        return "redirect:/prioridades-ticket";
    }

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        PrioridadTicket prioridad =
                prioridadTicketService.buscarPorId(id);

        if (prioridad == null) {

            attributes.addFlashAttribute(
                    "error",
                    "La prioridad no existe."
            );

            return "redirect:/prioridades-ticket";
        }

        model.addAttribute(
                "prioridad",
                prioridad
        );

        return "prioridades-ticket/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        PrioridadTicket prioridad =
                prioridadTicketService.buscarPorId(id);

        if (prioridad == null) {

            attributes.addFlashAttribute(
                    "error",
                    "La prioridad no existe."
            );

            return "redirect:/prioridades-ticket";
        }

        model.addAttribute(
                "prioridad",
                prioridad
        );

        return "prioridades-ticket/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        PrioridadTicket prioridad =
                prioridadTicketService.buscarPorId(id);

        if (prioridad == null) {

            attributes.addFlashAttribute(
                    "error",
                    "La prioridad no existe."
            );

            return "redirect:/prioridades-ticket";
        }

        model.addAttribute(
                "prioridad",
                prioridad
        );

        return "prioridades-ticket/delete";
    }

    @PostMapping("/delete")
    public String delete(
            PrioridadTicket prioridad,
            RedirectAttributes attributes) {

        PrioridadTicket prioridadExistente =
                prioridadTicketService.buscarPorId(
                        prioridad.getId()
                );

        if (prioridadExistente == null) {

            attributes.addFlashAttribute(
                    "error",
                    "La prioridad no existe."
            );

            return "redirect:/prioridades-ticket";
        }

        prioridadTicketService.eliminarPorId(
                prioridad.getId()
        );

        attributes.addFlashAttribute(
                "msg",
                "Prioridad eliminada correctamente"
        );

        return "redirect:/prioridades-ticket";
    }

    private void agregarNumerosDePagina(
            Model model,
            Page<PrioridadTicket> prioridades) {

        if (prioridades.getTotalPages() > 0) {

            List<Integer> pageNumbers =
                    IntStream.rangeClosed(
                                    1,
                                    prioridades.getTotalPages()
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