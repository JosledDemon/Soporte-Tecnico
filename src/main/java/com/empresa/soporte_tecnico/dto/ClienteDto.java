package com.empresa.soporte_tecnico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa la información básica de un cliente")
public class ClienteDto {


    @Schema(description = "Identificador único del cliente", example = "1")
    private Long id;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Schema(description = "Nombre completo del cliente", example = "Juan Pérez")
    private String nombre;

    @NotBlank(message = "El correo del cliente es obligatorio")
    @Email(message = "El formato del correo no es válido")
    @Schema(description = "Correo electrónico del cliente", example = "juan.perez@empresa.com")
    private String correo;
}
