package med.voll.api.usuario;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;
	
	public String gerarToken(Usuario usuario) { // método que gera o token jwt
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret); // cria a assinatura usando a senha secreta que foi passada
		    return JWT.create() // Inicia a criação do token
		        .withIssuer("CursoAPI_Agendamento") // Define quem criou o token
		        .withSubject(usuario.getLogin()) // Define o usuário autenticado
		        .withExpiresAt(dataExpiracao()) // Define a validade do token
		        .sign(algorithm); // Gera o token assinado com a senha secreta
		} catch (JWTCreationException exception){
			throw new RuntimeException("Erro ao gerro token JWT", exception);
		}
	}

	private Instant dataExpiracao() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")); // criando expiração de 2h após o horário atual
	}
}
