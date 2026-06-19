package com.compo.security;

import com.compo.entity.Usuario;
import com.compo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String cedula) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con cédula: " + cedula));

        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + cedula);
        }

        return User.builder()
                .username(usuario.getCedula())
                .password(usuario.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol())))
                .build();
    }
}
