package com.empresa.soporte_tecnico.controller;

import com.empresa.soporte_tecnico.model.Solicitud;
import com.empresa.soporte_tecnico.service.SolicitudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes") // ruta base consistente
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public ResponseEntity<Solicitud> crearSolicitud(@RequestBody Solicitud solicitud) {
        Solicitud creada = solicitudService.crearSolicitud(solicitud);
        return ResponseEntity.ok(creada);
    }

    @GetMapping
    public ResponseEntity<List<Solicitud>> obtenerSolicitudes() {
        List<Solicitud> lista = solicitudService.obtenerSolicitudes();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> obtenerSolicitudPorId(@PathVariable Long id) {
        Solicitud s = solicitudService.obtenerPorId(id);
        return s != null ? ResponseEntity.ok(s) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> actualizarSolicitud(@PathVariable Long id, @RequestBody Solicitud solicitud) {
        Solicitud actualizada = solicitudService.actualizarSolicitud(id, solicitud);
        return actualizada != null ? ResponseEntity.ok(actualizada) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable Long id) {
        solicitudService.eliminarSolicitud(id);
        return ResponseEntity.noContent().build();
    }
}
