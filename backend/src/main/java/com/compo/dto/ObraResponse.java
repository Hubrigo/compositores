package com.compo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObraResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private String genero;
    private LocalDate fechaCreacion;
    private Boolean activo;
    private List<CompositorResponse> compositores;
}
