package com.empresa.soporte_tecnico.model;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class Cliente {
    private long id;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String nombre;

    @NotBlank(message = "El correo del cliente es obligatorio")
    private String correo;

}
