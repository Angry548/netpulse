package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.EstadoEquipo;
import com.equipo1.netpulse.servicios.interfaces.IEstadoEquipoService;
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
@RequestMapping("/estados-equipo")
public class EstadoEquipoController {

    private final IEstadoEquipoService estadoEquipoService;

    public EstadoEquipoController(
            IEstadoEquipoService estadoEquipoService) {

        this.estadoEquipoService = estadoEquipoService;
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

        Pageable pageable = PageRequest.of(
                currentPage,
                pageSize
        );

        String filtroNombre =
                nombre.orElse("").trim();

        Page<EstadoEquipo> estadosEquipo;

        if (id.isPresent()) {

            estadosEquipo =
                    estadoEquipoService.buscarPorIdPaginado(
                            id.get(),
                            pageable
                    );

        } else if (!filtroNombre.isEmpty()) {

            estadosEquipo =
                    estadoEquipoService.buscarPorNombre(
                            filtroNombre,
                            pageable
                    );

        } else {

            estadosEquipo =
                    estadoEquipoService.buscarTodosPaginados(
                            pageable
                    );
        }

        model.addAttribute(
                "estadosEquipo",
                estadosEquipo
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
                estadosEquipo
        );

        return "estados-equipo/index";
    }

    @GetMapping("/create")
    public String create(
            Model model) {

        EstadoEquipo estadoEquipo =
                new EstadoEquipo();

        model.addAttribute(
                "estadoEquipo",
                estadoEquipo
        );

        return "estados-equipo/create";
    }

    @PostMapping("/save")
    public String save(
            EstadoEquipo estadoEquipo,
            BindingResult result,
            Model model,
            RedirectAttributes attributes) {

        boolean esEdicion =
                estadoEquipo.getId() != null;

        if (result.hasErrors()) {

            if (esEdicion) {
                return "estados-equipo/edit";
            }

            return "estados-equipo/create";
        }

        EstadoEquipo estadoExistente =
                estadoEquipoService.buscarPorNombre(
                        estadoEquipo.getNombre()
                );

        if (estadoExistente != null) {

            boolean perteneceAlMismoEstado =
                    esEdicion
                            && estadoExistente
                            .getId()
                            .equals(estadoEquipo.getId());

            if (!perteneceAlMismoEstado) {

                model.addAttribute(
                        "error",
                        "El nombre del estado del equipo ya está registrado."
                );

                if (esEdicion) {
                    return "estados-equipo/edit";
                }

                return "estados-equipo/create";
            }
        }

        if (esEdicion) {

            EstadoEquipo estadoEquipoExistente =
                    estadoEquipoService.buscarPorId(
                            estadoEquipo.getId()
                    );

            if (estadoEquipoExistente == null) {

                attributes.addFlashAttribute(
                        "error",
                        "El estado del equipo que intenta editar no existe."
                );

                return "redirect:/estados-equipo";
            }

            estadoEquipoExistente.setNombre(
                    estadoEquipo.getNombre()
            );

            estadoEquipoExistente.setDescripcion(
                    estadoEquipo.getDescripcion()
            );

            estadoEquipoService.actualizar(
                    estadoEquipoExistente
            );

            attributes.addFlashAttribute(
                    "msg",
                    "Estado del equipo actualizado correctamente"
            );

            return "redirect:/estados-equipo";
        }

        estadoEquipoService.crear(
                estadoEquipo
        );

        attributes.addFlashAttribute(
                "msg",
                "Estado del equipo guardado correctamente"
        );

        return "redirect:/estados-equipo";
    }

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        EstadoEquipo estadoEquipo =
                estadoEquipoService.buscarPorId(id);

        if (estadoEquipo == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El estado del equipo solicitado no existe."
            );

            return "redirect:/estados-equipo";
        }

        model.addAttribute(
                "estadoEquipo",
                estadoEquipo
        );

        return "estados-equipo/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        EstadoEquipo estadoEquipo =
                estadoEquipoService.buscarPorId(id);

        if (estadoEquipo == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El estado del equipo que intenta editar no existe."
            );

            return "redirect:/estados-equipo";
        }

        model.addAttribute(
                "estadoEquipo",
                estadoEquipo
        );

        return "estados-equipo/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        EstadoEquipo estadoEquipo =
                estadoEquipoService.buscarPorId(id);

        if (estadoEquipo == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El estado del equipo que intenta eliminar no existe."
            );

            return "redirect:/estados-equipo";
        }

        model.addAttribute(
                "estadoEquipo",
                estadoEquipo
        );

        return "estados-equipo/delete";
    }

    @PostMapping("/delete")
    public String delete(
            EstadoEquipo estadoEquipo,
            RedirectAttributes attributes) {

        if (estadoEquipo.getId() == null) {

            attributes.addFlashAttribute(
                    "error",
                    "No se pudo identificar el estado del equipo."
            );

            return "redirect:/estados-equipo";
        }

        EstadoEquipo estadoExistente =
                estadoEquipoService.buscarPorId(
                        estadoEquipo.getId()
                );

        if (estadoExistente == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El estado del equipo que intenta eliminar no existe."
            );

            return "redirect:/estados-equipo";
        }

        estadoEquipoService.eliminarPorId(
                estadoEquipo.getId()
        );

        attributes.addFlashAttribute(
                "msg",
                "Estado del equipo eliminado correctamente"
        );

        return "redirect:/estados-equipo";
    }

    private void agregarNumerosDePagina(
            Model model,
            Page<EstadoEquipo> estadosEquipo) {

        if (estadosEquipo.getTotalPages() > 0) {

            List<Integer> pageNumbers =
                    IntStream.rangeClosed(
                                    1,
                                    estadosEquipo.getTotalPages()
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