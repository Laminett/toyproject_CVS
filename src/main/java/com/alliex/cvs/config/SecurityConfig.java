
package com.alliex.cvs.config;

import com.alliex.cvs.domain.type.Role;
import com.alliex.cvs.security.JwtAuthenticationTokenFilter;
import com.alliex.cvs.security.provider.ApiUserAuthenticationProvider;
import com.alliex.cvs.security.provider.CustomUserAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @Component
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private ApiUserAuthenticationProvider apiUserAuthenticationProvider;

        @Autowired
        public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) {
            authenticationManagerBuilder
                    .authenticationProvider(apiUserAuthenticationProvider);
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Bean
        public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilterBean() {
            return new JwtAuthenticationTokenFilter();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/login/**").permitAll()
                .antMatchers("/api/**").hasRole(Role.USER.name());

            http
                .csrf()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            // JWT based authentication
            http
                .addFilterBefore(jwtAuthenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        }
    }

    @Configuration
    @Component
    @Order(2)
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        private CustomUserAuthenticationProvider customUserAuthenticationProvider;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) {
            auth.authenticationProvider(customUserAuthenticationProvider);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/css/**", "/js/**", "/img/**", "/material-dashboard/**").permitAll()
                    .antMatchers("/login/**", "/logout/**").permitAll()
                    .antMatchers("/ping").permitAll()
                    .antMatchers("/**").hasRole(Role.ADMIN.name());

            http.formLogin()
                    .defaultSuccessUrl("/")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll();

            http.logout()
                    .logoutUrl("/logout") // default
                    .logoutSuccessUrl("/login")
                    .permitAll();

            http.csrf().disable();
        }
    }

}
