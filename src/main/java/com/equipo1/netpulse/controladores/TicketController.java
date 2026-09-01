package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.Ticket;
import com.equipo1.netpulse.servicios.interfaces.ICategoriaIncidenciaService;
import com.equipo1.netpulse.servicios.interfaces.IEstadoTicketService;
import com.equipo1.netpulse.servicios.interfaces.IEquipoService;
import com.equipo1.netpulse.servicios.interfaces.IPrioridadTicketService;
import com.equipo1.netpulse.servicios.interfaces.ITicketService;
import com.equipo1.netpulse.servicios.interfaces.IUsuarioService;

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
@RequestMapping("/tickets")
public class TicketController {

    private final ITicketService ticketService;
    private final IEquipoService equipoService;
    private final IUsuarioService usuarioService;
    private final ICategoriaIncidenciaService categoriaService;
    private final IPrioridadTicketService prioridadService;
    private final IEstadoTicketService estadoTicketService;

    public TicketController(
            ITicketService ticketService,
            IEquipoService equipoService,
            IUsuarioService usuarioService,
            ICategoriaIncidenciaService categoriaService,
            IPrioridadTicketService prioridadService,
            IEstadoTicketService estadoTicketService) {

        this.ticketService = ticketService;
        this.equipoService = equipoService;
        this.usuarioService = usuarioService;
        this.categoriaService = categoriaService;
        this.prioridadService = prioridadService;
        this.estadoTicketService = estadoTicketService;
    }

    @GetMapping
    public String index(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(10);

        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Ticket> tickets = ticketService.buscarTodosPaginados(pageable);

        model.addAttribute("tickets", tickets);
        agregarNumerosDePagina(model, tickets);

        return "tickets/index";
    }

    @GetMapping("/create")
    public String create(Ticket ticket, Model model) {

        cargarCatalogos(model);

        return "tickets/create";
    }

    @PostMapping("/save")
    public String save(
            @Valid Ticket ticket,
            BindingResult result,
            Model model,
            RedirectAttributes attributes) {

        if (result.hasErrors()) {

            cargarCatalogos(model);

            attributes.addFlashAttribute(
                    "error",
                    "No se pudo guardar el ticket debido a errores de validación."
            );

            return ticket.getIdTicket() == null
                    ? "tickets/create"
                    : "tickets/edit";
        }

        if (ticket.getIdTicket() == null) {
            ticketService.registrar(ticket);
            attributes.addFlashAttribute(
                    "msg",
                    "Ticket registrado correctamente."
            );
        } else {
            ticketService.actualizar(ticket);
            attributes.addFlashAttribute(
                    "msg",
                    "Ticket actualizado correctamente."
            );
        }

        return "redirect:/tickets";
    }

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model) {

        model.addAttribute(
                "ticket",
                ticketService.buscarPorId(id).orElseThrow()
        );

        return "tickets/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model) {

        model.addAttribute(
                "ticket",
                ticketService.buscarPorId(id).orElseThrow()
        );

        cargarCatalogos(model);

        return "tickets/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Integer id,
            Model model) {

        model.addAttribute(
                "ticket",
                ticketService.buscarPorId(id).orElseThrow()
        );

        return "tickets/delete";
    }

    @PostMapping("/delete")
    public String delete(
            Ticket ticket,
            RedirectAttributes attributes) {

        ticketService.eliminarPorId(ticket.getIdTicket());

        attributes.addFlashAttribute(
                "msg",
                "Ticket eliminado correctamente."
        );

        return "redirect:/tickets";
    }

    private void cargarCatalogos(Model model) {

        model.addAttribute("equipos", equipoService.obtenerTodos());
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        model.addAttribute("categorias", categoriaService.obtenerTodos());
        model.addAttribute("prioridades", prioridadService.obtenerTodos());
        model.addAttribute("estados", estadoTicketService.obtenerTodos());
    }

    private void agregarNumerosDePagina(
            Model model,
            Page<Ticket> tickets) {

        if (tickets.getTotalPages() > 0) {

            List<Integer> pageNumbers = IntStream
                    .rangeClosed(1, tickets.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }
    }
}
