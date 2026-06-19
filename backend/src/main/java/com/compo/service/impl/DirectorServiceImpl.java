package com.compo.service.impl;

import com.compo.dto.DirectorRequest;
import com.compo.dto.DirectorResponse;
import com.compo.entity.Director;
import com.compo.exception.ResourceNotFoundException;
import com.compo.repository.DirectorRepository;
import com.compo.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DirectorResponse> listarActivos() {
        return directorRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DirectorResponse> listarTodos() {
        return directorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DirectorResponse obtenerPorId(Long id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director", id));
        return toResponse(director);
    }

    @Override
    @Transactional
    public DirectorResponse crear(DirectorRequest request) {
        Director director = Director.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .nacionalidad(request.getNacionalidad())
                .fechaNacimiento(request.getFechaNacimiento())
                .activo(true)
                .build();
        director = directorRepository.save(director);
        return toResponse(director);
    }

    @Override
    @Transactional
    public DirectorResponse actualizar(Long id, DirectorRequest request) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director", id));

        director.setNombre(request.getNombre());
        director.setApellido(request.getApellido());
        director.setNacionalidad(request.getNacionalidad());
        director.setFechaNacimiento(request.getFechaNacimiento());

        director = directorRepository.save(director);
        return toResponse(director);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director", id));
        director.setActivo(false);
        directorRepository.save(director);
    }

    private DirectorResponse toResponse(Director director) {
        return DirectorResponse.builder()
                .id(director.getId())
                .nombre(director.getNombre())
                .apellido(director.getApellido())
                .nacionalidad(director.getNacionalidad())
                .fechaNacimiento(director.getFechaNacimiento())
                .activo(director.getActivo())
                .build();
    }
}
