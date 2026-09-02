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

    private final ICategoriaIncidenciaService categoriaService;

    public CategoriaIncidenciaController(ICategoriaIncidenciaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<CategoriaIncidencia> categorias =
                categoriaService.buscarTodosPaginados(pageable);

        model.addAttribute("categorias", categorias);

        agregarNumerosDePagina(model, categorias);

        return "categoriaIncidencia/index";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam("nombre") String nombre,
                         @RequestParam("size") Optional<Integer> size,
                         Model model) {

        Pageable pageable = PageRequest.of(0, size.orElse(10));

        Page<CategoriaIncidencia> categorias =
                categoriaService.buscarPorNombre(nombre.trim(), pageable);

        model.addAttribute("categorias", categorias);
        model.addAttribute("nombre", nombre.trim());

        agregarNumerosDePagina(model, categorias);

        return "categoriaIncidencia/index :: tablaCategorias";
    }

    @GetMapping("/create")
    public String create(CategoriaIncidencia categoria) {

        return "categoriaIncidencia/create";
    }

    @PostMapping("/save")
    public String save(CategoriaIncidencia categoria,
                       BindingResult result,
                       Model model,
                       RedirectAttributes attributes) {

        if (result.hasErrors()) {

            model.addAttribute("categoria", categoria);

            attributes.addFlashAttribute(
                    "error",
                    "No se pudo guardar debido a un error."
            );

            return "categoriaIncidencia/create";
        }

        categoriaService.crear(categoria);

        attributes.addFlashAttribute(
                "msg",
                "Categoría de incidencia guardada correctamente"
        );

        return "redirect:/categorias-incidencia";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Integer id,
                          Model model) {

        model.addAttribute(
                "categoria",
                categoriaService.buscarPorId(id).orElseThrow()
        );

        return "categoriaIncidencia/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id,
                       Model model) {

        model.addAttribute(
                "categoria",
                categoriaService.buscarPorId(id).orElseThrow()
        );

        return "categoriaIncidencia/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Integer id,
                         Model model) {

        model.addAttribute(
                "categoria",
                categoriaService.buscarPorId(id).orElseThrow()
        );

        return "categoriaIncidencia/delete";
    }

    @PostMapping("/delete")
    public String delete(CategoriaIncidencia categoria,
                         RedirectAttributes attributes) {

        categoriaService.eliminarPorId(categoria.getId());

        attributes.addFlashAttribute(
                "msg",
                "Categoría de incidencia eliminada correctamente"
        );

        return "redirect:/categorias-incidencia";
    }

    @PostMapping("/activar/{id}")
    public String activar(@PathVariable Integer id,
                          RedirectAttributes attributes) {

        CategoriaIncidencia categoria =
                categoriaService.buscarPorId(id).orElseThrow();

        categoriaService.activar(categoria);

        attributes.addFlashAttribute(
                "msg",
                "Categoría de incidencia activada correctamente"
        );

        return "redirect:/categorias-incidencia";
    }

    @PostMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Integer id,
                             RedirectAttributes attributes) {

        CategoriaIncidencia categoria =
                categoriaService.buscarPorId(id).orElseThrow();

        categoriaService.desactivar(categoria);

        attributes.addFlashAttribute(
                "msg",
                "Categoría de incidencia desactivada correctamente"
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

            model.addAttribute("pageNumbers", pageNumbers);
        }
    }
}
