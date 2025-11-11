package com.empresa.soporte_tecnico.controller;

import com.empresa.soporte_tecnico.dto.SolicitudDto;
import com.empresa.soporte_tecnico.mapper.SolicitudMapper;
import com.empresa.soporte_tecnico.model.Solicitud;
import com.empresa.soporte_tecnico.service.SolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/solicitudes")
@Tag(name = "Solicitudes", description = "Gestión de solicitudes de soporte técnico")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    // 🟢 Crear una nueva solicitud

    @Operation(

            summary = "Crear una nueva solicitud",
            description = "Crea una nueva solicitud de soporte técnico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Solicitud creada correctamente",
                            content = @Content(schema = @Schema(implementation = SolicitudDto.class)))
            }
    )
    @PostMapping
    public ResponseEntity<SolicitudDto> crearSolicitud(@Valid @RequestBody SolicitudDto solicitudDto) {
        Solicitud solicitud = SolicitudMapper.toEntity(solicitudDto);
        Solicitud creada = solicitudService.crearSolicitud(solicitud);
        return ResponseEntity.ok(SolicitudMapper.toDto(creada));
    }

    // 🟡 Obtener todas las solicitudes
    @Operation(
            summary = "Obtener todas las solicitudes",
            description = "Devuelve una lista de todas las solicitudes de soporte técnico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
                            content = @Content(schema = @Schema(implementation = SolicitudDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<SolicitudDto>> obtenerSolicitudes() {
        List<SolicitudDto> lista = solicitudService.obtenerSolicitudes()
                .stream()
                .map(SolicitudMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    // 🟠 Obtener una solicitud por ID
    @Operation(
            summary = "Obtener una solicitud por ID",
            description = "Devuelve una solicitud específica según su identificador",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Solicitud encontrada",
                            content = @Content(schema = @Schema(implementation = SolicitudDto.class))),
                    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDto> obtenerSolicitudPorId(@PathVariable Long id) {
        Solicitud solicitud = solicitudService.obtenerPorId(id);
        return solicitud != null
                ? ResponseEntity.ok(SolicitudMapper.toDto(solicitud))
                : ResponseEntity.notFound().build();
    }

    // 🟤 Actualizar una solicitud existente
    @Operation(
            summary = "Actualizar una solicitud",
            description = "Actualiza los datos de una solicitud existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Solicitud actualizada correctamente",
                            content = @Content(schema = @Schema(implementation = SolicitudDto.class))),
                    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<SolicitudDto> actualizarSolicitud(@PathVariable Long id, @RequestBody SolicitudDto solicitudDto) {
        Solicitud solicitud = SolicitudMapper.toEntity(solicitudDto);
        Solicitud actualizada = solicitudService.actualizarSolicitud(id, solicitud);
        return actualizada != null
                ? ResponseEntity.ok(SolicitudMapper.toDto(actualizada))
                : ResponseEntity.notFound().build();
    }

    // 🔴 Eliminar una solicitud por ID
    @Operation(
            summary = "Eliminar solicitud por ID",
            description = "Elimina una solicitud existente según su ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Eliminada correctamente"),
                    @ApiResponse(responseCode = "404", description = "No encontrada")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable Long id) {
        solicitudService.eliminarSolicitud(id);
        return ResponseEntity.noContent().build();
    }
}
