package com.equipo1.netpulse.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity {

    // =========================================================
    // USUARIOS DESDE LA BASE DE DATOS
    // =========================================================

    @Bean
    public UserDetailsManager customUsers(DataSource dataSource) {

        JdbcUserDetailsManager users =
                new JdbcUserDetailsManager(dataSource);

        /*
         * Spring Security recibe:
         *
         * username = correo
         * password = contrasena
         * enabled  = activo
         */

        users.setUsersByUsernameQuery(
                "SELECT correo, contrasena, activo " +
                        "FROM usuarios " +
                        "WHERE correo = ?"
        );

        /*
         * Obtener el rol del usuario.
         *
         * usuarios.id_rol -> roles.id_rol
         */

        users.setAuthoritiesByUsernameQuery(
                "SELECT u.correo, r.nombre " +
                        "FROM usuarios u " +
                        "INNER JOIN roles r " +
                        "ON u.id_rol = r.id_rol " +
                        "WHERE u.correo = ?"
        );

        return users;
    }


    // =========================================================
    // CONFIGURACIÓN DE SEGURIDAD
    // =========================================================

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http

                // =================================================
                // AUTORIZACIONES
                // =================================================

                .authorizeHttpRequests(authorize -> authorize

                        // -------------------------------
                        // Archivos públicos
                        // -------------------------------

                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/assets/**",
                                "/webjars/**"
                        ).permitAll()


                        // -------------------------------
                        // Login público
                        // -------------------------------

                        .requestMatchers("/login")
                        .permitAll()


                        // -------------------------------
                        // Página principal
                        // -------------------------------

                        .requestMatchers("/index")
                        .authenticated()


                        // -------------------------------
                        // Roles
                        // -------------------------------

                        .requestMatchers("/roles/**")
                        .hasAuthority("admin")


                        // -------------------------------
                        // Todo lo demás necesita login
                        // -------------------------------

                        .anyRequest()
                        .authenticated()
                )


                // =================================================
                // LOGIN
                // =================================================

                .formLogin(form -> form

                        // Página personalizada
                        .loginPage("/login")

                        // Después de iniciar sesión:
                        // ir SIEMPRE a /index
                        .defaultSuccessUrl("/index", true)

                        // Si las credenciales son incorrectas
                        .failureUrl("/login?error=true")

                        .permitAll()
                )


                // =================================================
                // LOGOUT
                // =================================================

                .logout(logout -> logout

                        // Spring Security manejará POST /logout
                        .logoutUrl("/logout")

                        // Después de cerrar sesión
                        .logoutSuccessUrl("/login?logout=true")

                        // Eliminar sesión
                        .invalidateHttpSession(true)

                        // Eliminar cookie
                        .deleteCookies("JSESSIONID")

                        .permitAll()
                );


        return http.build();
    }


    // =========================================================
    // PASSWORD ENCODER
    // =========================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
