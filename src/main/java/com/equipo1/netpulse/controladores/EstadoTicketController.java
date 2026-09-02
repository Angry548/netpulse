package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.EstadoTicket;
import com.equipo1.netpulse.servicios.interfaces.IEstadoTicketService;
import jakarta.validation.Valid;
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

    public EstadoTicketController(IEstadoTicketService estadoTicketService) {
        this.estadoTicketService = estadoTicketService;
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size,
                        @RequestParam("nombre") Optional<String> nombre) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize);

        String filtroNombre = nombre.orElse("").trim();

        Page<EstadoTicket> estadosTicket;

        if (filtroNombre.isEmpty()) {
            estadosTicket = estadoTicketService.buscarTodosPaginados(pageable);
        } else {
            estadosTicket = estadoTicketService.buscarPorNombre(filtroNombre, pageable);
        }

        model.addAttribute("estadosTicket", estadosTicket);
        model.addAttribute("nombre", filtroNombre);

        agregarNumerosDePagina(model, estadosTicket);

        return "estado-ticket/index";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam("nombre") String nombre,
                         @RequestParam("size") Optional<Integer> size,
                         Model model) {

        Pageable pageable = PageRequest.of(0, size.orElse(10));

        Page<EstadoTicket> estadosTicket =
                estadoTicketService.buscarPorNombre(nombre.trim(), pageable);

        model.addAttribute("estadosTicket", estadosTicket);
        model.addAttribute("nombre", nombre.trim());

        agregarNumerosDePagina(model, estadosTicket);

        return "estado-ticket/index :: tablaEstadosTicket";
    }

    @GetMapping("/create")
    public String create(EstadoTicket estadoTicket, Model model) {
        return "estado-ticket/create";
    }

    @PostMapping("/save")
    public String save(@Valid EstadoTicket estadoTicket,
                       BindingResult result,
                       Model model,
                       RedirectAttributes attributes) {

        if (result.hasErrors()) {
            model.addAttribute("estadoTicket", estadoTicket);
            attributes.addFlashAttribute(
                    "error",
                    "No se pudo guardar debido a un error."
            );
            return "estado-ticket/create";
        }

        estadoTicketService.crear(estadoTicket);

        attributes.addFlashAttribute(
                "msg",
                "Estado de ticket guardado correctamente"
        );

        return "redirect:/estados-ticket";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Integer id, Model model) {

        model.addAttribute(
                "estadoTicket",
                estadoTicketService.buscarPorId(id).orElseThrow()
        );

        return "estado-ticket/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {

        model.addAttribute(
                "estadoTicket",
                estadoTicketService.buscarPorId(id).orElseThrow()
        );

        return "estado-ticket/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Integer id, Model model) {

        model.addAttribute(
                "estadoTicket",
                estadoTicketService.buscarPorId(id).orElseThrow()
        );

        return "estado-ticket/delete";
    }

    @PostMapping("/delete")
    public String delete(EstadoTicket estadoTicket,
                         RedirectAttributes attributes) {

        estadoTicketService.eliminarPorId(estadoTicket.getId());

        attributes.addFlashAttribute(
                "msg",
                "Estado de ticket eliminado correctamente"
        );

        return "redirect:/estados-ticket";
    }

    private void agregarNumerosDePagina(Model model,
                                        Page<EstadoTicket> estadosTicket) {

        if (estadosTicket.getTotalPages() > 0) {

            List<Integer> pageNumbers = IntStream
                    .rangeClosed(1, estadosTicket.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }
    }
}
