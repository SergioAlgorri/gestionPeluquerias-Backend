package gestionPeluqueria.security.config;

import gestionPeluqueria.security.JwtAuthenticationEntryPoint;
import gestionPeluqueria.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static gestionPeluqueria.security.config.ApplicationConfig.corsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(c -> c.configurationSource(corsFilter()))
            .exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(authenticationEntryPoint))
            .sessionManagement(sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize ->
                    authorize
                        .requestMatchers("/recuperar_contraseña/**", "/auth/**").permitAll()
                        // HairdresserController
                        .requestMatchers(HttpMethod.POST, "/peluquerias").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/peluquerias/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/peluquerias/{id}").hasAuthority("ADMIN")
                        // Reward Controller
                        .requestMatchers(HttpMethod.POST, "/peluquerias/recompensas").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/peluquerias/recompensas/{id}")
                            .hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/peluquerias/recompensas/{id}")
                            .hasAuthority("ADMIN")
                        // ServiceController
                        .requestMatchers(HttpMethod.POST, "/peluquerias/servicios").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/peluquerias/servicios/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/peluquerias/servicios/{id}")
                            .hasAuthority("ADMIN")
                        // UserController
                        .requestMatchers(HttpMethod.GET, "/usuarios")
                            .hasAnyAuthority("ADMIN", "EMPLOYEE", "CLIENT")
                        .requestMatchers(HttpMethod.GET, "/usuarios/{idUsuario}")
                            .hasAnyAuthority("CLIENT", "ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/usuarios")
                            .hasAnyAuthority("ADMIN", "EMPLOYEE")
                        // AppointmentController
                        .requestMatchers(HttpMethod.GET, "/peluquerias/{idPeluqueria}/citas")
                            .hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/peluquerias/{idPeluqueria}/citas/{idCita}")
                            .hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/usuarios/{idUsuario}/historico_citas")
                            .hasAuthority("CLIENT")
                        .requestMatchers(HttpMethod.PUT, "usuarios/{idUsuario}/citas/{idCita}")
                            .hasAnyAuthority("ADMIN", "EMPLOYEE")
                        // El resto de endpoints cualquier ROL puede ejecutarlo
                        .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        http.addFilterBefore(jwtRequestFilter(),
                    UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}