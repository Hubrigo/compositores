package com.compo.service;

import com.compo.dto.CompositorRequest;
import com.compo.dto.CompositorResponse;

import java.util.List;

public interface CompositorService {

    List<CompositorResponse> listarActivos();

    List<CompositorResponse> listarTodos();

    CompositorResponse obtenerPorId(Long id);

    CompositorResponse crear(CompositorRequest request);

    CompositorResponse actualizar(Long id, CompositorRequest request);

    void eliminar(Long id);
}
