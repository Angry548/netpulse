package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.AlertaRed;
import com.equipo1.netpulse.servicios.interfaces.IAlertaRedService;
import com.equipo1.netpulse.servicios.interfaces.IEquipoService;

import org.springframework.data.domain.Page;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/alertas")
public class AlertaRedController {

    private final IAlertaRedService alertaRedService;
    private final IEquipoService equipoService;

    public AlertaRedController(IAlertaRedService alertaRedService,
                               IEquipoService equipoService) {
        this.alertaRedService = alertaRedService;
        this.equipoService = equipoService;
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size,
                        @RequestParam("tipoEvento") Optional<String> tipoEvento) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize);

        String filtroTipoEvento = tipoEvento.orElse("").trim();

        Page<AlertaRed> alertas;

        if (filtroTipoEvento.isEmpty()) {
            alertas = alertaRedService.buscarTodosPaginados(pageable);
        } else {
            alertas = alertaRedService.obtenerPorTipoEventoPaginado(
                    filtroTipoEvento,
                    pageable
            );
        }

        model.addAttribute("alertas", alertas);
        model.addAttribute("tipoEvento", filtroTipoEvento);

        agregarNumerosDePagina(model, alertas);

        return "alerta/index";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam("tipoEvento") String tipoEvento,
                         @RequestParam("size") Optional<Integer> size,
                         Model model) {

        Pageable pageable = PageRequest.of(0, size.orElse(10));

        Page<AlertaRed> alertas =
                alertaRedService.obtenerPorTipoEventoPaginado(
                        tipoEvento.trim(),
                        pageable
                );

        model.addAttribute("alertas", alertas);
        model.addAttribute("tipoEvento", tipoEvento.trim());

        agregarNumerosDePagina(model, alertas);

        return "alerta/index :: tablaAlertas";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Integer id, Model model) {

        model.addAttribute(
                "alerta",
                alertaRedService.buscarPorId(id).orElseThrow()
        );

        return "alerta/details";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Integer id, Model model) {

        model.addAttribute(
                "alerta",
                alertaRedService.buscarPorId(id).orElseThrow()
        );

        return "alerta/delete";
    }

    @PostMapping("/delete")
    public String delete(AlertaRed alerta,
                         RedirectAttributes attributes) {

        alertaRedService.eliminarPorId(alerta.getId());

        attributes.addFlashAttribute(
                "msg",
                "Alerta eliminada correctamente"
        );

        return "redirect:/alertas";
    }

    @PostMapping("/notificar/{id}")
    public String notificar(@PathVariable Integer id,
                            RedirectAttributes attributes) {

        AlertaRed alerta =
                alertaRedService.buscarPorId(id).orElseThrow();

        alertaRedService.enviarNotificacion(alerta);

        attributes.addFlashAttribute(
                "msg",
                "Notificación enviada correctamente"
        );

        return "redirect:/alertas";
    }

    @PostMapping("/marcar-notificada/{id}")
    public String marcarNotificada(@PathVariable Integer id,
                                   RedirectAttributes attributes) {

        AlertaRed alerta =
                alertaRedService.buscarPorId(id).orElseThrow();

        alertaRedService.marcarNotificacionEnviada(alerta);

        attributes.addFlashAttribute(
                "msg",
                "La alerta fue marcada como notificada"
        );

        return "redirect:/alertas";
    }

    private void agregarNumerosDePagina(Model model,
                                        Page<AlertaRed> alertas) {

        if (alertas.getTotalPages() > 0) {

            List<Integer> pageNumbers =
                    IntStream.rangeClosed(1, alertas.getTotalPages())
                            .boxed()
                            .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }
    }
}
