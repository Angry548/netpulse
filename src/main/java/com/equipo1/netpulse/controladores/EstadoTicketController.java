package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.EstadoTicket;
import com.equipo1.netpulse.servicios.interfaces.IEstadoTicketService;
import org.springframework.data.domain.Page;
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
@RequestMapping("/estados-ticket")
public class EstadoTicketController {

    private final IEstadoTicketService estadoTicketService;

    public EstadoTicketController(
            IEstadoTicketService estadoTicketService) {

        this.estadoTicketService = estadoTicketService;
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

        Page<EstadoTicket> estados;

        /*
         * Si se busca por ID, obtenemos el registro por ID
         * y lo convertimos a una página de un solo elemento.
         */
        if (id.isPresent()) {

            EstadoTicket estado =
                    estadoTicketService.buscarPorId(id.get());

            if (estado != null) {

                List<EstadoTicket> lista =
                        List.of(estado);

                estados = new org.springframework.data.domain.PageImpl<>(
                        lista,
                        pageable,
                        1
                );

            } else {

                estados = new org.springframework.data.domain.PageImpl<>(
                        List.of(),
                        pageable,
                        0
                );
            }

        } else if (!filtroNombre.isEmpty()) {

            EstadoTicket estado =
                    estadoTicketService.buscarPorNombre(
                            filtroNombre
                    );

            if (estado != null) {

                List<EstadoTicket> lista =
                        List.of(estado);

                estados = new org.springframework.data.domain.PageImpl<>(
                        lista,
                        pageable,
                        1
                );

            } else {

                estados = new org.springframework.data.domain.PageImpl<>(
                        List.of(),
                        pageable,
                        0
                );
            }

        } else {

            estados =
                    estadoTicketService.buscarTodosPaginados(
                            pageable
                    );
        }

        model.addAttribute(
                "estados",
                estados
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
                estados
        );

        return "estados-ticket/index";
    }

    @GetMapping("/create")
    public String create(Model model) {

        EstadoTicket estado =
                new EstadoTicket();

        model.addAttribute(
                "estado",
                estado
        );

        return "estados-ticket/create";
    }

    @PostMapping("/save")
    public String save(
            EstadoTicket estado,
            BindingResult result,
            Model model,
            RedirectAttributes attributes) {

        boolean esEdicion =
                estado.getId() != null;

        /*
         * Validación del formulario.
         */
        if (result.hasErrors()) {

            if (esEdicion) {

                return "estados-ticket/edit";
            }

            return "estados-ticket/create";
        }

        /*
         * Verificamos que no exista otro estado
         * con el mismo nombre.
         */
        EstadoTicket estadoExistente =
                estadoTicketService.buscarPorNombre(
                        estado.getNombre()
                );

        if (estadoExistente != null
                && !estadoExistente.getId().equals(estado.getId())) {

            model.addAttribute(
                    "error",
                    "Ya existe un estado de ticket con ese nombre."
            );

            if (esEdicion) {

                return "estados-ticket/edit";
            }

            return "estados-ticket/create";
        }

        /*
         * ==========================================================
         * EDITAR
         * ==========================================================
         */
        if (esEdicion) {

            EstadoTicket estadoActual =
                    estadoTicketService.buscarPorId(
                            estado.getId()
                    );

            if (estadoActual == null) {

                attributes.addFlashAttribute(
                        "error",
                        "El estado de ticket que intenta editar no existe."
                );

                return "redirect:/estados-ticket";
            }

            estadoActual.setNombre(
                    estado.getNombre()
            );

            estadoActual.setDescripcion(
                    estado.getDescripcion()
            );

            estadoTicketService.actualizar(
                    estadoActual
            );

            attributes.addFlashAttribute(
                    "msg",
                    "Estado de ticket actualizado correctamente"
            );

            return "redirect:/estados-ticket";
        }

        /*
         * ==========================================================
         * CREAR
         * ==========================================================
         */

        estadoTicketService.crear(
                estado
        );

        attributes.addFlashAttribute(
                "msg",
                "Estado de ticket guardado correctamente"
        );

        return "redirect:/estados-ticket";
    }

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        EstadoTicket estado =
                estadoTicketService.buscarPorId(id);

        if (estado == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El estado de ticket no existe."
            );

            return "redirect:/estados-ticket";
        }

        model.addAttribute(
                "estado",
                estado
        );

        return "estados-ticket/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        EstadoTicket estado =
                estadoTicketService.buscarPorId(id);

        if (estado == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El estado de ticket no existe."
            );

            return "redirect:/estados-ticket";
        }

        model.addAttribute(
                "estado",
                estado
        );

        return "estados-ticket/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        EstadoTicket estado =
                estadoTicketService.buscarPorId(id);

        if (estado == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El estado de ticket no existe."
            );

            return "redirect:/estados-ticket";
        }

        model.addAttribute(
                "estado",
                estado
        );

        return "estados-ticket/delete";
    }

    @PostMapping("/delete")
    public String delete(
            EstadoTicket estado,
            RedirectAttributes attributes) {

        EstadoTicket estadoExistente =
                estadoTicketService.buscarPorId(
                        estado.getId()
                );

        if (estadoExistente == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El estado de ticket no existe."
            );

            return "redirect:/estados-ticket";
        }

        estadoTicketService.eliminarPorId(
                estado.getId()
        );

        attributes.addFlashAttribute(
                "msg",
                "Estado de ticket eliminado correctamente"
        );

        return "redirect:/estados-ticket";
    }

    private void agregarNumerosDePagina(
            Model model,
            Page<EstadoTicket> estados) {

        if (estados.getTotalPages() > 0) {

            List<Integer> pageNumbers =
                    IntStream.rangeClosed(
                                    1,
                                    estados.getTotalPages()
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
