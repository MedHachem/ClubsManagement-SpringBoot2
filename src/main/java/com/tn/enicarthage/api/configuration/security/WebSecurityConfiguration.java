package com.tn.enicarthage.api.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JWTUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.route.authentication.path}")
    private String authenticationPath;

    @Autowired
    public WebSecurityConfiguration(JWTUtil jwtUtil, JwtUserDetailsService jwtUserDetailsService) {
        this.jwtTokenUtil = jwtUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(jwtUserDetailsService)
            .passwordEncoder(passwordEncoderBean());
    }

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity

                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                //LOGIN PATHs
                .antMatchers("/auth/**").permitAll()
         ;

                //Users Paths
/*
                //CLUUUUB PATHS
                .antMatchers(("/Clubs/All")).permitAll()
                .antMatchers("/Clubs/{id}").permitAll()
                .antMatchers("/Clubs/NewClub").hasRole("ADMIN")
                .antMatchers("/Clubs/Update/{id}").hasRole("ADMIN")
                .antMatchers("/Clubs/Delete/{id}").hasRole("ADMIN")
                //Event PATHS
                .antMatchers("/Events/countEvents").permitAll()
                .antMatchers("/Events/{id}").permitAll()
                .antMatchers(("/Events/All")).permitAll()
                .antMatchers("/Events/by-club-id/{clubIds}").permitAll()
                .antMatchers("/Events/NewEvent").hasAnyRole("ADMIN","CLUBPRESIDENT")
                .antMatchers("/Events/Update/{id}").hasAnyRole("ADMIN","CLUBPRESIDENT")
                .antMatchers("/Events/Delete/{id}").hasAnyRole("ADMIN","CLUBPRESIDENT")
//Membres PATHS
                .antMatchers("/Membres/countMembres").hasRole("CLUBPRESIDENT")
                .antMatchers("/Membres/{id}").permitAll()
                .antMatchers(("/Membres/All")).hasRole("CLUBPRESIDENT")
                .antMatchers("/Membres/NewMembre").hasAnyRole("ADMIN","CLUBPRESIDENT")
                .antMatchers("/Membres/Update/{id}").hasAnyRole("ADMIN","CLUBPRESIDENT")
                .antMatchers("/Membres/Delete/{id}").hasAnyRole("ADMIN","CLUBPRESIDENT")

                .anyRequest().authenticated();*/
        JWTTokenFilter authenticationTokenFilter = new JWTTokenFilter(userDetailsService(), jwtTokenUtil, tokenHeader);
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity
                .headers()
                .frameOptions().sameOrigin()
                .cacheControl();
    }







}