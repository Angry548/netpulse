package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.Empleado;
import com.equipo1.netpulse.modelos.EstadoEmpleado;
import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.servicios.interfaces.IEmpleadoService;
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
@RequestMapping("/empleados")
public class EmpleadoController {

    private final IEmpleadoService empleadoService;
    private final IUsuarioService usuarioService;

    public EmpleadoController(
            IEmpleadoService empleadoService,
            IUsuarioService usuarioService) {

        this.empleadoService = empleadoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String index(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("id") Optional<Integer> id,
            @RequestParam("codigoEmpleado") Optional<String> codigoEmpleado,
            @RequestParam("departamento") Optional<String> departamento) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(
                currentPage,
                pageSize
        );

        String filtroCodigo = codigoEmpleado
                .orElse("")
                .trim();

        String filtroDepartamento = departamento
                .orElse("")
                .trim();

        Page<Empleado> empleados;

        if (id.isPresent()) {

            empleados = empleadoService.buscarPorIdPaginado(
                    id.get(),
                    pageable
            );

        } else if (!filtroCodigo.isEmpty()) {

            empleados = empleadoService.buscarPorCodigoEmpleado(
                    filtroCodigo,
                    pageable
            );

        } else if (!filtroDepartamento.isEmpty()) {

            empleados = empleadoService.buscarPorDepartamento(
                    filtroDepartamento,
                    pageable
            );

        } else {

            empleados = empleadoService.buscarTodosPaginados(
                    pageable
            );
        }

        model.addAttribute(
                "empleados",
                empleados
        );

        model.addAttribute(
                "id",
                id.orElse(null)
        );

        model.addAttribute(
                "codigoEmpleado",
                filtroCodigo
        );

        model.addAttribute(
                "departamento",
                filtroDepartamento
        );

        agregarNumerosDePagina(
                model,
                empleados
        );

        return "empleados/index";
    }


    @GetMapping("/create")
    public String create(Model model) {

        Empleado empleado = new Empleado();

        /*
         * Los empleados nuevos quedan activos
         * por defecto.
         */
        empleado.setEstadoLaboral(
                EstadoEmpleado.ACTIVO
        );

        model.addAttribute(
                "empleado",
                empleado
        );

        model.addAttribute(
                "usuarios",
                usuarioService.obtenerTodos()
        );

        return "empleados/create";
    }


    @PostMapping("/save")
    public String save(
            @RequestParam("idUsuario") Integer idUsuario,
            Empleado empleado,
            BindingResult result,
            Model model,
            RedirectAttributes attributes) {

        boolean esEdicion =
                empleado.getIdEmpleado() != null;


        /*
         * ==========================================================
         * VALIDACIÓN DEL FORMULARIO
         * ==========================================================
         */

        if (result.hasErrors()) {

            model.addAttribute(
                    "usuarios",
                    usuarioService.obtenerTodos()
            );

            if (esEdicion) {
                return "empleados/edit";
            }

            return "empleados/create";
        }


        /*
         * ==========================================================
         * BUSCAR USUARIO
         * ==========================================================
         */

        Usuario usuarioSeleccionado =
                usuarioService.buscarPorId(idUsuario);

        if (usuarioSeleccionado == null) {

            model.addAttribute(
                    "usuarios",
                    usuarioService.obtenerTodos()
            );

            model.addAttribute(
                    "error",
                    "El usuario seleccionado no existe."
            );

            if (esEdicion) {
                return "empleados/edit";
            }

            return "empleados/create";
        }


        /*
         * ==========================================================
         * VALIDAR CÓDIGO DE EMPLEADO DUPLICADO
         * ==========================================================
         */

        Empleado empleadoConMismoCodigo =
                empleadoService.buscarPorCodigoEmpleado(
                        empleado.getCodigoEmpleado()
                );

        if (empleadoConMismoCodigo != null) {

            /*
             * Si estamos editando, permitimos que el código
             * pertenezca al mismo empleado.
             */
            boolean perteneceAlMismoEmpleado =
                    esEdicion
                            && empleadoConMismoCodigo
                            .getIdEmpleado()
                            .equals(empleado.getIdEmpleado());

            if (!perteneceAlMismoEmpleado) {

                model.addAttribute(
                        "usuarios",
                        usuarioService.obtenerTodos()
                );

                model.addAttribute(
                        "error",
                        "El código de empleado ya está registrado."
                );

                if (esEdicion) {
                    return "empleados/edit";
                }

                return "empleados/create";
            }
        }


        /*
         * ==========================================================
         * CREAR EMPLEADO
         * ==========================================================
         */

        if (!esEdicion) {

            /*
             * Un usuario solamente puede corresponder
             * a un empleado.
             */
            Empleado empleadoExistente =
                    empleadoService.obtenerPorUsuario(
                            usuarioSeleccionado
                    );

            if (empleadoExistente != null) {

                model.addAttribute(
                        "usuarios",
                        usuarioService.obtenerTodos()
                );

                model.addAttribute(
                        "error",
                        "El usuario seleccionado ya está asociado a un empleado."
                );

                return "empleados/create";
            }


            empleado.setUsuario(
                    usuarioSeleccionado
            );


            /*
             * Si no se recibió estado laboral,
             * queda ACTIVO por defecto.
             */
            if (empleado.getEstadoLaboral() == null) {

                empleado.setEstadoLaboral(
                        EstadoEmpleado.ACTIVO
                );
            }


            empleadoService.registrar(
                    empleado
            );

            attributes.addFlashAttribute(
                    "msg",
                    "Empleado guardado correctamente"
            );

            return "redirect:/empleados";
        }


        /*
         * ==========================================================
         * EDITAR EMPLEADO
         * ==========================================================
         */

        Empleado empleadoExistente =
                empleadoService.buscarPorId(
                        empleado.getIdEmpleado()
                );


        /*
         * Verificamos que el empleado exista.
         */
        if (empleadoExistente == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El empleado que intenta editar no existe."
            );

            return "redirect:/empleados";
        }


