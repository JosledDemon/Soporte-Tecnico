package com.empresa.soporte_tecnico.mapper;

import com.empresa.soporte_tecnico.dto.SolicitudDto;
import com.empresa.soporte_tecnico.model.Solicitud;
import com.empresa.soporte_tecnico.model.Cliente;
import com.empresa.soporte_tecnico.model.Tecnico;
import com.empresa.soporte_tecnico.dto.SolicitudDto;
import com.empresa.soporte_tecnico.model.Solicitud;


public class SolicitudMapper {

    public static SolicitudDto toDto(Solicitud solicitud) {
        if (solicitud == null) {
            return null;
        }

        return SolicitudDto.builder()
                .id(solicitud.getId())
                .descripcion(solicitud.getDescripcion())
                .cliente(ClienteMapper.toDto(solicitud.getCliente()))
                .tecnicoAsignado(TecnicoMapper.toDto(solicitud.getTecnicoAsignado()))
                .estado(solicitud.getEstado())
                .build();
    }

    public static Solicitud toEntity(SolicitudDto dto) {
        if (dto == null) return null;

        Solicitud solicitud = new Solicitud();
        solicitud.setId(dto.getId());
        solicitud.setDescripcion(dto.getDescripcion());
        solicitud.setEstado(dto.getEstado());

        // Mapear cliente usando solo el ID
        if (dto.getCliente() != null && dto.getCliente().getId() != null) {
            Cliente cliente = new Cliente();
            cliente.setId(dto.getCliente().getId());
            solicitud.setCliente(cliente);
        }

        // Mapear técnico usando solo el ID
        if (dto.getTecnicoAsignado() != null && dto.getTecnicoAsignado().getId() != null) {
            Tecnico tecnico = new Tecnico();
            tecnico.setId(dto.getTecnicoAsignado().getId());
            solicitud.setTecnicoAsignado(tecnico);
        }

        return solicitud;
    }

}
