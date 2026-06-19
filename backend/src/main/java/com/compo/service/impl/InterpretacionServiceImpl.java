package com.compo.service.impl;

import com.compo.dto.*;
import com.compo.entity.*;
import com.compo.exception.ResourceNotFoundException;
import com.compo.repository.*;
import com.compo.service.InterpretacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterpretacionServiceImpl implements InterpretacionService {

    private final InterpretacionRepository interpretacionRepository;
    private final ObraRepository obraRepository;
    private final InterpreteRepository interpreteRepository;
    private final DirectorRepository directorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<InterpretacionResponse> listarActivos() {
        return interpretacionRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterpretacionResponse> listarTodos() {
        return interpretacionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InterpretacionResponse obtenerPorId(Long id) {
        Interpretacion interpretacion = interpretacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interpretación", id));
        return toResponse(interpretacion);
    }

    @Override
    @Transactional
    public InterpretacionResponse crear(InterpretacionRequest request) {
        Obra obra = obraRepository.findByIdWithCompositores(request.getObraId())
                .orElseThrow(() -> new ResourceNotFoundException("Obra", request.getObraId()));

        Interprete interprete = interpreteRepository.findById(request.getInterpreteId())
                .orElseThrow(() -> new ResourceNotFoundException("Intérprete", request.getInterpreteId()));

        Director director = directorRepository.findById(request.getDirectorId())
                .orElseThrow(() -> new ResourceNotFoundException("Director", request.getDirectorId()));

        Interpretacion interpretacion = Interpretacion.builder()
                .obra(obra)
                .interprete(interprete)
                .director(director)
                .fechaInterpretacion(request.getFechaInterpretacion())
                .lugar(request.getLugar())
                .observaciones(request.getObservaciones())
                .activo(true)
                .build();

        interpretacion = interpretacionRepository.save(interpretacion);
        return toResponse(interpretacion);
    }

    @Override
    @Transactional
    public InterpretacionResponse actualizar(Long id, InterpretacionRequest request) {
        Interpretacion interpretacion = interpretacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interpretación", id));

        Obra obra = obraRepository.findByIdWithCompositores(request.getObraId())
                .orElseThrow(() -> new ResourceNotFoundException("Obra", request.getObraId()));

        Interprete interprete = interpreteRepository.findById(request.getInterpreteId())
                .orElseThrow(() -> new ResourceNotFoundException("Intérprete", request.getInterpreteId()));

        Director director = directorRepository.findById(request.getDirectorId())
                .orElseThrow(() -> new ResourceNotFoundException("Director", request.getDirectorId()));

        interpretacion.setObra(obra);
        interpretacion.setInterprete(interprete);
        interpretacion.setDirector(director);
        interpretacion.setFechaInterpretacion(request.getFechaInterpretacion());
        interpretacion.setLugar(request.getLugar());
        interpretacion.setObservaciones(request.getObservaciones());

        interpretacion = interpretacionRepository.save(interpretacion);
        return toResponse(interpretacion);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!interpretacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Interpretación", id);
        }
        interpretacionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterpretacionResponse> listarPorInterprete(Long interpreteId) {
        if (!interpreteRepository.existsById(interpreteId)) {
            throw new ResourceNotFoundException("Intérprete", interpreteId);
        }
        return interpretacionRepository.findByInterpreteIdAndActivoTrue(interpreteId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterpretacionResponse> listarPorDirector(Long directorId) {
        if (!directorRepository.existsById(directorId)) {
            throw new ResourceNotFoundException("Director", directorId);
        }
        return interpretacionRepository.findByDirectorIdAndActivoTrue(directorId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ObraResponse toObraResponse(Obra obra) {
        List<CompositorResponse> compositores = obra.getCompositores()
                .stream()
                .map(c -> CompositorResponse.builder()
                        .id(c.getId())
                        .nombre(c.getNombre())
                        .apellido(c.getApellido())
                        .nacionalidad(c.getNacionalidad())
                        .fechaNacimiento(c.getFechaNacimiento())
                        .activo(c.getActivo())
                        .build())
                .collect(Collectors.toList());

        return ObraResponse.builder()
                .id(obra.getId())
                .titulo(obra.getTitulo())
                .descripcion(obra.getDescripcion())
                .genero(obra.getGenero())
                .fechaCreacion(obra.getFechaCreacion())
                .activo(obra.getActivo())
                .compositores(compositores)
                .build();
    }

    private InterpreteResponse toInterpreteResponse(Interprete interprete) {
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

    private DirectorResponse toDirectorResponse(Director director) {
        return DirectorResponse.builder()
                .id(director.getId())
                .nombre(director.getNombre())
                .apellido(director.getApellido())
                .nacionalidad(director.getNacionalidad())
                .fechaNacimiento(director.getFechaNacimiento())
                .activo(director.getActivo())
                .build();
    }

    private InterpretacionResponse toResponse(Interpretacion interpretacion) {
        return InterpretacionResponse.builder()
                .id(interpretacion.getId())
                .obra(toObraResponse(interpretacion.getObra()))
                .interprete(toInterpreteResponse(interpretacion.getInterprete()))
                .director(toDirectorResponse(interpretacion.getDirector()))
                .fechaInterpretacion(interpretacion.getFechaInterpretacion())
                .lugar(interpretacion.getLugar())
                .observaciones(interpretacion.getObservaciones())
                .activo(interpretacion.getActivo())
                .build();
    }
}