        /*
         * ==========================================================
         * VALIDAR USUARIO EN EDICIÓN
         * ==========================================================
         */

        Usuario usuarioAnterior =
                empleadoExistente.getUsuario();


        boolean mismoUsuario =
                usuarioAnterior != null
                        && usuarioAnterior.getId()
                        .equals(usuarioSeleccionado.getId());


        /*
         * Si el usuario seleccionado cambió,
         * verificamos que no pertenezca a otro empleado.
         */
        if (!mismoUsuario) {

            Empleado otroEmpleado =
                    empleadoService.obtenerPorUsuario(
                            usuarioSeleccionado
                    );

            if (otroEmpleado != null
                    && !otroEmpleado.getIdEmpleado()
                    .equals(empleadoExistente.getIdEmpleado())) {

                model.addAttribute(
                        "usuarios",
                        usuarioService.obtenerTodos()
                );

                model.addAttribute(
                        "error",
                        "El usuario seleccionado ya está asociado a otro empleado."
                );

                return "empleados/edit";
            }
        }


        /*
         * ==========================================================
         * ACTUALIZAR DATOS
         * ==========================================================
         */

        empleadoExistente.setUsuario(
                usuarioSeleccionado
        );

        empleadoExistente.setCodigoEmpleado(
                empleado.getCodigoEmpleado()
        );

        empleadoExistente.setDepartamento(
                empleado.getDepartamento()
        );

        empleadoExistente.setCargo(
                empleado.getCargo()
        );

        empleadoExistente.setTelefono(
                empleado.getTelefono()
        );

        empleadoExistente.setFechaIngreso(
                empleado.getFechaIngreso()
        );

        if (empleado.getEstadoLaboral() != null) {

            empleadoExistente.setEstadoLaboral(
                    empleado.getEstadoLaboral()
            );
        }


        empleadoService.actualizar(
                empleadoExistente
        );

        attributes.addFlashAttribute(
                "msg",
                "Empleado actualizado correctamente"
        );

        return "redirect:/empleados";
    }


    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        Empleado empleado =
                empleadoService.buscarPorId(id);

        if (empleado == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El empleado solicitado no existe."
            );

            return "redirect:/empleados";
        }

        model.addAttribute(
                "empleado",
                empleado
        );

        return "empleados/details";
    }


    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        Empleado empleado =
                empleadoService.buscarPorId(id);

        if (empleado == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El empleado que intenta editar no existe."
            );

            return "redirect:/empleados";
        }

        model.addAttribute(
                "empleado",
                empleado
        );

        model.addAttribute(
                "usuarios",
                usuarioService.obtenerTodos()
        );

        return "empleados/edit";
    }


    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes attributes) {

        Empleado empleado =
                empleadoService.buscarPorId(id);

        if (empleado == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El empleado que intenta eliminar no existe."
            );

            return "redirect:/empleados";
        }

        model.addAttribute(
                "empleado",
                empleado
        );

        return "empleados/delete";
    }


    @PostMapping("/delete")
    public String delete(
            Empleado empleado,
            RedirectAttributes attributes) {

        if (empleado.getIdEmpleado() == null) {

            attributes.addFlashAttribute(
                    "error",
                    "No se pudo identificar el empleado."
            );

            return "redirect:/empleados";
        }

        Empleado empleadoExistente =
                empleadoService.buscarPorId(
                        empleado.getIdEmpleado()
                );

        if (empleadoExistente == null) {

            attributes.addFlashAttribute(
                    "error",
                    "El empleado que intenta eliminar no existe."
            );

            return "redirect:/empleados";
        }

        empleadoService.eliminarPorId(
                empleado.getIdEmpleado()
        );

        attributes.addFlashAttribute(
                "msg",
                "Empleado eliminado correctamente"
        );

        return "redirect:/empleados";
    }


    private void agregarNumerosDePagina(
            Model model,
            Page<Empleado> empleados) {

        if (empleados.getTotalPages() > 0) {

            List<Integer> pageNumbers =
                    IntStream.rangeClosed(
                                    1,
                                    empleados.getTotalPages()
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