package med.voll.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.experimental.var;
import med.voll.api.usuario.DadosAutenticacao;
import med.voll.api.usuario.DadosTokenDTO;
import med.voll.api.usuario.TokenService;
import med.voll.api.usuario.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RestController
@RequestMapping("/login")
public class AutenticacaoController {
	
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
		
		//BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //String hash = encoder.encode("123456");
        //System.out.println(hash);
		
		var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
		Authentication authentication = manager.authenticate(authenticationToken); // é do tipo Authentication
		
		var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
		
		return ResponseEntity.ok(new DadosTokenDTO(tokenJWT)); // caso seja autenticado, irá retornar status 200 e no body terá o token
	}
	
}
