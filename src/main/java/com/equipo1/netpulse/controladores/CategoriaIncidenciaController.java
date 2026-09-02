package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import com.equipo1.netpulse.servicios.interfaces.ICategoriaIncidenciaService;

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
@RequestMapping("/categorias-incidencia")
public class CategoriaIncidenciaController {

    private final ICategoriaIncidenciaService categoriaIncidenciaService;

    public CategoriaIncidenciaController(
            ICategoriaIncidenciaService categoriaIncidenciaService) {

        this.categoriaIncidenciaService = categoriaIncidenciaService;
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

        if (currentPage < 0) {
            currentPage = 0;
        }

        Pageable pageable = PageRequest.of(
                currentPage,
                pageSize
        );

        String filtroNombre = nombre.orElse("").trim();

        Page<CategoriaIncidencia> categorias;

        /*
         * ==========================================================
         * BUSCAR POR ID
         * ==========================================================
         */

        if (id.isPresent()) {

            CategoriaIncidencia categoria =
                    categoriaIncidenciaService.buscarPorId(
                            id.get()
                    );

            if (categoria != null) {

                categorias = new org.springframework.data.domain.PageImpl<>(
                        List.of(categoria),
                        pageable,
                        1
                );

            } else {

                categorias = new org.springframework.data.domain.PageImpl<>(
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

            categorias =
                    categoriaIncidenciaService.buscarPorNombrePaginado(
                            filtroNombre,
                            pageable
                    );

            /*
             * ==========================================================
             * LISTAR TODAS
             * ==========================================================
             */

        } else {

            categorias =
                    categoriaIncidenciaService.buscarTodosPaginados(
                            pageable
                    );
        }

        model.addAttribute(
                "categorias",
                categorias
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
                categorias
        );

        return "categorias-incidencia/index";
    }

    @GetMapping("/create")
    public String create(Model model) {

        CategoriaIncidencia categoria =
                new CategoriaIncidencia();

        model.addAttribute(
                "categoria",
                categoria
        );

        return "categorias-incidencia/create";
    }

    @PostMapping("/save")
    public String save(
            CategoriaIncidencia categoria,
            BindingResult result,
            Model model,
            RedirectAttributes attributes) {

        boolean esEdicion =
                categoria.getId() != null;

        if (result.hasErrors()) {

            if (esEdicion) {
                return "categorias-incidencia/edit";
            }

            return "categorias-incidencia/create";
        }

        CategoriaIncidencia categoriaExistente =
                categoriaIncidenciaService.buscarPorNombre(
                        categoria.getNombre()
                );

        if (categoriaExistente != null
                && !categoriaExistente.getId()
                .equals(categoria.getId())) {

            model.addAttribute(
                    "error",
                    "Ya existe una categoría con ese nombre."
            );

            if (esEdicion) {
                return "categorias-incidencia/edit";
            }

            return "categorias-incidencia/create";
        }

        /*
         * ==========================================================
         * EDITAR CATEGORÍA
         * ==========================================================
         */

        if (esEdicion) {

            CategoriaIncidencia categoriaActual =
                    categoriaIncidenciaService.buscarPorId(
                            categoria.getId()
                    );

            if (categoriaActual == null) {

                attributes.addFlashAttribute(
                        "error",
                        "La categoría que intenta editar no existe."
                );

                return "redirect:/categorias-incidencia";
            }

            categoriaActual.setNombre(
                    categoria.getNombre()
            );

            categoriaActual.setDescripcion(
                    categoria.getDescripcion()
            );

            if (categoria.getActivo() != null) {

                categoriaActual.setActivo(
                        categoria.getActivo()
                );
            }

            categoriaIncidenciaService.actualizar(
                    categoriaActual
            );

            attributes.addFlashAttribute(
                    "msg",
                    "Categoría actualizada correctamente"
            );

            return "redirect:/categorias-incidencia";
        }

        /*
         * ==========================================================
         * CREAR CATEGORÍA
         * ==========================================================
         */

        if (categoria.getActivo() == null) {

            categoria.setActivo(true);
        }

        categoriaIncidenciaService.crear(
                categoria
        );

        attributes.addFlashAttribute(
                "msg",
                "Categoría guardada correctamente"
        );

        return "redirect:/categorias-incidencia";
    }

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        CategoriaIncidencia categoria =
                categoriaIncidenciaService.buscarPorId(id);

        if (categoria == null) {

            attributes.addFlashAttribute(
                    "error",
                    "La categoría no existe."
            );

            return "redirect:/categorias-incidencia";
        }

        model.addAttribute(
                "categoria",
                categoria
        );

        return "categorias-incidencia/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        CategoriaIncidencia categoria =
                categoriaIncidenciaService.buscarPorId(id);

        if (categoria == null) {

            attributes.addFlashAttribute(
                    "error",
                    "La categoría no existe."
            );

            return "redirect:/categorias-incidencia";
        }

        model.addAttribute(
                "categoria",
                categoria
        );

        return "categorias-incidencia/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        CategoriaIncidencia categoria =
                categoriaIncidenciaService.buscarPorId(id);

        if (categoria == null) {

            attributes.addFlashAttribute(
                    "error",
                    "La categoría no existe."
            );

            return "redirect:/categorias-incidencia";
        }

        model.addAttribute(
                "categoria",
                categoria
        );

        return "categorias-incidencia/delete";
    }

    @PostMapping("/delete")
    public String delete(
            CategoriaIncidencia categoria,
            RedirectAttributes attributes) {

        CategoriaIncidencia categoriaExistente =
                categoriaIncidenciaService.buscarPorId(
                        categoria.getId()
                );

        if (categoriaExistente == null) {

            attributes.addFlashAttribute(
                    "error",
                    "La categoría no existe."
            );

            return "redirect:/categorias-incidencia";
        }

        categoriaIncidenciaService.eliminarPorId(
                categoria.getId()
        );

        attributes.addFlashAttribute(
                "msg",
                "Categoría eliminada correctamente"
        );

        return "redirect:/categorias-incidencia";
    }

    @PostMapping("/activar")
    public String activar(
            @RequestParam("id") Integer id,
            RedirectAttributes attributes) {

        CategoriaIncidencia categoria =
                categoriaIncidenciaService.buscarPorId(id);

        if (categoria == null) {

            attributes.addFlashAttribute(
                    "error",
                    "La categoría no existe."
            );

            return "redirect:/categorias-incidencia";
        }

        categoriaIncidenciaService.activar(
                categoria
        );

        attributes.addFlashAttribute(
                "msg",
                "Categoría activada correctamente"
        );

        return "redirect:/categorias-incidencia";
    }

    @PostMapping("/desactivar")
    public String desactivar(
            @RequestParam("id") Integer id,
            RedirectAttributes attributes) {

        CategoriaIncidencia categoria =
                categoriaIncidenciaService.buscarPorId(id);

        if (categoria == null) {

            attributes.addFlashAttribute(
                    "error",
                    "La categoría no existe."
            );

            return "redirect:/categorias-incidencia";
        }

        categoriaIncidenciaService.desactivar(
                categoria
        );

        attributes.addFlashAttribute(
                "msg",
                "Categoría desactivada correctamente"
        );

        return "redirect:/categorias-incidencia";
    }

    private void agregarNumerosDePagina(
            Model model,
            Page<CategoriaIncidencia> categorias) {

        if (categorias.getTotalPages() > 0) {

            List<Integer> pageNumbers =
                    IntStream.rangeClosed(
                                    1,
                                    categorias.getTotalPages()
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