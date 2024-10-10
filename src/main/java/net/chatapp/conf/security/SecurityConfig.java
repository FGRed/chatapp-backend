package net.chatapp.conf.security;

import net.chatapp.handler.CLogoutHandler;
import net.chatapp.interceptor.BeforeRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public SessionRegistry sessionRegistry;

    @Autowired
    private CLogoutHandler cLogoutHandler;

    @Bean
    public CLogoutHandler customLogoutSuccessHandler(){
        return new CLogoutHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
               /* "http://localhost:3000",
                "http://192.168.1.191:3000",
                "http://chatapp-frontend:3000",
                "http://chatapp.net.local:3000",
                "http://chatapp.net:3000",*/
                "https://chatapp-react-7m2q.onrender.com"
        ));
        configuration.setAllowedMethods(List.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        /*source.registerCorsConfiguration("http://localhost:3000", configuration);
        source.registerCorsConfiguration("http://chatapp-frontend:3000", configuration);
        source.registerCorsConfiguration("http://chatapp.net.local:3000", configuration);
        source.registerCorsConfiguration("http://chatapp.net:3000", configuration);*/
        source.registerCorsConfiguration("https://chatapp-react-7m2q.onrender.com", configuration);
        http.authorizeRequests()
                .antMatchers("/**").permitAll().anyRequest().authenticated()
                .and().logout().logoutUrl("/session/logout").addLogoutHandler(cLogoutHandler)
                .and().csrf().disable()
                .cors().configurationSource(request -> configuration)
                .and()
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/session/session-expired").and().invalidSessionUrl("/session/session-expired");
    }

}
