package com.alliex.cvs.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationProvider authenticationProvider;

    public SecurityConfig(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/logout/**").permitAll()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/events/*").hasRole("ADMIN")
                .antMatchers("/**").hasRole("USER")

                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout/success")

                .and()
                .csrf().disable()

                .addFilter(jwtAuthenticationFilter())

                .exceptionHandling()
                .accessDeniedPage("/errors/403")
                .authenticationEntryPoint(loginUrlAuthenticationEntryPoint());

        http.headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**")
                .antMatchers("/css/**");
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");
        jwtAuthenticationFilter.setUsernameParameter("username");
        jwtAuthenticationFilter.setPasswordParameter("password");

        jwtAuthenticationFilter.setAuthenticationSuccessHandler(
                new SavedRequestAwareAuthenticationSuccessHandler() {{
                    setDefaultTargetUrl("/index");
                }}
        );

        jwtAuthenticationFilter.setAuthenticationFailureHandler(
                new SimpleUrlAuthenticationFailureHandler() {{
                    setDefaultFailureUrl("/login/form?error=e");
                }}
        );

        jwtAuthenticationFilter.afterPropertiesSet();

        return jwtAuthenticationFilter;
    }

    @Bean
    public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint("/login/form");
    }

}
