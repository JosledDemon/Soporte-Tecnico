package com.empresa.soporte_tecnico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la gestión de solicitudes de soporte técnico")
public class SolicitudDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Identificador único de la solicitud", example = "100")
    private Long id;

    @NotBlank(message = "La descripción es obligatoria")
    @Schema(description = "Descripción del problema o solicitud", example = "El sistema no arranca correctamente")
    private String descripcion;

    @NotNull(message = "Debe asociarse un cliente")
    @Schema(description = "Cliente que realiza la solicitud", example = "{\"id\":1}")
    private ClienteDto cliente; // Solo necesitamos que tenga el ID en el POST

    @Schema(description = "Técnico asignado para la solicitud", example = "{\"id\":10}")
    private TecnicoDto tecnicoAsignado; // Solo necesitamos el ID en el POST

    @Schema(description = "Estado actual de la solicitud", example = "Pendiente")
    private String estado;
}
