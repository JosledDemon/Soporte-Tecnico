package com.empresa.soporte_tecnico.controller;

import com.empresa.soporte_tecnico.dto.TecnicoDto;
import com.empresa.soporte_tecnico.mapper.TecnicoMapper;
import com.empresa.soporte_tecnico.model.Tecnico;
import com.empresa.soporte_tecnico.repository.TecnicoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tecnicos")
@Tag(name = "Técnicos", description = "Gestión de técnicos del sistema de soporte técnico")
public class TecnicoController {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Operation(
            summary = "Registrar un nuevo técnico",
            description = "Guarda un nuevo técnico en la base de datos",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Técnico creado correctamente",
                            content = @Content(schema = @Schema(implementation = TecnicoDto.class)))
            }
    )
    @PostMapping
    public ResponseEntity<TecnicoDto> crearTecnico(@Valid @RequestBody TecnicoDto tecnicoDto) {
        Tecnico tecnico = TecnicoMapper.toEntity(tecnicoDto);
        tecnico = tecnicoRepository.save(tecnico);
        return ResponseEntity.ok(TecnicoMapper.toDto(tecnico));
    }

    @Operation(summary = "Listar todos los técnicos")
    @GetMapping
    public ResponseEntity<List<TecnicoDto>> obtenerTecnicos() {
        List<TecnicoDto> tecnicos = tecnicoRepository.findAll()
                .stream()
                .map(TecnicoMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tecnicos);
    }

    @Operation(summary = "Obtener un técnico por ID")
    @GetMapping("/{id}")
    public ResponseEntity<TecnicoDto> obtenerTecnicoPorId(@PathVariable Long id) {

        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Técnico no encontrado con ID " + id
                        )
                );

        return ResponseEntity.ok(TecnicoMapper.toDto(tecnico));
    }

    @Operation(summary = "Actualizar los datos de un técnico")
    @PutMapping("/{id}")
    public ResponseEntity<TecnicoDto> actualizarTecnico(@PathVariable Long id, @Valid @RequestBody TecnicoDto tecnicoDto) {

        if (!tecnicoRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se puede actualizar: el técnico con ID " + id + " no existe."
            );
        }

        Tecnico tecnico = TecnicoMapper.toEntity(tecnicoDto);
        tecnico.setId(id);

        Tecnico actualizado = tecnicoRepository.save(tecnico);
        return ResponseEntity.ok(TecnicoMapper.toDto(actualizado));
    }

    @Operation(summary = "Eliminar un técnico por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTecnico(@PathVariable Long id) {

        if (!tecnicoRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se puede eliminar: el técnico con ID " + id + " no existe."
            );
        }

        tecnicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

