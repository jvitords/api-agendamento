package med.voll.api.configuration;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.var;
import med.voll.api.usuario.TokenService;
import med.voll.api.usuario.UsuarioRepository;

@Component
public class SecurityFilter extends OncePerRequestFilter{ // essa class que estou extendendo, garante que esse filtro será chamado uma única vez

	@Autowired
	private TokenService tokenService;
	@Autowired 
	private UsuarioRepository usuarioRepository;
	
	// método que será executado apenas uma vez em todas as requisições
	// request: requisição feita pelo cliente, response: resposta que será enviada, filterChain: cadeia de filtros que será processada até chegar no controller
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		var tokenJWT = recuperarToken(request);
		
		if(tokenJWT != null) {                                                                                                    
			var subject = tokenService.verificarToken(tokenJWT); // verifica se o token é valido, faz a autenticação e entrega o nome do usuário			
			var usuario = usuarioRepository.findByLogin(subject); // cria um objeto do usuário com as informações dele
			
			var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()); // agr cria um objeto autenticado
			SecurityContextHolder.getContext().setAuthentication(authentication); // aqui passa as informações para o spring security
		}
		
		filterChain.doFilter(request, response); // avança para o próximo filtro ou controller
	}
	
	private String recuperarToken(HttpServletRequest request) { // retorna o token que for enviado na requisição
		String authorizationHeader = request.getHeader("Authorization");
		if(authorizationHeader != null) {
			return authorizationHeader.replace("Bearer ", "");
		}
		return null;
	}
}
