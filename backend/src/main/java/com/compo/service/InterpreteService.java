package com.compo.service;

import com.compo.dto.InterpreteRequest;
import com.compo.dto.InterpreteResponse;

import java.util.List;

public interface InterpreteService {

    List<InterpreteResponse> listarActivos();

    List<InterpreteResponse> listarTodos();

    InterpreteResponse obtenerPorId(Long id);

    InterpreteResponse crear(InterpreteRequest request);

    InterpreteResponse actualizar(Long id, InterpreteRequest request);

    void eliminar(Long id);
}
