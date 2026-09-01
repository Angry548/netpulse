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

    @Bean
    public UserDetailsManager customUsers(DataSource dataSource) {

        JdbcUserDetailsManager users =
                new JdbcUserDetailsManager(dataSource);

        /*
         * LOGIN:
         * El usuario escribe su CORREO.
         *
         * La tabla usuarios contiene:
         * id_usuario
         * id_rol
         * nombre
         * correo
         * contrasena
         * activo
         * fecha_creacion
         * ultimo_acceso
         */
        users.setUsersByUsernameQuery(
                "SELECT correo, contrasena, activo " +
                        "FROM usuarios " +
                        "WHERE correo = ?"
        );

        /*
         * ROLES:
         *
         * usuarios.id_rol -> roles.id_rol
         *
         * Spring Security necesita:
         * username, authority
         */
        users.setAuthoritiesByUsernameQuery(
                "SELECT u.correo, r.nombre " +
                        "FROM usuarios u " +
                        "INNER JOIN roles r ON u.id_rol = r.id_rol " +
                        "WHERE u.correo = ?"
        );

        return users;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize

                        // ==========================================
                        // RECURSOS PÚBLICOS
                        // ==========================================

                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/assets/**",
                                "/webjars/**"
                        ).permitAll()


                        // ==========================================
                        // LOGIN
                        // ==========================================

                        .requestMatchers(
                                "/login"
                        ).permitAll()


                        // ==========================================
                        // PÁGINA PRINCIPAL
                        // ==========================================

                        .requestMatchers("/")
                        .authenticated()


                        // ==========================================
                        // ROLES
                        // ==========================================

                        .requestMatchers("/roles/**")
                        .hasAuthority("admin")


                        // ==========================================
                        // TODO LO DEMÁS
                        // REQUIERE INICIAR SESIÓN
                        // ==========================================

                        .anyRequest()
                        .authenticated()
                )


                // ==========================================
                // FORMULARIO DE LOGIN
                // ==========================================

                .formLogin(form -> form

                        .loginPage("/login")

                        /*
                         * Spring recibirá:
                         *
                         * username = correo
                         * password = contraseña
                         *
                         * Aunque el campo se llame "username",
                         * Spring lo utilizará como correo porque
                         * nuestra consulta busca por correo.
                         */

                        .defaultSuccessUrl("/", true)

                        .failureUrl("/login?error=true")

                        .permitAll()
                )


                // ==========================================
                // LOGOUT
                // ==========================================

                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/login?logout=true")

                        .invalidateHttpSession(true)

                        .deleteCookies("JSESSIONID")

                        .permitAll()
                );

        return http.build();
    }


    // ==========================================
    // ENCODER DE CONTRASEÑAS
    // ==========================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}