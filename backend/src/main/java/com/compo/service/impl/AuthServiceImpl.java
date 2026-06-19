package com.compo.service.impl;

import com.compo.dto.AuthRequest;
import com.compo.dto.AuthResponse;
import com.compo.entity.Usuario;
import com.compo.exception.BadRequestException;
import com.compo.exception.ResourceNotFoundException;
import com.compo.repository.UsuarioRepository;
import com.compo.security.JwtUtil;
import com.compo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    @Transactional
    public AuthResponse register(AuthRequest request) {
        if (usuarioRepository.existsByCedula(request.getCedula())) {
            throw new BadRequestException("Ya existe un usuario con la cédula: " + request.getCedula());
        }

        Usuario usuario = Usuario.builder()
                .cedula(request.getCedula())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombre("Usuario " + request.getCedula())
                .email(request.getCedula() + "@compo.com")
                .rol("USER")
                .activo(true)
                .build();

        usuario = usuarioRepository.save(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getCedula());
        String token = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .cedula(usuario.getCedula())
                .build();
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getCedula(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Cédula o contraseña incorrectos");
        }

        Usuario usuario = usuarioRepository.findByCedula(request.getCedula())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "cédula", request.getCedula()));

        if (!usuario.getActivo()) {
            throw new BadRequestException("El usuario se encuentra inactivo");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getCedula());
        String token = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .cedula(usuario.getCedula())
                .build();
    }
}
