package com.equipo1.netpulse.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Al entrar al sistema:
    // http://localhost:8080/
    // siempre manda al formulario de login
    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    // Mostrar formulario de inicio de sesión
    @GetMapping("/login")
    public String mostrarLogin() {
        return "home/formLogin";
    }

    // Página principal DESPUÉS de iniciar sesión
    @GetMapping("/index")
    public String index() {
        return "home/index";
    }
}
