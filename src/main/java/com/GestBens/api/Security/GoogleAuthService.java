package com.GestBens.api.Security;

import com.GestBens.api.model.Provider;
import com.GestBens.api.model.Usuario;
import com.GestBens.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GoogleAuthService {

    private final UsuarioRepository usuarioRepository;
    private final String allowedDomain;

    public GoogleAuthService(UsuarioRepository usuarioRepository,
                             @Value("${app.auth.google.allowed-domain:}") String allowedDomain) {
        this.usuarioRepository = usuarioRepository;
        this.allowedDomain = allowedDomain;
    }

    @Transactional
    public Usuario entrarOuCadastrar(DadosGoogle google) {

        // Restrição por domínio (desligada quando vazia)
        if (!allowedDomain.isBlank()
                && !google.email().toLowerCase().endsWith("@" + allowedDomain.toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Domínio de e-mail não autorizado");
        }

        Usuario usuario = usuarioRepository
                .findByProviderAndProviderId(Provider.GOOGLE, google.sub())
                .orElseGet(() -> vincularOuCriar(google));

        if (!usuario.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário desativado");
        }
        return usuario;
    }

    private Usuario vincularOuCriar(DadosGoogle google) {
        // Já existe conta com esse e-mail? Vincula o Google a ela.
        return usuarioRepository.findByEmail(google.email())
                .map(existente -> {
                    existente.setProvider(Provider.GOOGLE);
                    existente.setProviderId(google.sub());
                    return existente;
                })
                // Senão, cria um usuário comum
                .orElseGet(() -> {
                    Usuario novo = new Usuario();
                    novo.setNome(google.nome() != null ? google.nome() : google.email());
                    novo.setEmail(google.email());
                    novo.setProvider(Provider.GOOGLE);
                    novo.setProviderId(google.sub());
                    // sem senha, admin = false e ativo = true (padrões da entidade)
                    return usuarioRepository.save(novo);
                });
    }
}