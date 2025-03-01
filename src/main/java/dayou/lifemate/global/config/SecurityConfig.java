package dayou.lifemate.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import dayou.lifemate.global.jwt.JwtAuthenticationFilter;
import dayou.lifemate.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtProvider provider;

	@Bean
	public JwtAuthenticationFilter filter() {
		return new JwtAuthenticationFilter(provider);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth ->
				auth.
					requestMatchers(HttpMethod.GET, "/todos/v1/**", "/reviews/v1/**").permitAll()
					.requestMatchers(
						"/members/v1/join",
						"/members/v1/login",
						"/members/v1/mentor",
						"/members/v1/mentor/{mentorId}"
					)
					.permitAll()
					.anyRequest().permitAll())
			.addFilterBefore(filter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
