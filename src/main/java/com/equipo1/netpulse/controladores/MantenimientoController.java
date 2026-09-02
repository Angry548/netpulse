package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.Mantenimiento;
import com.equipo1.netpulse.modelos.Ticket;
import com.equipo1.netpulse.modelos.TipoMantenimiento;
import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.repositorios.IEquipoRepository;
import com.equipo1.netpulse.repositorios.ITicketRepository;
import com.equipo1.netpulse.repositorios.IUsuarioRepository;
import com.equipo1.netpulse.servicios.interfaces.IMantenimientoService;

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
    private final IEquipoRepository equipoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ITicketRepository ticketRepository;

    public MantenimientoController(
            IMantenimientoService mantenimientoService,
            IEquipoRepository equipoRepository,
            IUsuarioRepository usuarioRepository,
            ITicketRepository ticketRepository) {

        this.mantenimientoService = mantenimientoService;
        this.equipoRepository = equipoRepository;
        this.usuarioRepository = usuarioRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    public String index(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("id") Optional<Integer> id) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(
                currentPage,
                pageSize
        );

        Page<Mantenimiento> mantenimientos;

        if (id.isPresent()) {

            Page<Mantenimiento> paginaCompleta =
                    mantenimientoService.buscarTodosPaginados(pageable);

            List<Mantenimiento> filtrados =
                    paginaCompleta.getContent()
                            .stream()
                            .filter(item -> item.getId().equals(id.get()))
                            .collect(Collectors.toList());

            mantenimientos = new org.springframework.data.domain.PageImpl<>(
                    filtrados,
                    pageable,
                    filtrados.size()
            );

        } else {

            mantenimientos =
                    mantenimientoService.buscarTodosPaginados(pageable);
        }

        model.addAttribute(
                "mantenimientos",
                mantenimientos
        );

        model.addAttribute(
                "id",
                id.orElse(null)
        );

        agregarNumerosDePagina(
                model,
                mantenimientos
        );

        return "mantenimientos/index";
    }

    @GetMapping("/create")
    public String create(Model model) {

        Mantenimiento mantenimiento =
                new Mantenimiento();

        model.addAttribute(
                "mantenimiento",
                mantenimiento
        );

        cargarDatosFormulario(model);

        return "mantenimientos/create";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam("idEquipo") Integer idEquipo,
            @RequestParam("idTecnico") Integer idTecnico,
            @RequestParam(value = "idTicket", required = false) Integer idTicket,
            Mantenimiento mantenimiento,
            BindingResult result,
            Model model,
            RedirectAttributes attributes) {

        boolean esEdicion =
                mantenimiento.getId() != null;

        if (result.hasErrors()) {

            cargarDatosFormulario(model);

            if (esEdicion) {
                return "mantenimientos/edit";
            }

            return "mantenimientos/create";
        }

        Optional<Equipo> equipo =
                equipoRepository.findById(idEquipo);

        Optional<Usuario> tecnico =
                usuarioRepository.findById(idTecnico);

        Optional<Ticket> ticket =
                idTicket != null
                        ? ticketRepository.findById(idTicket)
                        : Optional.empty();

        if (equipo.isEmpty()) {

            cargarDatosFormulario(model);

            model.addAttribute(
                    "error",
                    "El equipo seleccionado no existe."
            );

            if (esEdicion) {
                return "mantenimientos/edit";
            }

            return "mantenimientos/create";
        }

        if (tecnico.isEmpty()) {

            cargarDatosFormulario(model);

            model.addAttribute(
                    "error",
                    "El técnico seleccionado no existe."
            );

            if (esEdicion) {
                return "mantenimientos/edit";
            }

            return "mantenimientos/create";
        }

        if (idTicket != null && ticket.isEmpty()) {

            cargarDatosFormulario(model);

            model.addAttribute(
                    "error",
                    "El ticket seleccionado no existe."
            );

            if (esEdicion) {
                return "mantenimientos/edit";
            }

            return "mantenimientos/create";
        }

        if (esEdicion) {

            Mantenimiento mantenimientoExistente =
                    mantenimientoService.buscarPorId(
                            mantenimiento.getId()
                    );

            if (mantenimientoExistente == null) {

                attributes.addFlashAttribute(
                        "error",
                        "El mantenimiento que intenta editar no existe."
                );

                return "redirect:/mantenimientos";
            }

            mantenimientoExistente.setEquipo(
                    equipo.get()
            );

            mantenimientoExistente.setTecnico(
                    tecnico.get()
            );

            mantenimientoExistente.setTicket(
                    ticket.orElse(null)
            );

            mantenimientoExistente.setTipo(
                    mantenimiento.getTipo()
            );

            mantenimientoExistente.setDescripcion(
                    mantenimiento.getDescripcion()
            );

            mantenimientoExistente.setRepuestos(
                    mantenimiento.getRepuestos()
            );

            if (mantenimiento.getFecha() != null) {

                mantenimientoExistente.setFecha(
                        mantenimiento.getFecha()
                );
            }

            mantenimientoService.actualizar(
                    mantenimientoExistente
            );

            attributes.addFlashAttribute(
                    "msg",
                    "Mantenimiento actualizado correctamente"
            );

            return "redirect:/mantenimientos";
        }

        mantenimiento.setEquipo(
                equipo.get()
        );

        mantenimiento.setTecnico(
                tecnico.get()
        );

        mantenimiento.setTicket(
                ticket.orElse(null)
        );

        mantenimientoService.registrar(
                mantenimiento
        );

        attributes.addFlashAttribute(
                "msg",
                "Mantenimiento guardado correctamente"
        );

        return "redirect:/mantenimientos";
    }

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model) {

        Mantenimiento mantenimiento =
                mantenimientoService.buscarPorId(id);

        model.addAttribute(
                "mantenimiento",
                mantenimiento
        );

        return "mantenimientos/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model) {

        Mantenimiento mantenimiento =
                mantenimientoService.buscarPorId(id);

        model.addAttribute(
                "mantenimiento",
                mantenimiento
        );

        cargarDatosFormulario(model);

        return "mantenimientos/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Integer id,
            Model model) {

        Mantenimiento mantenimiento =
                mantenimientoService.buscarPorId(id);

        model.addAttribute(
                "mantenimiento",
                mantenimiento
        );

        return "mantenimientos/delete";
    }

    @PostMapping("/delete")
    public String delete(
            Mantenimiento mantenimiento,
            RedirectAttributes attributes) {

        mantenimientoService.eliminarPorId(
                mantenimiento.getId()
        );

        attributes.addFlashAttribute(
                "msg",
                "Mantenimiento eliminado correctamente"
        );

        return "redirect:/mantenimientos";
    }

    private void cargarDatosFormulario(Model model) {

        model.addAttribute(
                "equipos",
                equipoRepository.findAll()
        );

        model.addAttribute(
                "tecnicos",
                usuarioRepository.findAll()
        );

        model.addAttribute(
                "tickets",
                ticketRepository.findAll()
        );

        model.addAttribute(
                "tiposMantenimiento",
                TipoMantenimiento.values()
        );
    }

    private void agregarNumerosDePagina(
            Model model,
            Page<Mantenimiento> mantenimientos) {

        if (mantenimientos.getTotalPages() > 0) {

            List<Integer> pageNumbers =
                    IntStream.rangeClosed(
                                    1,
                                    mantenimientos.getTotalPages()
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