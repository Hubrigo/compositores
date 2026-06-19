package com.compo.service;

import com.compo.dto.InterpretacionRequest;
import com.compo.dto.InterpretacionResponse;

import java.util.List;

public interface InterpretacionService {

    List<InterpretacionResponse> listarActivos();

    List<InterpretacionResponse> listarTodos();

    InterpretacionResponse obtenerPorId(Long id);

    InterpretacionResponse crear(InterpretacionRequest request);

    InterpretacionResponse actualizar(Long id, InterpretacionRequest request);

    void eliminar(Long id);

    List<InterpretacionResponse> listarPorInterprete(Long interpreteId);

    List<InterpretacionResponse> listarPorDirector(Long directorId);
}
