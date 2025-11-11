package com.empresa.soporte_tecnico.service;

import com.empresa.soporte_tecnico.model.Solicitud;
import java.util.List;

public interface SolicitudService {
    Solicitud crearSolicitud(Solicitud solicitud);
    List<Solicitud> obtenerSolicitudes();
    Solicitud obtenerPorId(Long id);
    Solicitud actualizarSolicitud(Long id, Solicitud solicitud);
    void eliminarSolicitud(Long id);
}
