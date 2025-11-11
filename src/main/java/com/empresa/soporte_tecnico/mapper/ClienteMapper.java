package com.empresa.soporte_tecnico.mapper;

import com.empresa.soporte_tecnico.dto.ClienteDto;
import com.empresa.soporte_tecnico.model.Cliente;

public class ClienteMapper {

    public static ClienteDto toDto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        ClienteDto dto = new ClienteDto();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setCorreo(cliente.getCorreo());
        return dto;
    }

    public static Cliente toEntity(ClienteDto dto) {
        if (dto == null) {
            return null;
        }

        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setNombre(dto.getNombre());
        cliente.setCorreo(dto.getCorreo());
        return cliente;
    }
}
