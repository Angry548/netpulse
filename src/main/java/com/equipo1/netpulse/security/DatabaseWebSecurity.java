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


                        .requestMatchers("/roles/**")
                        .hasAuthority("Administrador")


                        .anyRequest()
                        .authenticated()
                )


                .formLogin(form -> form

                        .loginPage("/login")

                        .defaultSuccessUrl("/index", true)

                        .failureUrl("/login?error=true")

                        .permitAll()
                )

                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/login?logout=true")

                        .invalidateHttpSession(true)

                        .deleteCookies("JSESSIONID")

                        .permitAll()
                );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}