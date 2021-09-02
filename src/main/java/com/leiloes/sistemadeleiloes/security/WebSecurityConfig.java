package com.leiloes.sistemadeleiloes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    ImplementsUserDetailsService implementsUserDetailsService;

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET, "/registrar").permitAll()
                .antMatchers(HttpMethod.POST, "/usuario/new").permitAll()
                .antMatchers(HttpMethod.GET, "/leilao/list").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/leilao/list").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/leilao/list/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/leilao/new").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/leilao/new").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/leilao/update/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/leilao/update/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/lance/list").hasRole("ADMIN")

                .anyRequest().authenticated()
                .and().formLogin().successHandler(loginSuccessHandler).permitAll()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll().logoutSuccessUrl("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(implementsUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/bootstrap/**");
    }
}
