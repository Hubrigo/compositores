package com.compo.service.impl;

import com.compo.dto.CompositorRequest;
import com.compo.dto.CompositorResponse;
import com.compo.entity.Compositor;
import com.compo.exception.ResourceNotFoundException;
import com.compo.repository.CompositorRepository;
import com.compo.service.CompositorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompositorServiceImpl implements CompositorService {

    private final CompositorRepository compositorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CompositorResponse> listarActivos() {
        return compositorRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompositorResponse> listarTodos() {
        return compositorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompositorResponse obtenerPorId(Long id) {
        Compositor compositor = compositorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compositor", id));
        return toResponse(compositor);
    }

    @Override
    @Transactional
    public CompositorResponse crear(CompositorRequest request) {
        Compositor compositor = Compositor.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .nacionalidad(request.getNacionalidad())
                .fechaNacimiento(request.getFechaNacimiento())
                .activo(true)
                .build();
        compositor = compositorRepository.save(compositor);
        return toResponse(compositor);
    }

    @Override
    @Transactional
    public CompositorResponse actualizar(Long id, CompositorRequest request) {
        Compositor compositor = compositorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compositor", id));

        compositor.setNombre(request.getNombre());
        compositor.setApellido(request.getApellido());
        compositor.setNacionalidad(request.getNacionalidad());
        compositor.setFechaNacimiento(request.getFechaNacimiento());

        compositor = compositorRepository.save(compositor);
        return toResponse(compositor);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Compositor compositor = compositorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compositor", id));
        compositor.setActivo(false);
        compositorRepository.save(compositor);
    }

    private CompositorResponse toResponse(Compositor compositor) {
        return CompositorResponse.builder()
                .id(compositor.getId())
                .nombre(compositor.getNombre())
                .apellido(compositor.getApellido())
                .nacionalidad(compositor.getNacionalidad())
                .fechaNacimiento(compositor.getFechaNacimiento())
                .activo(compositor.getActivo())
                .build();
    }
}
