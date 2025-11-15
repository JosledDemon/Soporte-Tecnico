package com.empresa.soporte_tecnico.service;

import com.empresa.soporte_tecnico.model.Cliente;
import com.empresa.soporte_tecnico.model.Solicitud;
import com.empresa.soporte_tecnico.model.Tecnico;
import com.empresa.soporte_tecnico.repository.ClienteRepository;
import com.empresa.soporte_tecnico.repository.SolicitudRepository;
import com.empresa.soporte_tecnico.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteRepository clienteRepository;
    private final TecnicoRepository tecnicoRepository;

    @Autowired
    public SolicitudServiceImpl(SolicitudRepository solicitudRepository,
                                ClienteRepository clienteRepository,
                                TecnicoRepository tecnicoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.clienteRepository = clienteRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    @Override
    public Solicitud crearSolicitud(Solicitud solicitud) {

        // Validar cliente
        if (solicitud.getCliente() == null || solicitud.getCliente().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe especificar el ID del cliente"
            );
        }

        Cliente cliente = clienteRepository.findById(solicitud.getCliente().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente no encontrado con id " + solicitud.getCliente().getId() + " no existe"
                ));
        solicitud.setCliente(cliente);

        // Validar técnico si viene
        if (solicitud.getTecnicoAsignado() != null && solicitud.getTecnicoAsignado().getId() != null) {
            Tecnico tecnico = tecnicoRepository.findById(solicitud.getTecnicoAsignado().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Técnico no encontrado con id " + solicitud.getTecnicoAsignado().getId() + " no existe"
                    ));
            solicitud.setTecnicoAsignado(tecnico);
        } else {
            solicitud.setTecnicoAsignado(null);
        }

        // Estado por defecto
        if (solicitud.getEstado() == null || solicitud.getEstado().isEmpty()) {
            solicitud.setEstado("Pendiente");
        }

        return solicitudRepository.save(solicitud);
    }

    @Override
    public List<Solicitud> obtenerSolicitudes() {
        return solicitudRepository.findAll();
    }

    @Override
    public Solicitud obtenerPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Solicitud no encontrada con id " + id + " no existe."
                ));
    }

    @Override
    public Solicitud actualizarSolicitud(Long id, Solicitud solicitud) {

        // Verificar existencia
        Solicitud existente = solicitudRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Solicitud no encontrada con id " + id
                ));

        // Validar cliente
        if (solicitud.getCliente() != null && solicitud.getCliente().getId() != null) {
            Cliente cliente = clienteRepository.findById(solicitud.getCliente().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Cliente no encontrado con id " + solicitud.getCliente().getId()
                    ));
            solicitud.setCliente(cliente);
        } else {
            solicitud.setCliente(existente.getCliente());
        }

        // Validar técnico
        if (solicitud.getTecnicoAsignado() != null && solicitud.getTecnicoAsignado().getId() != null) {
            Tecnico tecnico = tecnicoRepository.findById(solicitud.getTecnicoAsignado().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Técnico no encontrado con id " + solicitud.getTecnicoAsignado().getId()
                    ));
            solicitud.setTecnicoAsignado(tecnico);
        } else {
            solicitud.setTecnicoAsignado(existente.getTecnicoAsignado());
        }

        solicitud.setId(id);
        return solicitudRepository.save(solicitud);
    }

    @Override
    public void eliminarSolicitud(Long id) {
        if (!solicitudRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Solicitud no encontrada con id " + id
            );
        }
        solicitudRepository.deleteById(id);
    }
}
