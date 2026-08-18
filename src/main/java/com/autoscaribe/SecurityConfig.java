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
 * Configura toda la seguridad del sistema.
 */
@Configuration
public class SecurityConfig {

    // Define qué rutas puede ver cada usuario según su rol.@Lazy evita errores de dependencias circulares al iniciar
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Lazy RutaService rutaService) throws Exception {
        
        // Trae todas las rutas desde la base de datos
        var rutas = rutaService.getRutas();
        // Configura los permisos de cada ruta
        http.authorizeHttpRequests(request -> {
            for (Ruta ruta : rutas) {
                if (ruta.isRequiereRol()) {
                    // Obtiene el rol desde la BD
                    String dbRol = ruta.getRol().getRol();
                    // Quita el prefijo "ROLE_" si lo tiene
                    if (dbRol.startsWith("ROLE_")) {
                        dbRol = dbRol.substring(5);
                    }

                    // Si el rol es ADMIN, solo admin puede entrar
                    if (dbRol.equals("ADMIN")) {
                        request.requestMatchers(ruta.getRuta()).hasRole("ADMIN");
                    } else {
                        // Otros roles: ese rol o ADMIN pueden entrar
                        request.requestMatchers(ruta.getRuta()).hasAnyRole(dbRol, "ADMIN");
                    }
                } else {
                    // Si no requiere rol, cualquiera puede entrar
                    request.requestMatchers(ruta.getRuta()).permitAll();
                }
            }
            // Cualquier otra ruta necesita estar logueado
            request.anyRequest().authenticated();
        });

        // Configura el formulario de login
        http.formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        );

        // Configura el cierre de sesión
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );
        // Si no tiene permiso, lo manda a esta pagina
        http.exceptionHandling(ex -> ex.accessDeniedPage("/acceso_denegado"));
        return http.build();
    }

    // Define como se encriptan las contraseñas (BCrypt)

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Le dice a Spring que use nuestro servicio de usuarios y el codificador de contraseñas para validar el login
    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build,
            @Lazy PasswordEncoder passwordEncoder,
            @Lazy UserDetailsService userDetailsService) throws Exception {
        build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}