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
        try {
            // 🟢 Logs para depuración
            System.out.println("=== Datos recibidos en crearSolicitud() ===");
            System.out.println("Descripción: " + solicitud.getDescripcion());
            System.out.println("Cliente: " + (solicitud.getCliente() != null ? solicitud.getCliente().getId() : "null"));
            System.out.println("Técnico: " + (solicitud.getTecnicoAsignado() != null ? solicitud.getTecnicoAsignado().getId() : "null"));
            System.out.println("Estado: " + solicitud.getEstado());
            System.out.println("===========================================");
            // Verificar que el cliente venga con ID
            if (solicitud.getCliente() == null || solicitud.getCliente().getId() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Debe especificar el ID del cliente"
                );
            }

            // Buscar cliente existente
            Cliente cliente = clienteRepository.findById(solicitud.getCliente().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Cliente no encontrado con id " + solicitud.getCliente().getId()
                    ));
            solicitud.setCliente(cliente);

            // Si tiene técnico asignado, buscarlo
            if (solicitud.getTecnicoAsignado() != null && solicitud.getTecnicoAsignado().getId() != null) {
                Tecnico tecnico = tecnicoRepository.findById(solicitud.getTecnicoAsignado().getId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Técnico no encontrado con id " + solicitud.getTecnicoAsignado().getId()
                        ));
                solicitud.setTecnicoAsignado(tecnico);
            } else {
                solicitud.setTecnicoAsignado(null);
            }

            // Asignar estado por defecto si no viene
            if (solicitud.getEstado() == null || solicitud.getEstado().isEmpty()) {
                solicitud.setEstado("Pendiente");
            }

            // 🔥 Verificación final
            if (solicitud.getCliente() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Debe asociarse un cliente válido"
                );
            }

            return solicitudRepository.save(solicitud);

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al crear la solicitud: " + ex.getMessage(),
                    ex
            );
        }
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
                        "Solicitud no encontrada con id " + id
                ));
    }

    @Override
    public Solicitud actualizarSolicitud(Long id, Solicitud solicitud) {
        if (!solicitudRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada con id " + id);
        }

        // Revalidar cliente y técnico antes de actualizar
        if (solicitud.getCliente() != null && solicitud.getCliente().getId() != null) {
            Cliente cliente = clienteRepository.findById(solicitud.getCliente().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
            solicitud.setCliente(cliente);
        }

        if (solicitud.getTecnicoAsignado() != null && solicitud.getTecnicoAsignado().getId() != null) {
            Tecnico tecnico = tecnicoRepository.findById(solicitud.getTecnicoAsignado().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Técnico no encontrado"));
            solicitud.setTecnicoAsignado(tecnico);
        }

        solicitud.setId(id);
        return solicitudRepository.save(solicitud);
    }

    @Override
    public void eliminarSolicitud(Long id) {
        if (!solicitudRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada con id " + id);
        }
        solicitudRepository.deleteById(id);
    }
}
