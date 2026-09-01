package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.PrioridadTicket;
import com.equipo1.netpulse.servicios.interfaces.IPrioridadTicketService;
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
@RequestMapping("/prioridades-ticket")
public class PrioridadTicketController {

    private final IPrioridadTicketService prioridadTicketService;

    public PrioridadTicketController(IPrioridadTicketService prioridadTicketService) {
        this.prioridadTicketService = prioridadTicketService;
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

        Page<PrioridadTicket> prioridades;

        if (filtroNombre.isEmpty()) {
            prioridades = prioridadTicketService.buscarTodosPaginados(pageable);
        } else {
            prioridades = prioridadTicketService.buscarPorNombre(filtroNombre, pageable);
        }

        model.addAttribute("prioridades", prioridades);
        model.addAttribute("nombre", filtroNombre);

        agregarNumerosDePagina(model, prioridades);

        return "prioridad-ticket/index";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam("nombre") String nombre,
                         @RequestParam("size") Optional<Integer> size,
                         Model model) {

        Pageable pageable = PageRequest.of(0, size.orElse(5));

        String filtroNombre = nombre.trim();

        Page<PrioridadTicket> prioridades =
                prioridadTicketService.buscarPorNombre(filtroNombre, pageable);

        model.addAttribute("prioridades", prioridades);
        model.addAttribute("nombre", filtroNombre);

        agregarNumerosDePagina(model, prioridades);

        return "prioridad-ticket/index :: tablaPrioridades";
    }

    @GetMapping("/create")
    public String create(PrioridadTicket prioridadTicket) {
        return "prioridad-ticket/create";
    }

    @PostMapping("/save")
    public String save(PrioridadTicket prioridadTicket,
                       BindingResult result,
                       Model model,
                       RedirectAttributes attributes) {

        if (result.hasErrors()) {
            model.addAttribute("prioridadTicket", prioridadTicket);
            attributes.addFlashAttribute(
                    "error",
                    "No se pudo guardar debido a un error."
            );

            return "prioridad-ticket/create";
        }

        if (prioridadTicket.getId() == null) {
            prioridadTicketService.crear(prioridadTicket);
            attributes.addFlashAttribute(
                    "msg",
                    "Prioridad de ticket creada correctamente"
            );
        } else {
            prioridadTicketService.actualizar(prioridadTicket);
            attributes.addFlashAttribute(
                    "msg",
                    "Prioridad de ticket actualizada correctamente"
            );
        }

        return "redirect:/prioridades-ticket";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Integer id, Model model) {

        PrioridadTicket prioridadTicket =
                prioridadTicketService.buscarPorId(id).orElseThrow();

        model.addAttribute("prioridadTicket", prioridadTicket);

        return "prioridad-ticket/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {

        PrioridadTicket prioridadTicket =
                prioridadTicketService.buscarPorId(id).orElseThrow();

        model.addAttribute("prioridadTicket", prioridadTicket);

        return "prioridad-ticket/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Integer id, Model model) {

        PrioridadTicket prioridadTicket =
                prioridadTicketService.buscarPorId(id).orElseThrow();

        model.addAttribute("prioridadTicket", prioridadTicket);

        return "prioridad-ticket/delete";
    }

    @PostMapping("/delete")
    public String delete(PrioridadTicket prioridadTicket,
                         RedirectAttributes attributes) {

        prioridadTicketService.eliminarPorId(prioridadTicket.getId());

        attributes.addFlashAttribute(
                "msg",
                "Prioridad de ticket eliminada correctamente"
        );

        return "redirect:/prioridades-ticket";
    }

    private void agregarNumerosDePagina(Model model,
                                        Page<PrioridadTicket> prioridades) {

        if (prioridades.getTotalPages() > 0) {

            List<Integer> pageNumbers = IntStream
                    .rangeClosed(1, prioridades.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }
    }
}
