package com.compo.controller;

import com.compo.dto.CompositorRequest;
import com.compo.dto.CompositorResponse;
import com.compo.service.CompositorService;
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
@RequestMapping("/api/compositores")
@RequiredArgsConstructor
@Tag(name = "Compositores", description = "CRUD de compositores")
@SecurityRequirement(name = "bearerAuth")
public class CompositorController {

    private final CompositorService compositorService;

    @GetMapping
    @Operation(summary = "Listar todos los compositores activos")
    public ResponseEntity<List<CompositorResponse>> listarActivos() {
        return ResponseEntity.ok(compositorService.listarActivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener compositor por ID")
    public ResponseEntity<CompositorResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(compositorService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo compositor")
    public ResponseEntity<CompositorResponse> crear(@Valid @RequestBody CompositorRequest request) {
        CompositorResponse response = compositorService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar compositor")
    public ResponseEntity<CompositorResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CompositorRequest request) {
        return ResponseEntity.ok(compositorService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar compositor (baja lógica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        compositorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
