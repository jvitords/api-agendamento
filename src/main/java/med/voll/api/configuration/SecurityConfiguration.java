package med.voll.api.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // permite personalizar o spring security
public class SecurityConfiguration { // é o arquivo de configuração para ser um projeto stateless(forma de autenticação)
	
	@Autowired
	private SecurityFilter securityFilter;
	
	// o HttpSecurity cria as configuraçãoes das requisições http
	// e o SecurityFilterChain é um conjunto de filtros de segurança. Ele usa o HttpSecurity para fazer a segurança quando a aplicação receber uma requisição 
	@Bean
	public SecurityFilterChain configureStatelessSecurity(HttpSecurity http) throws Exception {
		return http.csrf().disable()  // desabilita a proteção CSRF, pq stateless não usa cookies na autenticação
			    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // configura para ser stateless
			    .and().authorizeHttpRequests().antMatchers(HttpMethod.POST, "/login").permitAll() // irá autorizar tudo que vier da url /login e que seja POST
			    .anyRequest().authenticated() // todas as outras requisições precisam ser autenticadas
			    .and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
			    .build();  // finaliza a configuração e cria o objeto SecurityFilterChain com todas as regras definidas até o momento.
		
	}
	
	// criamos esse bean para conseguir usar ele no controller e fazer a verificação de login e senha
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	// bean para dizer que nossas senhas no BD são Bcript(forma de guardar a senha sem mostrar ela diretamente) 
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}
