package learn.ai.tinder.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

/**
 * Security configuration for the application.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig {

    /**
     * Configures security filters and permissions.
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() } // Disable CSRF for API
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/api/v1/users/register",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic { httpBasic -> httpBasic.disable() } // Disable basic auth
            
        return http.build()
    }

    /**
     * Provides a password encoder bean for hashing passwords.
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}

