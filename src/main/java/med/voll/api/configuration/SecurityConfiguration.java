package med.voll.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // permite personalizar o spring security
public class SecurityConfiguration { // é o arquivo de configuração para ser um projeto stateless(forma de autenticação)
	
	// o HttpSecurity cria as configuraçãoes das requisições http
	// e o SecurityFilterChain é um conjunto de filtros de segurança. Ele usa o HttpSecurity para fazer a segurança quando a aplicação receber uma requisição 
	@Bean
	public SecurityFilterChain configureStatelessSecurity(HttpSecurity http) throws Exception {
		return http.csrf().disable()  // desabilita a proteção CSRF, pq stateless não usa cookies na autenticação
			    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // configura para ser stateless
			    .and()  // permite continuar o fluxo de configuração
			    .build();  // finaliza a configuração e cria o objeto SecurityFilterChain com todas as regras definidas até o momento.
	}
}
