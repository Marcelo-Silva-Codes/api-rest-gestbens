package com.GestBens.api.config;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

        public record ErroResponse(int status, String erro, String mensagem, LocalDateTime timestamp) {}

        // Não encontrado -> 404
        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErroResponse> naoEncontrado(EntityNotFoundException e) {
            return montar(HttpStatus.NOT_FOUND, e.getMessage());
        }

        // Regra de negócio violada (ex.: bem em manutenção) -> 409
        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<ErroResponse> regraViolada(IllegalStateException e) {
            return montar(HttpStatus.CONFLICT, e.getMessage());
        }

        // Dado inválido -> 400
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErroResponse> dadoInvalido(IllegalArgumentException e) {
            return montar(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        // Ex.: apagar categoria que ainda tem bens, ou e-mail duplicado -> 409
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErroResponse> integridade(DataIntegrityViolationException e) {
            return montar(HttpStatus.CONFLICT, "Operação viola uma regra de integridade dos dados (registro em uso ou duplicado)");
        }

        // JSON malformado ou valor de enum inválido (ex.: "status": "QUALQUER") -> 400
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErroResponse> jsonInvalido(HttpMessageNotReadableException e) {
            return montar(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou com valor não permitido");
        }

        private ResponseEntity<ErroResponse> montar(HttpStatus status, String mensagem) {
            return ResponseEntity.status(status)
                    .body(new ErroResponse(status.value(), status.getReasonPhrase(), mensagem, LocalDateTime.now()));
        }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResponse> credenciaisInvalidas(BadCredentialsException e) {
        return montar(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos");
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErroResponse> usuarioDesativado(DisabledException e) {
        return montar(HttpStatus.FORBIDDEN, "Usuário desativado");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> erroInesperado(Exception e) {
        return montar(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno no servidor");
    }
    }