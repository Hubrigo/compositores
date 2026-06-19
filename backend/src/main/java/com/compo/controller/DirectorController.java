package com.compo.controller;

import com.compo.dto.DirectorRequest;
import com.compo.dto.DirectorResponse;
import com.compo.service.DirectorService;
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
@RequestMapping("/api/directores")
@RequiredArgsConstructor
@Tag(name = "Directores", description = "CRUD de directores")
@SecurityRequirement(name = "bearerAuth")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    @Operation(summary = "Listar todos los directores activos")
    public ResponseEntity<List<DirectorResponse>> listarActivos() {
        return ResponseEntity.ok(directorService.listarActivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener director por ID")
    public ResponseEntity<DirectorResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(directorService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo director")
    public ResponseEntity<DirectorResponse> crear(@Valid @RequestBody DirectorRequest request) {
        DirectorResponse response = directorService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar director")
    public ResponseEntity<DirectorResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DirectorRequest request) {
        return ResponseEntity.ok(directorService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar director (baja lógica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        directorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
