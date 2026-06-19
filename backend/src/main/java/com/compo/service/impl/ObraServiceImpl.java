package com.compo.service.impl;

import com.compo.dto.CompositorResponse;
import com.compo.dto.ObraRequest;
import com.compo.dto.ObraResponse;
import com.compo.entity.Compositor;
import com.compo.entity.Obra;
import com.compo.exception.ResourceNotFoundException;
import com.compo.repository.CompositorRepository;
import com.compo.repository.ObraRepository;
import com.compo.service.ObraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObraServiceImpl implements ObraService {

    private final ObraRepository obraRepository;
    private final CompositorRepository compositorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ObraResponse> listarActivos() {
        return obraRepository.findByActivoTrueWithCompositores()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObraResponse> listarTodos() {
        return obraRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ObraResponse obtenerPorId(Long id) {
        Obra obra = obraRepository.findByIdWithCompositores(id)
                .orElseThrow(() -> new ResourceNotFoundException("Obra", id));
        return toResponse(obra);
    }

    @Override
    @Transactional
    public ObraResponse crear(ObraRequest request) {
        Set<Compositor> compositores = resolverCompositores(request.getCompositorIds());

        Obra obra = Obra.builder()
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .genero(request.getGenero())
                .fechaCreacion(request.getFechaCreacion())
                .activo(true)
                .compositores(compositores)
                .build();

        obra = obraRepository.save(obra);
        return toResponse(obra);
    }

    @Override
    @Transactional
    public ObraResponse actualizar(Long id, ObraRequest request) {
        Obra obra = obraRepository.findByIdWithCompositores(id)
                .orElseThrow(() -> new ResourceNotFoundException("Obra", id));

        Set<Compositor> compositores = resolverCompositores(request.getCompositorIds());

        obra.setTitulo(request.getTitulo());
        obra.setDescripcion(request.getDescripcion());
        obra.setGenero(request.getGenero());
        obra.setFechaCreacion(request.getFechaCreacion());
        obra.setCompositores(compositores);

        obra = obraRepository.save(obra);
        return toResponse(obra);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!obraRepository.existsById(id)) {
            throw new ResourceNotFoundException("Obra", id);
        }
        obraRepository.deleteById(id);
    }

    private Set<Compositor> resolverCompositores(List<Long> compositorIds) {
        if (compositorIds == null || compositorIds.isEmpty()) {
            return new HashSet<>();
        }
        return compositorIds.stream()
                .map(cId -> compositorRepository.findById(cId)
                        .orElseThrow(() -> new ResourceNotFoundException("Compositor", cId)))
                .collect(Collectors.toSet());
    }

    private CompositorResponse toCompositorResponse(Compositor compositor) {
        return CompositorResponse.builder()
                .id(compositor.getId())
                .nombre(compositor.getNombre())
                .apellido(compositor.getApellido())
                .nacionalidad(compositor.getNacionalidad())
                .fechaNacimiento(compositor.getFechaNacimiento())
                .activo(compositor.getActivo())
                .build();
    }

    private ObraResponse toResponse(Obra obra) {
        List<CompositorResponse> compositoresResponse = obra.getCompositores()
                .stream()
                .map(this::toCompositorResponse)
                .collect(Collectors.toList());

        return ObraResponse.builder()
                .id(obra.getId())
                .titulo(obra.getTitulo())
                .descripcion(obra.getDescripcion())
                .genero(obra.getGenero())
                .fechaCreacion(obra.getFechaCreacion())
                .activo(obra.getActivo())
                .compositores(compositoresResponse)
                .build();
    }
}
