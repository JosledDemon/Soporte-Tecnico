package com.empresa.soporte_tecnico.controller;

import com.empresa.soporte_tecnico.dto.ClienteDto;
import com.empresa.soporte_tecnico.mapper.ClienteMapper;
import com.empresa.soporte_tecnico.model.Cliente;
import com.empresa.soporte_tecnico.repository.ClienteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Gestión de clientes del sistema de soporte técnico")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Operation(
            summary = "Crear un nuevo cliente",
            description = "Crea un cliente nuevo en la base de datos",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente creado correctamente",
                            content = @Content(schema = @Schema(implementation = ClienteDto.class)))
            }
    )
    @PostMapping
    public ResponseEntity<ClienteDto> crearCliente(@Valid @RequestBody ClienteDto clienteDto) {
        Cliente cliente = ClienteMapper.toEntity(clienteDto);
        cliente = clienteRepository.save(cliente);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

    @Operation(summary = "Listar todos los clientes")
    @GetMapping
    public ResponseEntity<List<ClienteDto>> obtenerClientes() {
        List<ClienteDto> clientes = clienteRepository.findAll()
                .stream()
                .map(ClienteMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    @Operation(summary = "Obtener un cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtenerClientePorId(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(ClienteMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar un cliente existente")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> actualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteDto clienteDto) {
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Cliente cliente = ClienteMapper.toEntity(clienteDto);
        cliente.setId(id);
        cliente = clienteRepository.save(cliente);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

    @Operation(summary = "Eliminar un cliente por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
