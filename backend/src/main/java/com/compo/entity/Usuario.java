package com.compo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false, length = 20)
    private String cedula;

    @NotBlank
    @Size(min = 6)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nombre;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 10)
    @Builder.Default
    private String rol = "USER";

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}
