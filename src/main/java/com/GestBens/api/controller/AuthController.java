package com.GestBens.api.controller;

import com.GestBens.api.Security.*;
import com.GestBens.api.dto.GoogleLoginRequest;
import com.GestBens.api.dto.LoginRequest;
import com.GestBens.api.dto.LoginResponse;
import com.GestBens.api.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final GoogleTokenService googleTokenService;
    private final GoogleAuthService googleAuthService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,  GoogleTokenService googleTokenService, GoogleAuthService googleAuthService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.googleTokenService = googleTokenService;
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest dto) {
        // Se e-mail/senha estiverem errados, o Spring lança BadCredentialsException.
        // Se o usuário estiver inativo, lança DisabledException.
        // As duas são tratadas no GlobalExceptionHandler.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()));

        UsuarioAutenticado logado = (UsuarioAutenticado) authentication.getPrincipal();
        String token = jwtService.gerarToken(logado);

        Usuario u = logado.getUsuario();
        return new LoginResponse(token, u.getNome(), u.isAdmin() ? "ADMIN" : "USER");
    }

    @PostMapping("/login/google")
    public LoginResponse loginGoogle( @Valid @RequestBody GoogleLoginRequest dto) {
        DadosGoogle google = googleTokenService.validar(dto.credential());
        Usuario usuario = googleAuthService.entrarOuCadastrar(google);

        String token = jwtService.gerarToken(new UsuarioAutenticado(usuario));
        return new LoginResponse(token, usuario.getNome(), usuario.isAdmin() ? "ADMIN" : "USER");
    }
}
