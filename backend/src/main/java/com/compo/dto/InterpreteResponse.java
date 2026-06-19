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
public class InterpreteResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String tipoVozOInstrumento;
    private String nacionalidad;
    private LocalDate fechaNacimiento;
    private Boolean activo;
}
