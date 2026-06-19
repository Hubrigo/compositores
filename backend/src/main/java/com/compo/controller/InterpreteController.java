package com.compo.controller;

import com.compo.dto.InterpreteRequest;
import com.compo.dto.InterpreteResponse;
import com.compo.service.InterpreteService;
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
@RequestMapping("/api/interpretes")
@RequiredArgsConstructor
@Tag(name = "Intérpretes", description = "CRUD de intérpretes")
@SecurityRequirement(name = "bearerAuth")
public class InterpreteController {

    private final InterpreteService interpreteService;

    @GetMapping
    @Operation(summary = "Listar todos los intérpretes activos")
    public ResponseEntity<List<InterpreteResponse>> listarActivos() {
        return ResponseEntity.ok(interpreteService.listarActivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener intérprete por ID")
    public ResponseEntity<InterpreteResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(interpreteService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo intérprete")
    public ResponseEntity<InterpreteResponse> crear(@Valid @RequestBody InterpreteRequest request) {
        InterpreteResponse response = interpreteService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar intérprete")
    public ResponseEntity<InterpreteResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InterpreteRequest request) {
        return ResponseEntity.ok(interpreteService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar intérprete (baja lógica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        interpreteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
