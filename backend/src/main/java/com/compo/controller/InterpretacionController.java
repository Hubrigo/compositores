package com.compo.controller;

import com.compo.dto.InterpretacionRequest;
import com.compo.dto.InterpretacionResponse;
import com.compo.service.InterpretacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interpretaciones")
@RequiredArgsConstructor
@Tag(name = "Interpretaciones", description = "CRUD de interpretaciones musicales")
@SecurityRequirement(name = "bearerAuth")
public class InterpretacionController {

    private final InterpretacionService interpretacionService;

    @GetMapping
    @Operation(summary = "Listar todas las interpretaciones activas")
    public ResponseEntity<List<InterpretacionResponse>> listarActivos() {
        return ResponseEntity.ok(interpretacionService.listarActivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener interpretación por ID")
    public ResponseEntity<InterpretacionResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(interpretacionService.obtenerPorId(id));
    }

    @GetMapping("/interprete/{id}")
    @Operation(summary = "Listar interpretaciones por intérprete")
    public ResponseEntity<List<InterpretacionResponse>> listarPorInterprete(@PathVariable Long id) {
        return ResponseEntity.ok(interpretacionService.listarPorInterprete(id));
    }

    @GetMapping("/director/{id}")
    @Operation(summary = "Listar interpretaciones por director")
    public ResponseEntity<List<InterpretacionResponse>> listarPorDirector(@PathVariable Long id) {
        return ResponseEntity.ok(interpretacionService.listarPorDirector(id));
    }

    @PostMapping
    @Operation(summary = "Crear nueva interpretación")
    public ResponseEntity<InterpretacionResponse> crear(@Valid @RequestBody InterpretacionRequest request) {
        InterpretacionResponse response = interpretacionService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar interpretación")
    public ResponseEntity<InterpretacionResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InterpretacionRequest request) {
        return ResponseEntity.ok(interpretacionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar interpretación (eliminación física)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        interpretacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
