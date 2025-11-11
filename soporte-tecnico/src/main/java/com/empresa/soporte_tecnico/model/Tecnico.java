package com.empresa.soporte_tecnico.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class Tecnico {
    private Long id;

    @NotBlank(message = "El nombre del técnico es obligatorio")
    private String nombre;

    private String especialidad;
}
