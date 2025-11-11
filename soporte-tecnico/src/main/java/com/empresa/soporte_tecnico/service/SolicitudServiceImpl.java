package com.empresa.soporte_tecnico.service;

import com.empresa.soporte_tecnico.model.Solicitud;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SolicitudServiceImpl implements SolicitudService {

    private final Map<Long, Solicitud> solicitudes = new HashMap<>();
    private Long contador = 1L;

    @Override
    public Solicitud crearSolicitud(Solicitud solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        solicitud.setId(contador++);
        solicitudes.put(solicitud.getId(), solicitud);
        return solicitud;
    }

    @Override
    public List<Solicitud> obtenerSolicitudes() {
        return new ArrayList<>(solicitudes.values());
    }

    @Override
    public Solicitud obtenerPorId(Long id) {
        return solicitudes.get(id);
    }

    @Override
    public Solicitud actualizarSolicitud(Long id, Solicitud solicitud) {
        if (!solicitudes.containsKey(id)) return null;
        solicitud.setId(id);
        solicitudes.put(id, solicitud);
        return solicitud;
    }

    @Override
    public void eliminarSolicitud(Long id) {
        solicitudes.remove(id);
    }
}
