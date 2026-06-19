package com.compo.service.impl;

import com.compo.dto.InterpreteRequest;
import com.compo.dto.InterpreteResponse;
import com.compo.entity.Interprete;
import com.compo.exception.ResourceNotFoundException;
import com.compo.repository.InterpreteRepository;
import com.compo.service.InterpreteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterpreteServiceImpl implements InterpreteService {

    private final InterpreteRepository interpreteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<InterpreteResponse> listarActivos() {
        return interpreteRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterpreteResponse> listarTodos() {
        return interpreteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InterpreteResponse obtenerPorId(Long id) {
        Interprete interprete = interpreteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intérprete", id));
        return toResponse(interprete);
    }

    @Override
    @Transactional
    public InterpreteResponse crear(InterpreteRequest request) {
        Interprete interprete = Interprete.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .tipoVozOInstrumento(request.getTipoVozOInstrumento())
                .nacionalidad(request.getNacionalidad())
                .fechaNacimiento(request.getFechaNacimiento())
                .activo(true)
                .build();
        interprete = interpreteRepository.save(interprete);
        return toResponse(interprete);
    }

    @Override
    @Transactional
    public InterpreteResponse actualizar(Long id, InterpreteRequest request) {
        Interprete interprete = interpreteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intérprete", id));

        interprete.setNombre(request.getNombre());
        interprete.setApellido(request.getApellido());
        interprete.setTipoVozOInstrumento(request.getTipoVozOInstrumento());
        interprete.setNacionalidad(request.getNacionalidad());
        interprete.setFechaNacimiento(request.getFechaNacimiento());

        interprete = interpreteRepository.save(interprete);
        return toResponse(interprete);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Interprete interprete = interpreteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intérprete", id));
        interprete.setActivo(false);
        interpreteRepository.save(interprete);
    }

    private InterpreteResponse toResponse(Interprete interprete) {
        return InterpreteResponse.builder()
                .id(interprete.getId())
                .nombre(interprete.getNombre())
                .apellido(interprete.getApellido())
                .tipoVozOInstrumento(interprete.getTipoVozOInstrumento())
                .nacionalidad(interprete.getNacionalidad())
                .fechaNacimiento(interprete.getFechaNacimiento())
                .activo(interprete.getActivo())
                .build();
    }
}
