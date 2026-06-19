package com.compo.controller;

import com.compo.dto.ObraRequest;
import com.compo.dto.ObraResponse;
import com.compo.service.ObraService;
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
@RequestMapping("/api/obras")
@RequiredArgsConstructor
@Tag(name = "Obras", description = "CRUD de obras musicales")
@SecurityRequirement(name = "bearerAuth")
public class ObraController {

    private final ObraService obraService;

    @GetMapping
    @Operation(summary = "Listar todas las obras activas con sus compositores")
    public ResponseEntity<List<ObraResponse>> listarActivos() {
        return ResponseEntity.ok(obraService.listarActivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener obra por ID")
    public ResponseEntity<ObraResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(obraService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear nueva obra")
    public ResponseEntity<ObraResponse> crear(@Valid @RequestBody ObraRequest request) {
        ObraResponse response = obraService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar obra")
    public ResponseEntity<ObraResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ObraRequest request) {
        return ResponseEntity.ok(obraService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar obra (eliminación física)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        obraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
