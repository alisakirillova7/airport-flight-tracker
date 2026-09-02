package com.company.airport.security;

import io.jmix.securityflowui.security.FlowuiVaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class AirportSecurityConfiguration extends FlowuiVaadinWebSecurity {
    // Оставляем пустым. Jmix сам подтянет базовую защиту и пустит admin/admin
}