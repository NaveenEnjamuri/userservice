package com.usermanagement.configuration.security;

import com.usermanagement.configuration.authentication.AuthFailureHandler;
import com.usermanagement.configuration.authentication.AuthenticationSuccessHandler;
import com.usermanagement.configuration.authentication.CustomAuthenticationHandler;
import com.usermanagement.configuration.authentication.SignoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationProvider")
    private CustomAuthenticationHandler authenticationHandler;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthFailureHandler failureHandler;

    @Autowired
    private SignoutSuccessHandler signoutSuccessHandler;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${usermanagement.rememberme.timeout}")
    private int remembermeTimeout;

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.authenticationProvider(authenticationHandler);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /*.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)*/
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource()).and()
                .exceptionHandling().accessDeniedPage("/403")
                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .formLogin()
                .loginPage("/403")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(failureHandler)
                .permitAll()
                .usernameParameter("username")
                .passwordParameter("password");
        http
                .authorizeRequests()
                .antMatchers("/v2/api-docs", "/swagger-resources/configuration/ui", "/swagger-resources",
                        "/swagger-resources/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
                .antMatchers("/login", "/registration", "/role/**", "/verify-account", "/user/username-exists",
                        "/user/email-exists", "/user/phone-exists", "/user/send-reset-password-steps", "/user/reset-password",
                        "/contact/save", "/authenticationFailure").permitAll()
                .antMatchers("/index.html", "/home.html", "/assets/**", "/user").permitAll().anyRequest()
                .authenticated();
        http
                .logout()
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true).clearAuthentication(true).logoutSuccessHandler(signoutSuccessHandler);
//        http
//                .rememberMe().key("rem-me-key").rememberMeParameter("rememberMe").rememberMeCookieName("rememberMe").tokenRepository(persistentTokenRepository)
//                .tokenValiditySeconds(remembermeTimeout);
        http.headers().cacheControl().disable();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(@Autowired DataSource dataSource) {
        JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setDataSource(dataSource);
        return jdbcTokenRepositoryImpl;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList(RequestMethod.POST.name(), RequestMethod.GET.name(),
                RequestMethod.OPTIONS.name(), RequestMethod.DELETE.name(),
                RequestMethod.PUT.name(), RequestMethod.PATCH.name()));
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin, Accept, Access-Control-Allow-Origin, X-Auth-Token, X-Auth-Login, X-Auth-Password, X-Auth"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }

}
