package com.compo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterpretacionResponse {

    private Long id;
    private ObraResponse obra;
    private InterpreteResponse interprete;
    private DirectorResponse director;
    private LocalDate fechaInterpretacion;
    private String lugar;
    private String observaciones;
    private Boolean activo;
}
