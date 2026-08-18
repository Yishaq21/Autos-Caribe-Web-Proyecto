/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.autoscaribe;

import com.autoscaribe.domain.Ruta;
import com.autoscaribe.service.RutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Esta clase configura toda la seguridad del sistema
 */
@Configuration
public class SecurityConfig {

    // Este método arma qué puede ver cada quien.
    // @Lazy evita un problema de dependencias circulares al arrancar la app.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Lazy RutaService rutaService) throws Exception {

        var rutas = rutaService.getRutas();
        System.out.println("========== RUTAS CARGADAS AL ARRANCAR ==========");
        for (var r : rutas) {
            System.out.println(r.getRuta() + " | requiereRol=" + r.isRequiereRol());
        }
        System.out.println("==================================================");

http.authorizeHttpRequests(request -> {
            for (Ruta ruta : rutas) {
                if (ruta.isRequiereRol()) {
                    String dbRol = ruta.getRol().getRol(); // Trae el rol de la BD 
                
                    if (dbRol.startsWith("ROLE_")) {
                        dbRol = dbRol.substring(5);
                    }
                    
   
                    if (dbRol.equals("ADMIN")) {
                        request.requestMatchers(ruta.getRuta()).hasRole("ADMIN");
                    } else {

                        request.requestMatchers(ruta.getRuta()).hasAnyRole(dbRol, "ADMIN");
                    }
                } else {
                    request.requestMatchers(ruta.getRuta()).permitAll();
                }
            }
            request.anyRequest().authenticated();
        });
        // Configuración del formulario de login: usa nuestra propia
        http.formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        );

        // Configuración del logout: al cerrar sesión, borra la sesión
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );

        // Si alguien intenta entrar a una ruta sin el rol necesario,
        // lo manda a una página de "acceso denegado"
        http.exceptionHandling(ex -> ex.accessDeniedPage("/acceso_denegado"));

        return http.build();
    }

    // Define el algoritmo para encriptar contraseñas (BCrypt).
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Le dice a Spring Security que use nuestro UsuarioDetailsService para validar cada intento de login.
    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build,
            @Lazy PasswordEncoder passwordEncoder,
            @Lazy UserDetailsService userDetailsService) throws Exception {
        build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
