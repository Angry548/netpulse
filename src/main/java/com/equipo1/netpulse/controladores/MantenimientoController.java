package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.Mantenimiento;
import com.equipo1.netpulse.modelos.TipoMantenimiento;
import com.equipo1.netpulse.servicios.interfaces.IEquipoService;
import com.equipo1.netpulse.servicios.interfaces.IMantenimientoService;
import com.equipo1.netpulse.servicios.interfaces.ITicketService;
import com.equipo1.netpulse.servicios.interfaces.IUsuarioService;
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
@RequestMapping("/mantenimientos")
public class MantenimientoController {

    private final IMantenimientoService mantenimientoService;
    private final IEquipoService equipoService;
    private final IUsuarioService usuarioService;
    private final ITicketService ticketService;

    public MantenimientoController(
            IMantenimientoService mantenimientoService,
            IEquipoService equipoService,
            IUsuarioService usuarioService,
            ITicketService ticketService) {

        this.mantenimientoService = mantenimientoService;
        this.equipoService = equipoService;
        this.usuarioService = usuarioService;
        this.ticketService = ticketService;
    }

    @GetMapping
    public String index(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Mantenimiento> mantenimientos =
                mantenimientoService.buscarTodosPaginados(pageable);

        model.addAttribute("mantenimientos", mantenimientos);
        agregarNumerosDePagina(model, mantenimientos);

        return "mantenimiento/index";
    }

    @GetMapping("/create")
    public String create(Mantenimiento mantenimiento, Model model) {

        cargarCatalogos(model);

        return "mantenimiento/create";
    }

    @PostMapping("/save")
    public String save(
            Mantenimiento mantenimiento,
            BindingResult result,
            Model model,
            RedirectAttributes attributes) {

        if (result.hasErrors()) {
            model.addAttribute("mantenimiento", mantenimiento);
            cargarCatalogos(model);

            attributes.addFlashAttribute(
                    "error",
                    "No se pudo guardar debido a un error.");

            return "mantenimiento/create";
        }

        if (mantenimiento.getFecha() == null) {
            mantenimiento.setFecha(java.time.LocalDateTime.now());
        }

        if (mantenimiento.getId() == null) {
            mantenimientoService.registrar(mantenimiento);
            attributes.addFlashAttribute(
                    "msg",
                    "Mantenimiento registrado correctamente");
        } else {
            mantenimientoService.actualizar(mantenimiento);
            attributes.addFlashAttribute(
                    "msg",
                    "Mantenimiento actualizado correctamente");
        }

        return "redirect:/mantenimientos";
    }

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model) {

        model.addAttribute(
                "mantenimiento",
                mantenimientoService.buscarPorId(id).orElseThrow());

        return "mantenimiento/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model) {

        model.addAttribute(
                "mantenimiento",
                mantenimientoService.buscarPorId(id).orElseThrow());

        cargarCatalogos(model);

        return "mantenimiento/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Integer id,
            Model model) {

        model.addAttribute(
                "mantenimiento",
                mantenimientoService.buscarPorId(id).orElseThrow());

        return "mantenimiento/delete";
    }

    @PostMapping("/delete")
    public String delete(
            Mantenimiento mantenimiento,
            RedirectAttributes attributes) {

        mantenimientoService.eliminarPorId(mantenimiento.getId());

        attributes.addFlashAttribute(
                "msg",
                "Mantenimiento eliminado correctamente");

        return "redirect:/mantenimientos";
    }

    private void cargarCatalogos(Model model) {

        model.addAttribute("equipos", equipoService.obtenerTodos());
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        model.addAttribute("tickets", ticketService.obtenerTodos());
        model.addAttribute("tipos", TipoMantenimiento.values());
    }

    private void agregarNumerosDePagina(
            Model model,
            Page<Mantenimiento> mantenimientos) {

        if (mantenimientos.getTotalPages() > 0) {

            List<Integer> pageNumbers = IntStream
                    .rangeClosed(1, mantenimientos.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }
    }
}

