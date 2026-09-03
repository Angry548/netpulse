package com.equipo1.netpulse.security;

import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.servicios.interfaces.IUsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler
        implements AuthenticationSuccessHandler {

    private final IUsuarioService usuarioService;

    public LoginSuccessHandler(
            IUsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        String correo = authentication.getName();

        Usuario usuario =
                usuarioService.buscarPorCorreo(correo);

        if (usuario != null) {
            usuarioService.registrarAcceso(usuario);
        }

        response.sendRedirect(
                request.getContextPath() + "/index"
        );
    }
}