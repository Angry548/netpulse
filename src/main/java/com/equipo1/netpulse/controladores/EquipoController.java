package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.EstadoConexion;
import com.equipo1.netpulse.modelos.EstadoEquipo;
import com.equipo1.netpulse.modelos.TipoEquipo;
import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.repositorios.IEstadoEquipoRepository;
import com.equipo1.netpulse.repositorios.ITipoEquipoRepository;
import com.equipo1.netpulse.servicios.interfaces.IEquipoService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/equipos")
public class EquipoController {

    private final IEquipoService equipoService;
    private final ITipoEquipoRepository tipoEquipoRepository;
    private final IEstadoEquipoRepository estadoEquipoRepository;
    private final IUsuarioService usuarioService;

    public EquipoController(
            IEquipoService equipoService,
            ITipoEquipoRepository tipoEquipoRepository,
            IEstadoEquipoRepository estadoEquipoRepository,
            IUsuarioService usuarioService) {

        this.equipoService = equipoService;
        this.tipoEquipoRepository = tipoEquipoRepository;
        this.estadoEquipoRepository = estadoEquipoRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String index(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("id") Optional<Integer> id,
            @RequestParam("numeroSerie") Optional<String> numeroSerie,
            @RequestParam("nombre") Optional<String> nombre) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(
                currentPage,
                pageSize
        );

        String filtroNumeroSerie =
                numeroSerie.orElse("").trim();

        String filtroNombre =
                nombre.orElse("").trim();

        Page<Equipo> equipos;

        if (id.isPresent()) {

            equipos = equipoService.buscarPorIdPaginado(
                    id.get(),
                    pageable
            );

        } else if (!filtroNumeroSerie.isEmpty()) {

            equipos = equipoService.buscarPorNumeroSerie(
                    filtroNumeroSerie,
                    pageable
            );

        } else if (!filtroNombre.isEmpty()) {

            equipos = equipoService.buscarPorNombre(
                    filtroNombre,
                    pageable
            );

        } else {

            equipos = equipoService.buscarTodosPaginados(
                    pageable
            );
        }

        model.addAttribute(
                "equipos",
                equipos
        );

        model.addAttribute(
                "id",
                id.orElse(null)
        );

        model.addAttribute(
                "numeroSerie",
                filtroNumeroSerie
        );

        model.addAttribute(
                "nombre",
                filtroNombre
        );

        agregarNumerosDePagina(
                model,
                equipos
        );

        return "equipos/index";
    }

    @GetMapping("/create")
    public String create(Model model) {

        Equipo equipo = new Equipo();

        equipo.setEstadoConexion(
                EstadoConexion.OFFLINE
        );

        model.addAttribute(
                "equipo",
                equipo
        );

        cargarCatalogos(model);

        return "equipos/create";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam("idTipo") Integer idTipo,
            @RequestParam("idEstado") Integer idEstado,
            @RequestParam(value = "idResponsable", required = false)
            Integer idResponsable,
            Equipo equipo,
            BindingResult result,
            Model model,
            RedirectAttributes attributes) {

        boolean esEdicion =
                equipo.getIdEquipo() != null;

        /*
         * ==========================================================
         * VALIDACIONES DEL FORMULARIO
         * ==========================================================
         */

        if (result.hasErrors()) {

            cargarCatalogos(model);

            if (esEdicion) {
                return "equipos/edit";
            }

            return "equipos/create";
        }

        /*
         * ==========================================================
         * BUSCAR TIPO
         * ==========================================================
         */

        Optional<TipoEquipo> tipo =
                tipoEquipoRepository.findById(idTipo);

        if (tipo.isEmpty()) {

            cargarCatalogos(model);

            model.addAttribute(
                    "error",
                    "El tipo de equipo seleccionado no existe."
            );

            if (esEdicion) {
                return "equipos/edit";
            }

            return "equipos/create";
        }

        /*
         * ==========================================================
         * BUSCAR ESTADO
         * ==========================================================
         */

        Optional<EstadoEquipo> estado =
                estadoEquipoRepository.findById(idEstado);

        if (estado.isEmpty()) {

            cargarCatalogos(model);

            model.addAttribute(
                    "error",
                    "El estado del equipo seleccionado no existe."
            );

            if (esEdicion) {
                return "equipos/edit";
            }

            return "equipos/create";
        }

        /*
         * ==========================================================
         * BUSCAR RESPONSABLE
         * ==========================================================
         */

        Usuario responsable = null;

        if (idResponsable != null) {

            responsable =
                    usuarioService.buscarPorId(idResponsable);

            if (responsable == null) {

                cargarCatalogos(model);

                model.addAttribute(
                        "error",
                        "El responsable seleccionado no existe."
                );

                if (esEdicion) {
                    return "equipos/edit";
                }

                return "equipos/create";
            }
        }

        /*
         * ==========================================================
         * VALIDAR NÚMERO DE SERIE DUPLICADO
         * ==========================================================
         */

        Equipo equipoConMismoNumeroSerie =
                equipoService.buscarPorNumeroSerie(
                        equipo.getNumeroSerie()
                );

        if (equipoConMismoNumeroSerie != null) {

            boolean perteneceAlMismoEquipo =
                    esEdicion
                            && equipoConMismoNumeroSerie
                            .getIdEquipo()
                            .equals(equipo.getIdEquipo());

            if (!perteneceAlMismoEquipo) {

                cargarCatalogos(model);

                model.addAttribute(
                        "error",
                        "El número de serie ya está registrado."
                );

                if (esEdicion) {
                    return "equipos/edit";
                }

                return "equipos/create";
            }
        }

        /*
         * ==========================================================
         * CREAR EQUIPO
         * ==========================================================
         */

        if (!esEdicion) {

            equipo.setTipo(
                    tipo.get()
            );

            equipo.setEstado(
                    estado.get()
            );

            equipo.setResponsable(
                    responsable
            );

            if (equipo.getEstadoConexion() == null) {

                equipo.setEstadoConexion(
                        EstadoConexion.OFFLINE
                );
            }

            equipo.setFechaRegistro(
                    LocalDateTime.now()
            );

            equipoService.registrar(
                    equipo
            );

            attributes.addFlashAttribute(
                    "msg",
                    "Equipo guardado correctamente"
            );

            return "redirect:/equipos";
        }

        /*
         * ==========================================================
         * EDITAR EQUIPO
         * ==========================================================
         */

        Equipo equipoExistente =
                equipoService.buscarPorId(
                        equipo.getIdEquipo()
                );

        if (equipoExistente == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El equipo que intenta editar no existe."
            );

            return "redirect:/equipos";
        }

