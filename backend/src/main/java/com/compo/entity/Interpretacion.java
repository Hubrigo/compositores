package com.compo.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "interpretaciones")
public class Interpretacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "obra_id", nullable = false)
    private Obra obra;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interprete_id", nullable = false)
    private Interprete interprete;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id", nullable = false)
    private Director director;

    @NotNull
    @Column(name = "fecha_interpretacion", nullable = false)
    private LocalDate fechaInterpretacion;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String lugar;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}
