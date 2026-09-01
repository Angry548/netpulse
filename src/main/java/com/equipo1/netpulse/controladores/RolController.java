package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.Rol;
import com.equipo1.netpulse.servicios.interfaces.IRolService;
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
@RequestMapping("/roles")
public class RolController {

    private final IRolService rolService;

    public RolController(IRolService rolService) {
        this.rolService = rolService;
    }

    // =========================================================
    // LISTADO DE ROLES Y BÚSQUEDA POR NOMBRE
    // =========================================================

    @GetMapping
    public String index(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("nombre") Optional<String> nombre) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize);

        String filtroNombre = nombre.orElse("").trim();

        Page<Rol> roles;

        if (filtroNombre.isEmpty()) {
            roles = rolService.buscarTodosPaginados(pageable);
        } else {
            roles = rolService.buscarPorNombre(filtroNombre, pageable);
        }

        model.addAttribute("roles", roles);
        model.addAttribute("nombre", filtroNombre);

        agregarNumerosDePagina(model, roles);

        return "roles/index";
    }

    // =========================================================
    // FORMULARIO CREAR
    // =========================================================

    @GetMapping("/create")
    public String create(Rol rol) {
        return "roles/create";
    }

    // =========================================================
    // GUARDAR ROL
    // =========================================================

    @PostMapping("/save")
    public String save(
            Rol rol,
            BindingResult result,
            RedirectAttributes attributes) {

        if (result.hasErrors()) {
            return "roles/create";
        }

        rolService.crear(rol);

        attributes.addFlashAttribute(
                "msg",
                "Rol guardado correctamente"
        );

        return "redirect:/roles";
    }

    // =========================================================
    // DETALLES
    // =========================================================

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model) {

        Rol rol = rolService.buscarPorId(id);

        model.addAttribute("rol", rol);

        return "roles/details";
    }

    // =========================================================
    // FORMULARIO EDITAR
    // =========================================================

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model) {

        Rol rol = rolService.buscarPorId(id);

        model.addAttribute("rol", rol);

        return "roles/edit";
    }

    // =========================================================
    // FORMULARIO ELIMINAR
    // =========================================================

    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Integer id,
            Model model) {

        Rol rol = rolService.buscarPorId(id);

        model.addAttribute("rol", rol);

        return "roles/delete";
    }

    // =========================================================
    // ELIMINAR ROL
    // =========================================================

    @PostMapping("/delete")
    public String delete(
            Rol rol,
            RedirectAttributes attributes) {

        rolService.eliminarPorId(rol.getIdRol());

        attributes.addFlashAttribute(
                "msg",
                "Rol eliminado correctamente"
        );

        return "redirect:/roles";
    }

    // =========================================================
    // PAGINACIÓN
    // =========================================================

    private void agregarNumerosDePagina(
            Model model,
            Page<Rol> roles) {

        if (roles.getTotalPages() > 0) {

            List<Integer> pageNumbers =
                    IntStream.rangeClosed(
                                    1,
                                    roles.getTotalPages()
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