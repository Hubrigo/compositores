package com.compo.repository;

import com.compo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCedula(String cedula);

    boolean existsByCedula(String cedula);

    boolean existsByEmail(String email);
}
