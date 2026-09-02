package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.HistorialEstado;
import com.equipo1.netpulse.servicios.interfaces.IHistorialEstadoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/historial-estados")
public class HistorialEstadoController {

    private final IHistorialEstadoService historialEstadoService;

    public HistorialEstadoController(
            IHistorialEstadoService historialEstadoService) {

        this.historialEstadoService = historialEstadoService;
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

        if (pageSize <= 0) {
            pageSize = 5;
        }

        List<HistorialEstado> resultados;

        if (id.isPresent()) {

            HistorialEstado historial =
                    historialEstadoService.buscarPorId(id.get());

            if (historial != null) {
                resultados = List.of(historial);
            } else {
                resultados = List.of();
            }

        } else {

            resultados =
                    historialEstadoService.obtenerTodos();
        }

        int totalElementos = resultados.size();

        int inicio = currentPage * pageSize;
        int fin = Math.min(inicio + pageSize, totalElementos);

        List<HistorialEstado> elementosPagina;

        if (inicio < totalElementos) {
            elementosPagina =
                    resultados.subList(inicio, fin);
        } else {
            elementosPagina = List.of();
        }

        Pageable pageable =
                PageRequest.of(currentPage, pageSize);

        Page<HistorialEstado> historialEstados =
                new PageImpl<>(
                        elementosPagina,
                        pageable,
                        totalElementos
                );

        model.addAttribute(
                "historialEstados",
                historialEstados
        );

        model.addAttribute(
                "id",
                id.orElse(null)
        );

        agregarNumerosDePagina(
                model,
                historialEstados
        );

        return "historial-estados/index";
    }

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model) {

        HistorialEstado historialEstado =
                historialEstadoService.buscarPorId(id);

        model.addAttribute(
                "historialEstado",
                historialEstado
        );

        return "historial-estados/details";
    }

    private void agregarNumerosDePagina(
            Model model,
            Page<HistorialEstado> historialEstados) {

        if (historialEstados.getTotalPages() > 0) {

            List<Integer> pageNumbers =
                    IntStream.rangeClosed(
                                    1,
                                    historialEstados.getTotalPages()
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