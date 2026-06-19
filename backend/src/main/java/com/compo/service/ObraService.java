package com.compo.service;

import com.compo.dto.ObraRequest;
import com.compo.dto.ObraResponse;

import java.util.List;

public interface ObraService {

    List<ObraResponse> listarActivos();

    List<ObraResponse> listarTodos();

    ObraResponse obtenerPorId(Long id);

    ObraResponse crear(ObraRequest request);

    ObraResponse actualizar(Long id, ObraRequest request);

    void eliminar(Long id);
}
