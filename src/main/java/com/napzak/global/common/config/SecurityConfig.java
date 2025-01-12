package com.napzak.global.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.napzak.domain.store.core.Role;
import com.napzak.global.auth.jwt.filter.JwtAuthenticationFilter;
import com.napzak.global.auth.security.CustomAccessDeniedHandler;
import com.napzak.global.auth.security.CustomJwtAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomJwtAuthenticationEntryPoint customJwtAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	private static final String[] AUTH_WHITELIST = {
		"/health-check",
		"/actuator/health",
		"/v3/api-docs/**",
		"/swagger-ui/**",
		"/swagger-resources/**",
		"/api/files/**",
		"/error",
		"/api/stores/login/**",
		"/"
	};

	private static final String[] AUTH_ADMIN_ONLY = {
		"/api/admin/**"
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exception ->
				exception.authenticationEntryPoint(customJwtAuthenticationEntryPoint)
					.accessDeniedHandler(customAccessDeniedHandler));

		http.authorizeHttpRequests(auth ->
				auth.requestMatchers(AUTH_WHITELIST).permitAll()
					.requestMatchers(AUTH_ADMIN_ONLY).hasAuthority(Role.ADMIN.getRoleName())
					.anyRequest().authenticated())
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
