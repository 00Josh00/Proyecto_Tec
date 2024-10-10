package pe.authentique.inventario.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pe.authentique.inventario.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                //Configurar el login
                .formLogin(form -> form.loginPage("/login").permitAll()
                )
                //configurar los acceso o permisos por rutas/URL's
                .authorizeHttpRequests((authz)-> authz
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/productos/**", "/productos").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(customizer-> customizer.accessDeniedHandler(accessDeniedHandlearApp()))
                .userDetailsService(userDetailsServiceImpl)
                .logout(logout->logout
                        .logoutRequestMatcher(
                                new AntPathRequestMatcher("/logout", "GET")).logoutSuccessUrl("/")
                );
        return http.build();
    }

    @Bean
    AccessDeniedHandler accessDeniedHandlearApp()
    {
        return ( (request, response, accessDeniedException) ->
                response.sendRedirect(request.getContextPath() + "/403"));
    }

}