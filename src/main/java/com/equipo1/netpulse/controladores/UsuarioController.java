package com.equipo1.netpulse.controladores;

import com.equipo1.netpulse.modelos.Rol;
import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.repositorios.IRolRepository;
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
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final IRolRepository rolRepository;

    public UsuarioController(
            IUsuarioService usuarioService,
            IRolRepository rolRepository) {

        this.usuarioService = usuarioService;
        this.rolRepository = rolRepository;
    }

    @GetMapping
    public String index(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("id") Optional<Integer> id,
            @RequestParam("nombre") Optional<String> nombre,
            @RequestParam("correo") Optional<String> correo) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);

        Pageable pageable = PageRequest.of(currentPage, pageSize);

        String filtroNombre = nombre.orElse("").trim();
        String filtroCorreo = correo.orElse("").trim();

        Page<Usuario> usuarios;

        if (id.isPresent()) {

            usuarios = usuarioService.buscarPorIdPaginado(
                    id.get(),
                    pageable
            );

        } else if (!filtroNombre.isEmpty()) {

            usuarios = usuarioService.buscarPorNombre(
                    filtroNombre,
                    pageable
            );

        } else if (!filtroCorreo.isEmpty()) {

            usuarios = usuarioService.buscarPorCorreoPaginado(
                    filtroCorreo,
                    pageable
            );

        } else {

            usuarios = usuarioService.buscarTodosPaginados(
                    pageable
            );
        }

        model.addAttribute(
                "usuarios",
                usuarios
        );

        model.addAttribute(
                "id",
                id.orElse(null)
        );

        model.addAttribute(
                "nombre",
                filtroNombre
        );

        model.addAttribute(
                "correo",
                filtroCorreo
        );

        agregarNumerosDePagina(
                model,
                usuarios
        );

        return "usuarios/index";
    }

    @GetMapping("/create")
    public String create(
            Model model) {

        Usuario usuario = new Usuario();

        model.addAttribute(
                "usuario",
                usuario
        );

        model.addAttribute(
                "roles",
                rolRepository.findAll()
        );

        return "usuarios/create";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam("idRol") Integer idRol,
            Usuario usuario,
            BindingResult result,
            Model model,
            RedirectAttributes attributes) {

        /*
         * Si el ID existe, estamos editando un usuario.
         * Si el ID es null, estamos creando uno nuevo.
         */
        boolean esEdicion = usuario.getId() != null;

        /*
         * Validación de errores de formulario.
         */
        if (result.hasErrors()) {

            model.addAttribute(
                    "roles",
                    rolRepository.findAll()
            );

            if (esEdicion) {
                return "usuarios/edit";
            }

            return "usuarios/create";
        }

        /*
         * Buscamos el rol seleccionado.
         */
        Optional<Rol> rol = rolRepository.findById(idRol);

        if (rol.isEmpty()) {

            model.addAttribute(
                    "roles",
                    rolRepository.findAll()
            );

            model.addAttribute(
                    "error",
                    "El rol seleccionado no existe."
            );

            if (esEdicion) {
                return "usuarios/edit";
            }

            return "usuarios/create";
        }

        /*
         * ==========================================================
         * EDITAR USUARIO
         * ==========================================================
         */
        if (esEdicion) {

            Usuario usuarioExistente =
                    usuarioService.buscarPorId(usuario.getId());

            /*
             * Verificamos que el usuario realmente exista.
             */
            if (usuarioExistente == null) {

                attributes.addFlashAttribute(
                        "error",
                        "El usuario que intenta editar no existe."
                );

                return "redirect:/usuarios";
            }

            /*
             * Actualizamos solamente los campos que se pueden
             * modificar desde el formulario.
             */
            usuarioExistente.setNombre(
                    usuario.getNombre()
            );

            usuarioExistente.setCorreo(
                    usuario.getCorreo()
            );

            usuarioExistente.setRol(
                    rol.get()
            );

            /*
             * Si el checkbox de activo llegó con un valor,
             * lo actualizamos.
             *
             * Si llegó null, conservamos el valor anterior.
             */
            if (usuario.getActivo() != null) {

                usuarioExistente.setActivo(
                        usuario.getActivo()
                );
            }

            /*
             * IMPORTANTE:
             *
             * Si la contraseña viene vacía, NO modificamos
             * la contraseña que ya tiene el usuario.
             *
             * Si el usuario escribió una nueva contraseña,
             * entonces sí la actualizamos.
             */
            if (usuario.getContrasena() != null
                    && !usuario.getContrasena().isBlank()) {

                usuarioExistente.setContrasena(
                        usuario.getContrasena()
                );
            }

            /*
             * Guardamos el usuario existente.
             */
            usuarioService.actualizar(
                    usuarioExistente
            );

            attributes.addFlashAttribute(
                    "msg",
                    "Usuario actualizado correctamente"
            );

            return "redirect:/usuarios";
        }

        /*
         * ==========================================================
         * CREAR USUARIO
         * ==========================================================
         */

        usuario.setRol(
                rol.get()
        );

        /*
         * Los usuarios nuevos quedan activos por defecto
         * si el formulario no envía el valor.
         */
        if (usuario.getActivo() == null) {

            usuario.setActivo(true);
        }

        /*
         * Guardamos el nuevo usuario.
         */
        usuarioService.registrar(
                usuario
        );

        attributes.addFlashAttribute(
                "msg",
                "Usuario guardado correctamente"
        );

        return "redirect:/usuarios";
    }

    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Integer id,
            Model model) {

        Usuario usuario = usuarioService.buscarPorId(
                id
        );

        model.addAttribute(
                "usuario",
                usuario
        );

        return "usuarios/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model) {

        Usuario usuario = usuarioService.buscarPorId(
                id
        );

        model.addAttribute(
                "usuario",
                usuario
        );

        model.addAttribute(
                "roles",
                rolRepository.findAll()
        );

        return "usuarios/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable Integer id,
            Model model) {

        Usuario usuario = usuarioService.buscarPorId(
                id
        );

        model.addAttribute(
                "usuario",
                usuario
        );

        return "usuarios/delete";
    }

    @PostMapping("/delete")
    public String delete(
            Usuario usuario,
            RedirectAttributes attributes) {

        usuarioService.eliminarPorId(
                usuario.getId()
        );

        attributes.addFlashAttribute(
                "msg",
                "Usuario eliminado correctamente"
        );

        return "redirect:/usuarios";
    }

    private void agregarNumerosDePagina(
            Model model,
            Page<Usuario> usuarios) {

        if (usuarios.getTotalPages() > 0) {

            List<Integer> pageNumbers =
                    IntStream.rangeClosed(
                                    1,
                                    usuarios.getTotalPages()
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