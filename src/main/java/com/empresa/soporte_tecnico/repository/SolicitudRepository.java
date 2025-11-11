package com.empresa.soporte_tecnico.repository;

import com.empresa.soporte_tecnico.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    // Aquí podrías agregar métodos personalizados si los necesitas, ej:
    // List<Solicitud> findByEstado(String estado);
}
