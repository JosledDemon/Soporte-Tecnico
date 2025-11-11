package com.empresa.soporte_tecnico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa un técnico de soporte")
public class TecnicoDto {


    @Schema(description = "Identificador único del técnico", example = "10")
    private Long id;

    @NotBlank(message = "El nombre del técnico es obligatorio")
    @Schema(description = "Nombre completo del técnico", example = "Carlos López")
    private String nombre;

    @Schema(description = "Especialidad o área del técnico", example = "Redes")
    private String especialidad;
}
