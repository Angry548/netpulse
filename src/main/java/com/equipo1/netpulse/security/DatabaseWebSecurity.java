package com.equipo1.netpulse.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity {

    private final LoginSuccessHandler loginSuccessHandler;

    public DatabaseWebSecurity(
            LoginSuccessHandler loginSuccessHandler) {

        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Bean
    public UserDetailsManager customUsers(DataSource dataSource) {

        JdbcUserDetailsManager users =
                new JdbcUserDetailsManager(dataSource);

        users.setUsersByUsernameQuery(
                "SELECT correo, contrasena, activo " +
                        "FROM usuarios " +
                        "WHERE correo = ?"
        );

        users.setAuthoritiesByUsernameQuery(
                "SELECT u.correo, r.nombre " +
                        "FROM usuarios u " +
                        "INNER JOIN roles r " +
                        "ON u.id_rol = r.id_rol " +
                        "WHERE u.correo = ?"
        );

        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/assets/**",
                                "/webjars/**"
                        ).permitAll()

                        .requestMatchers("/login")
                        .permitAll()

                        .requestMatchers("/index")
                        .authenticated()

                        .requestMatchers("/usuarios/**")
                        .hasAuthority("Administrador")

                        .requestMatchers("/roles/**")
                        .hasAuthority("Administrador")

                        .requestMatchers("/empleados/**")
                        .hasAuthority("Administrador")

                        .requestMatchers("/equipos/**")
                        .hasAnyAuthority(
                                "Administrador",
                                "Técnico"
                        )

                        .requestMatchers("/tipos-equipo/**")
                        .hasAuthority("Administrador")

                        .requestMatchers("/estados-equipo/**")
                        .hasAuthority("Administrador")

                        .requestMatchers("/tickets/**")
                        .hasAnyAuthority(
                                "Administrador",
                                "Técnico",
                                "Usuario"
                        )

                        .requestMatchers("/categorias-incidencia/**")
                        .hasAuthority("Administrador")

                        .requestMatchers("/prioridades-ticket/**")
                        .hasAuthority("Administrador")

                        .requestMatchers("/estados-ticket/**")
                        .hasAuthority("Administrador")

                        .requestMatchers("/mantenimientos/**")
                        .hasAnyAuthority(
                                "Administrador",
                                "Técnico"
                        )

                        .requestMatchers("/dashboard/**")
                        .hasAuthority("Administrador")

                        .requestMatchers("/alertas-red/**")
                        .hasAnyAuthority(
                                "Administrador",
                                "Técnico"
                        )

                        .requestMatchers("/historial-estados/**")
                        .hasAnyAuthority(
                                "Administrador",
                                "Técnico"
                        )

                        .requestMatchers("/reportes/**")
                        .hasAnyAuthority(
                                "Administrador",
                                "Técnico"
                        )

                        .requestMatchers("/perfil/**")
                        .authenticated()

                        .requestMatchers("/configuracion/**")
                        .hasAuthority("Administrador")

                        .requestMatchers(
                                "/privacidad",
                                "/terminos"
                        )
                        .authenticated()

                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")

                        /*
                         * Cuando el login sea exitoso:
                         * 1. Se busca el usuario.
                         * 2. Se actualiza ultimo_acceso.
                         * 3. Se redirige a /index.
                         */
                        .successHandler(loginSuccessHandler)

                        .failureUrl(
                                "/login?error=true"
                        )

                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")

                        .logoutSuccessUrl(
                                "/login?logout=true"
                        )

                        .invalidateHttpSession(true)

                        .deleteCookies("JSESSIONID")

                        .permitAll()
                );

        return http.build();
    }

}