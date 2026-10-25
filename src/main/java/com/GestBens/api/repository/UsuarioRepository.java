package com.GestBens.api.repository;

import com.GestBens.api.model.Provider;
import com.GestBens.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long>{
     Optional<Usuario> findByEmail(String email);
     boolean existsByEmail(String email);
     Optional<Usuario> findByProviderAndProviderId(Provider provider, String providerId);
     @Query("select count(u) from Usuario u where u.isAdmin = true and u.ativo = true")
     long contarAdminsAtivos();
}
