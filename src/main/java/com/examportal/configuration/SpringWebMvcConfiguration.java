package com.examportal.configuration;

import com.examportal.configuration.authentication.SecurityParams;
import com.examportal.configuration.authentication.SecurityParamsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
@ComponentScan("com.examportal")
@EnableWebMvc
@EnableRedisHttpSession
@PropertySource(value = {"classpath:mail:properties"})
public class SpringWebMvcConfiguration extends WebMvcConfigurationSupport {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters ) {
        converters.add(converter());
        converters.add(new ResourceHttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter converter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityParams securityParams() {
        return new SecurityParamsImpl();
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public HeaderHttpSessionIdResolver sessionStrategy() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }
}
