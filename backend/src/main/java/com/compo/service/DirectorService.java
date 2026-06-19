package com.compo.service;

import com.compo.dto.DirectorRequest;
import com.compo.dto.DirectorResponse;

import java.util.List;

public interface DirectorService {

    List<DirectorResponse> listarActivos();

    List<DirectorResponse> listarTodos();

    DirectorResponse obtenerPorId(Long id);

    DirectorResponse crear(DirectorRequest request);

    DirectorResponse actualizar(Long id, DirectorRequest request);

    void eliminar(Long id);
}
