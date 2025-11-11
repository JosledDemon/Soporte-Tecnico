package com.empresa.soporte_tecnico.mapper;

import com.empresa.soporte_tecnico.dto.TecnicoDto;
import com.empresa.soporte_tecnico.model.Tecnico;

public class TecnicoMapper {

    public static TecnicoDto toDto(Tecnico tecnico) {
        if (tecnico == null) {
            return null;
        }

        TecnicoDto dto = new TecnicoDto();
        dto.setId(tecnico.getId());
        dto.setNombre(tecnico.getNombre());
        dto.setEspecialidad(tecnico.getEspecialidad());
        return dto;
    }

    public static Tecnico toEntity(TecnicoDto dto) {
        if (dto == null) {
            return null;
        }

        Tecnico tecnico = new Tecnico();
        tecnico.setId(dto.getId());
        tecnico.setNombre(dto.getNombre());
        tecnico.setEspecialidad(dto.getEspecialidad());
        return tecnico;
    }
}
