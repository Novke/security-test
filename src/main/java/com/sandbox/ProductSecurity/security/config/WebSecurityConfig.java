package com.sandbox.ProductSecurity.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

@Configuration
public class WebSecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    SecurityContextRepository securityContextRepository(){
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());

        http.authorizeHttpRequests(authz -> authz
//                .requestMatchers(HttpMethod.GET, "/couponapi/coupons/**").hasAnyRole("USER", "ADMIN")

                //NEW
                .requestMatchers(HttpMethod.GET, "/couponapi/coupons/{code:^[A-Z]*$}").authenticated()
//                .requestMatchers(HttpMethod.POST, "/couponapi/coupons").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/couponapi/coupons").authenticated()

                        .requestMatchers("/", "/login", "/showReg", "/registerUser").permitAll()

                //DEFAULT
                        .anyRequest().authenticated()
        );

        //NEW
        http.logout(url -> url
                .logoutSuccessUrl("/")
                .deleteCookies("kolacici")
                .invalidateHttpSession(true) //default
        );

        //NEW
//        http.csrf(AbstractHttpConfigurer::disable);
        http.csrf(
                csrf -> {
                    csrf
                            .ignoringRequestMatchers("/couponapi/coupons/**");

                    RequestMatcher matcher = new RegexRequestMatcher("/couponapi/coupons/{code:^[A-Z]*$", "POST");
                    matcher = new MvcRequestMatcher(new HandlerMappingIntrospector(), "/getCoupon");
                    csrf.ignoringRequestMatchers(matcher);
                }
        );

        //kad se doda csrf /logout link nece raditi vec moram da napravim dugme za logout

         //CORS - kada se pristupa sa drugog domena / porta
        // access control allow origin - sa kojih izvora
        // access control allow methods - koje metode
        // access control allow headers - koji header-i

        http.cors(cors -> {
            CorsConfigurationSource configurationSource = request -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();

                corsConfiguration.setAllowedOrigins(List.of("localhost:3000"));
                corsConfiguration.setAllowedMethods(List.of("GET"));
                return corsConfiguration;
            };
            cors.configurationSource(configurationSource);
        });

        //NEW
        http.securityContext(context -> context.requireExplicitSave(true));

        return http.build();
    }
}