        /*
         * Actualizamos únicamente los datos
         * correspondientes al formulario.
         */

        equipoExistente.setTipo(
                tipo.get()
        );

        equipoExistente.setEstado(
                estado.get()
        );

        equipoExistente.setResponsable(
                responsable
        );

        equipoExistente.setNombre(
                equipo.getNombre()
        );

        equipoExistente.setMarca(
                equipo.getMarca()
        );

        equipoExistente.setModelo(
                equipo.getModelo()
        );

        equipoExistente.setNumeroSerie(
                equipo.getNumeroSerie()
        );

        equipoExistente.setUbicacion(
                equipo.getUbicacion()
        );

        equipoExistente.setIpAddress(
                equipo.getIpAddress()
        );

        equipoExistente.setMacAddress(
                equipo.getMacAddress()
        );

        if (equipo.getEstadoConexion() != null) {

            equipoExistente.setEstadoConexion(
                    equipo.getEstadoConexion()
            );
        }

        equipoService.actualizar(
                equipoExistente
        );

        attributes.addFlashAttribute(
                "msg",
                "Equipo actualizado correctamente"
        );

        return "redirect:/equipos";
    }

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        Equipo equipo =
                equipoService.buscarPorId(id);

        if (equipo == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El equipo solicitado no existe."
            );

            return "redirect:/equipos";
        }

        model.addAttribute(
                "equipo",
                equipo
        );

        return "equipos/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        Equipo equipo =
                equipoService.buscarPorId(id);

        if (equipo == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El equipo que intenta editar no existe."
            );

            return "redirect:/equipos";
        }

        model.addAttribute(
                "equipo",
                equipo
        );

        cargarCatalogos(model);

        return "equipos/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        Equipo equipo =
                equipoService.buscarPorId(id);

        if (equipo == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El equipo que intenta eliminar no existe."
            );

            return "redirect:/equipos";
        }

        model.addAttribute(
                "equipo",
                equipo
        );

        return "equipos/delete";
    }

    @PostMapping("/delete")
    public String delete(
            Equipo equipo,
            RedirectAttributes attributes) {

        if (equipo.getIdEquipo() == null) {

            attributes.addFlashAttribute(
                    "error",
                    "No se pudo identificar el equipo."
            );

            return "redirect:/equipos";
        }

        Equipo equipoExistente =
                equipoService.buscarPorId(
                        equipo.getIdEquipo()
                );

        if (equipoExistente == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El equipo que intenta eliminar no existe."
            );

            return "redirect:/equipos";
        }

        equipoService.eliminarPorId(
                equipo.getIdEquipo()
        );

        attributes.addFlashAttribute(
                "msg",
                "Equipo eliminado correctamente"
        );

        return "redirect:/equipos";
    }

    /*
     * ==========================================================
     * CARGAR CATÁLOGOS
     * ==========================================================
     */

    private void cargarCatalogos(Model model) {

        model.addAttribute(
                "tipos",
                tipoEquipoRepository.findAll()
        );

        model.addAttribute(
                "estados",
                estadoEquipoRepository.findAll()
        );

        model.addAttribute(
                "usuarios",
                usuarioService.obtenerTodos()
        );

        model.addAttribute(
                "estadosConexion",
                EstadoConexion.values()
        );
    }

    /*
     * ==========================================================
     * PAGINACIÓN
     * ==========================================================
     */

    private void agregarNumerosDePagina(
            Model model,
            Page<Equipo> equipos) {

        if (equipos.getTotalPages() > 0) {

            List<Integer> pageNumbers =
                    IntStream.rangeClosed(
                                    1,
                                    equipos.getTotalPages()
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