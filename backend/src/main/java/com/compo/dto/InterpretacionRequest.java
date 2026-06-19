package com.compo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterpretacionRequest {

    @NotNull(message = "El ID de la obra es obligatorio")
    private Long obraId;

    @NotNull(message = "El ID del intérprete es obligatorio")
    private Long interpreteId;

    @NotNull(message = "El ID del director es obligatorio")
    private Long directorId;

    @NotNull(message = "La fecha de interpretación es obligatoria")
    private LocalDate fechaInterpretacion;

    @NotBlank(message = "El lugar es obligatorio")
    private String lugar;

    private String observaciones;
}
