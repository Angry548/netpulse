package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.TipoEquipo;
import com.equipo1.netpulse.servicios.interfaces.ITipoEquipoService;
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
@RequestMapping("/tipos-equipo")
public class TipoEquipoController {

    private final ITipoEquipoService tipoEquipoService;

    public TipoEquipoController(
            ITipoEquipoService tipoEquipoService) {

        this.tipoEquipoService = tipoEquipoService;
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

        Page<TipoEquipo> tiposEquipo;

        if (id.isPresent()) {

            tiposEquipo =
                    tipoEquipoService.buscarPorIdPaginado(
                            id.get(),
                            pageable
                    );

        } else if (!filtroNombre.isEmpty()) {

            tiposEquipo =
                    tipoEquipoService.buscarPorNombre(
                            filtroNombre,
                            pageable
                    );

        } else {

            tiposEquipo =
                    tipoEquipoService.buscarTodosPaginados(
                            pageable
                    );
        }

        model.addAttribute(
                "tiposEquipo",
                tiposEquipo
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
                tiposEquipo
        );

        return "tipos-equipo/index";
    }

    @GetMapping("/create")
    public String create(Model model) {

        TipoEquipo tipoEquipo =
                new TipoEquipo();

        model.addAttribute(
                "tipoEquipo",
                tipoEquipo
        );

        return "tipos-equipo/create";
    }

    @PostMapping("/save")
    public String save(
            TipoEquipo tipoEquipo,
            BindingResult result,
            Model model,
            RedirectAttributes attributes) {

        /*
         * Si el ID existe, estamos editando.
         * Si el ID es null, estamos creando.
         */
        boolean esEdicion =
                tipoEquipo.getId() != null;

        /*
         * Validaciones del modelo.
         */
        if (result.hasErrors()) {

            if (esEdicion) {
                return "tipos-equipo/edit";
            }

            return "tipos-equipo/create";
        }

        /*
         * Verificamos si ya existe otro tipo
         * de equipo con el mismo nombre.
         */
        TipoEquipo tipoExistente =
                tipoEquipoService.buscarPorNombre(
                        tipoEquipo.getNombre()
                );

        if (tipoExistente != null) {

            boolean perteneceAlMismoTipo =
                    esEdicion
                            && tipoExistente.getId()
                            .equals(tipoEquipo.getId());

            /*
             * Si pertenece al mismo registro estamos
             * editando el nombre que ya tenía.
             */
            if (!perteneceAlMismoTipo) {

                model.addAttribute(
                        "error",
                        "El nombre del tipo de equipo ya está registrado."
                );

                if (esEdicion) {
                    return "tipos-equipo/edit";
                }

                return "tipos-equipo/create";
            }
        }

        if (esEdicion) {

            TipoEquipo tipoEquipoExistente =
                    tipoEquipoService.buscarPorId(
                            tipoEquipo.getId()
                    );

            /*
             * Verificamos que realmente exista.
             */
            if (tipoEquipoExistente == null) {

                attributes.addFlashAttribute(
                        "error",
                        "El tipo de equipo que intenta editar no existe."
                );

                return "redirect:/tipos-equipo";
            }

            /*
             * Actualizamos los campos permitidos.
             */
            tipoEquipoExistente.setNombre(
                    tipoEquipo.getNombre()
            );

            tipoEquipoExistente.setDescripcion(
                    tipoEquipo.getDescripcion()
            );

            tipoEquipoService.actualizar(
                    tipoEquipoExistente
            );

            attributes.addFlashAttribute(
                    "msg",
                    "Tipo de equipo actualizado correctamente"
            );

            return "redirect:/tipos-equipo";
        }



        tipoEquipoService.crear(
                tipoEquipo
        );

        attributes.addFlashAttribute(
                "msg",
                "Tipo de equipo guardado correctamente"
        );

        return "redirect:/tipos-equipo";
    }

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        TipoEquipo tipoEquipo =
                tipoEquipoService.buscarPorId(id);

        if (tipoEquipo == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El tipo de equipo solicitado no existe."
            );

            return "redirect:/tipos-equipo";
        }

        model.addAttribute(
                "tipoEquipo",
                tipoEquipo
        );

        return "tipos-equipo/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        TipoEquipo tipoEquipo =
                tipoEquipoService.buscarPorId(id);

        if (tipoEquipo == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El tipo de equipo que intenta editar no existe."
            );

            return "redirect:/tipos-equipo";
        }

        model.addAttribute(
                "tipoEquipo",
                tipoEquipo
        );

        return "tipos-equipo/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        TipoEquipo tipoEquipo =
                tipoEquipoService.buscarPorId(id);

        if (tipoEquipo == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El tipo de equipo que intenta eliminar no existe."
            );

            return "redirect:/tipos-equipo";
        }

        model.addAttribute(
                "tipoEquipo",
                tipoEquipo
        );

        return "tipos-equipo/delete";
    }

    @PostMapping("/delete")
    public String delete(
            TipoEquipo tipoEquipo,
            RedirectAttributes attributes) {

        if (tipoEquipo.getId() == null) {

            attributes.addFlashAttribute(
                    "error",
                    "No se pudo identificar el tipo de equipo."
            );

            return "redirect:/tipos-equipo";
        }

        TipoEquipo tipoExistente =
                tipoEquipoService.buscarPorId(
                        tipoEquipo.getId()
                );

        if (tipoExistente == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El tipo de equipo que intenta eliminar no existe."
            );

            return "redirect:/tipos-equipo";
        }

        tipoEquipoService.eliminarPorId(
                tipoEquipo.getId()
        );

        attributes.addFlashAttribute(
                "msg",
                "Tipo de equipo eliminado correctamente"
        );

        return "redirect:/tipos-equipo";
    }

    private void agregarNumerosDePagina(
            Model model,
            Page<TipoEquipo> tiposEquipo) {

        if (tiposEquipo.getTotalPages() > 0) {

            List<Integer> pageNumbers =
                    IntStream.rangeClosed(
                                    1,
                                    tiposEquipo.getTotalPages()
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