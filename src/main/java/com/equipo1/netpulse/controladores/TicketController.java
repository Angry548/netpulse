package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.EstadoTicket;
import com.equipo1.netpulse.modelos.PrioridadTicket;
import com.equipo1.netpulse.modelos.Ticket;
import com.equipo1.netpulse.modelos.Usuario;

import com.equipo1.netpulse.repositorios.ICategoriaIncidenciaRepository;
import com.equipo1.netpulse.repositorios.IEstadoTicketRepository;
import com.equipo1.netpulse.repositorios.IEquipoRepository;
import com.equipo1.netpulse.repositorios.IPrioridadTicketRepository;
import com.equipo1.netpulse.repositorios.IUsuarioRepository;

import com.equipo1.netpulse.servicios.interfaces.ITicketService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final ITicketService ticketService;

    private final IUsuarioRepository usuarioRepository;
    private final IEquipoRepository equipoRepository;
    private final ICategoriaIncidenciaRepository categoriaRepository;
    private final IPrioridadTicketRepository prioridadRepository;
    private final IEstadoTicketRepository estadoTicketRepository;

    public TicketController(
            ITicketService ticketService,
            IUsuarioRepository usuarioRepository,
            IEquipoRepository equipoRepository,
            ICategoriaIncidenciaRepository categoriaRepository,
            IPrioridadTicketRepository prioridadRepository,
            IEstadoTicketRepository estadoTicketRepository) {

        this.ticketService = ticketService;
        this.usuarioRepository = usuarioRepository;
        this.equipoRepository = equipoRepository;
        this.categoriaRepository = categoriaRepository;
        this.prioridadRepository = prioridadRepository;
        this.estadoTicketRepository = estadoTicketRepository;
    }

    @GetMapping
    public String index(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("id") Optional<Integer> id) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        if (currentPage < 0) {
            currentPage = 0;
        }

        Pageable pageable =
                PageRequest.of(
                        currentPage,
                        pageSize
                );

        Page<Ticket> tickets;

        if (id.isPresent()) {

            Ticket ticket =
                    ticketService.buscarPorId(
                            id.get()
                    );

            if (ticket != null) {

                List<Ticket> resultado =
                        Collections.singletonList(ticket);

                tickets =
                        new PageImpl<>(
                                resultado,
                                pageable,
                                1
                        );

            } else {

                tickets =
                        new PageImpl<>(
                                Collections.emptyList(),
                                pageable,
                                0
                        );
            }

        } else {

            tickets =
                    ticketService.buscarTodosPaginados(
                            pageable
                    );
        }

        model.addAttribute(
                "tickets",
                tickets
        );

        model.addAttribute(
                "id",
                id.orElse(null)
        );

        agregarNumerosDePagina(
                model,
                tickets
        );

        return "tickets/index";
    }


    @GetMapping("/create")
    public String create(Model model) {

        Ticket ticket = new Ticket();

        model.addAttribute(
                "ticket",
                ticket
        );

        cargarCatalogos(model);

        return "tickets/create";
    }


    @PostMapping("/save")
    public String save(
            @RequestParam("idEquipo") Integer idEquipo,
            @RequestParam("idUsuarioReporta") Integer idUsuarioReporta,
            @RequestParam(
                    value = "idTecnico",
                    required = false
            ) Integer idTecnico,
            @RequestParam("idCategoria") Integer idCategoria,
            @RequestParam("idPrioridad") Integer idPrioridad,
            @RequestParam("idEstadoTicket") Integer idEstadoTicket,
            Ticket ticket,
            Model model,
            RedirectAttributes attributes) {

        boolean esEdicion =
                ticket.getIdTicket() != null;


        Optional<Equipo> equipo =
                equipoRepository.findById(idEquipo);

        if (equipo.isEmpty()) {

            cargarCatalogos(model);

            model.addAttribute(
                    "error",
                    "El equipo seleccionado no existe."
            );

            if (esEdicion) {
                return "tickets/edit";
            }

            return "tickets/create";
        }

        Optional<Usuario> usuarioReporta =
                usuarioRepository.findById(
                        idUsuarioReporta
                );

        if (usuarioReporta.isEmpty()) {

            cargarCatalogos(model);

            model.addAttribute(
                    "error",
                    "El usuario que reporta no existe."
            );

            if (esEdicion) {
                return "tickets/edit";
            }

            return "tickets/create";
        }


        Optional<CategoriaIncidencia> categoria =
                categoriaRepository.findById(
                        idCategoria
                );

        if (categoria.isEmpty()) {

            cargarCatalogos(model);

            model.addAttribute(
                    "error",
                    "La categoría seleccionada no existe."
            );

            if (esEdicion) {
                return "tickets/edit";
            }

            return "tickets/create";
        }


        Optional<PrioridadTicket> prioridad =
                prioridadRepository.findById(
                        idPrioridad
                );

        if (prioridad.isEmpty()) {

            cargarCatalogos(model);

            model.addAttribute(
                    "error",
                    "La prioridad seleccionada no existe."
            );

            if (esEdicion) {
                return "tickets/edit";
            }

            return "tickets/create";
        }


        Optional<EstadoTicket> estado =
                estadoTicketRepository.findById(
                        idEstadoTicket
                );

        if (estado.isEmpty()) {

            cargarCatalogos(model);

            model.addAttribute(
                    "error",
                    "El estado seleccionado no existe."
            );

            if (esEdicion) {
                return "tickets/edit";
            }

            return "tickets/create";
        }


        ticket.setEquipo(
                equipo.get()
        );

        ticket.setUsuarioReporta(
                usuarioReporta.get()
        );

        ticket.setCategoria(
                categoria.get()
        );

        ticket.setPrioridad(
                prioridad.get()
        );

        ticket.setEstadoTicket(
                estado.get()
        );


        if (idTecnico != null) {

            Optional<Usuario> tecnico =
                    usuarioRepository.findById(
                            idTecnico
                    );

            if (tecnico.isEmpty()) {

                cargarCatalogos(model);

                model.addAttribute(
                        "error",
                        "El técnico seleccionado no existe."
                );

                if (esEdicion) {
                    return "tickets/edit";
                }

                return "tickets/create";
            }

            ticket.setTecnico(
                    tecnico.get()
            );

        } else {

            ticket.setTecnico(null);
        }


        if (esEdicion) {

            Ticket ticketExistente =
                    ticketService.buscarPorId(
                            ticket.getIdTicket()
                    );

            if (ticketExistente == null) {

                attributes.addFlashAttribute(
                        "error",
                        "El ticket que intenta editar no existe."
                );

                return "redirect:/tickets";
            }


            ticketExistente.setEquipo(
                    ticket.getEquipo()
            );

            ticketExistente.setUsuarioReporta(
                    ticket.getUsuarioReporta()
            );

            ticketExistente.setTecnico(
                    ticket.getTecnico()
            );

            ticketExistente.setCategoria(
                    ticket.getCategoria()
            );

            ticketExistente.setPrioridad(
                    ticket.getPrioridad()
            );

            ticketExistente.setEstadoTicket(
                    ticket.getEstadoTicket()
            );

            ticketExistente.setDescripcion(
                    ticket.getDescripcion()
            );


            if (ticketExistente.getTecnico() != null
                    && ticketExistente.getFechaAsignacion() == null) {

                ticketService.asignarTecnico(
                        ticketExistente
                );

            } else {

                ticketService.actualizar(
                        ticketExistente
                );
            }

            attributes.addFlashAttribute(
                    "msg",
                    "Ticket actualizado correctamente"
            );

            return "redirect:/tickets";
        }

        Ticket ticketGuardado =
                ticketService.registrar(
                        ticket
                );


        /*
         * Si se creó con técnico,
         * registrar fecha de asignación.
         */
        if (ticketGuardado.getTecnico() != null) {

            ticketService.asignarTecnico(
                    ticketGuardado
            );
        }

        attributes.addFlashAttribute(
                "msg",
                "Ticket guardado correctamente"
        );

        return "redirect:/tickets";
    }

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        Ticket ticket =
                ticketService.buscarPorId(id);

        if (ticket == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El ticket no existe."
            );

            return "redirect:/tickets";
        }

        model.addAttribute(
                "ticket",
                ticket
        );

        cargarCatalogos(model);

        return "tickets/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        Ticket ticket =
                ticketService.buscarPorId(id);

        if (ticket == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El ticket no existe."
            );

            return "redirect:/tickets";
        }

        model.addAttribute(
                "ticket",
                ticket
        );

        cargarCatalogos(model);

        return "tickets/edit";
    }


    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        Ticket ticket =
                ticketService.buscarPorId(id);

        if (ticket == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El ticket no existe."
            );

            return "redirect:/tickets";
        }

        model.addAttribute(
                "ticket",
                ticket
        );

        return "tickets/delete";
    }

    @PostMapping("/delete")
    public String delete(
            Ticket ticket,
            RedirectAttributes attributes) {

        ticketService.eliminarPorId(
                ticket.getIdTicket()
        );

        attributes.addFlashAttribute(
                "msg",
                "Ticket eliminado correctamente"
        );

        return "redirect:/tickets";
    }

    @PostMapping("/asignar-tecnico")
    public String asignarTecnico(
            @RequestParam("idTicket") Integer idTicket,
            @RequestParam("idTecnico") Integer idTecnico,
            RedirectAttributes attributes) {

        Ticket ticket =
                ticketService.buscarPorId(
                        idTicket
                );

        if (ticket == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El ticket no existe."
            );

            return "redirect:/tickets";
        }

        Optional<Usuario> tecnico =
                usuarioRepository.findById(
                        idTecnico
                );

        if (tecnico.isEmpty()) {

            attributes.addFlashAttribute(
                    "error",
                    "El técnico seleccionado no existe."
            );

            return "redirect:/tickets/details/"
                    + idTicket;
        }

        ticket.setTecnico(
                tecnico.get()
        );

        ticketService.asignarTecnico(
                ticket
        );

        attributes.addFlashAttribute(
                "msg",
                "Técnico asignado correctamente"
        );

        return "redirect:/tickets/details/"
                + idTicket;
    }

    @PostMapping("/cambiar-estado")
    public String cambiarEstado(
            @RequestParam("idTicket") Integer idTicket,
            @RequestParam("idEstadoTicket") Integer idEstadoTicket,
            RedirectAttributes attributes) {

        Ticket ticket =
                ticketService.buscarPorId(
                        idTicket
                );

        if (ticket == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El ticket no existe."
            );

            return "redirect:/tickets";
        }

        Optional<EstadoTicket> estado =
                estadoTicketRepository.findById(
                        idEstadoTicket
                );

        if (estado.isEmpty()) {

            attributes.addFlashAttribute(
                    "error",
                    "El estado seleccionado no existe."
            );

            return "redirect:/tickets/details/"
                    + idTicket;
        }

        ticket.setEstadoTicket(
                estado.get()
        );

        ticketService.cambiarEstado(
                ticket
        );

        attributes.addFlashAttribute(
                "msg",
                "Estado del ticket actualizado correctamente"
        );

        return "redirect:/tickets/details/"
                + idTicket;
    }


    @PostMapping("/cambiar-prioridad")
    public String cambiarPrioridad(
            @RequestParam("idTicket") Integer idTicket,
            @RequestParam("idPrioridad") Integer idPrioridad,
            RedirectAttributes attributes) {

        Ticket ticket =
                ticketService.buscarPorId(
                        idTicket
                );

        if (ticket == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El ticket no existe."
            );

            return "redirect:/tickets";
        }

        Optional<PrioridadTicket> prioridad =
                prioridadRepository.findById(
                        idPrioridad
                );

        if (prioridad.isEmpty()) {

            attributes.addFlashAttribute(
                    "error",
                    "La prioridad seleccionada no existe."
            );

            return "redirect:/tickets/details/"
                    + idTicket;
        }

        ticket.setPrioridad(
                prioridad.get()
        );

        ticketService.cambiarPrioridad(
                ticket
        );

        attributes.addFlashAttribute(
                "msg",
                "Prioridad del ticket actualizada correctamente"
        );

        return "redirect:/tickets/details/"
                + idTicket;
    }


    @PostMapping("/resolver")
    public String resolver(
            @RequestParam("idTicket") Integer idTicket,
            RedirectAttributes attributes) {

        Ticket ticket =
                ticketService.buscarPorId(
                        idTicket
                );

        if (ticket == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El ticket no existe."
            );

            return "redirect:/tickets";
        }

        ticketService.resolver(
                ticket
        );

        attributes.addFlashAttribute(
                "msg",
                "Ticket marcado como resuelto correctamente"
        );

        return "redirect:/tickets/details/"
                + idTicket;
    }

    private void cargarCatalogos(Model model) {

        model.addAttribute(
                "usuarios",
                usuarioRepository.findAll()
        );

        model.addAttribute(
                "tecnicos",
                usuarioRepository.findAll()
        );

        model.addAttribute(
                "equipos",
                equipoRepository.findAll()
        );

        model.addAttribute(
                "categorias",
                categoriaRepository.findAll()
        );

        model.addAttribute(
                "prioridades",
                prioridadRepository.findAll()
        );

        model.addAttribute(
                "estados",
                estadoTicketRepository.findAll()
        );
    }


    private void agregarNumerosDePagina(
            Model model,
            Page<Ticket> tickets) {

        if (tickets.getTotalPages() > 0) {

            List<Integer> pageNumbers =
                    IntStream.rangeClosed(
                                    1,
                                    tickets.getTotalPages()
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