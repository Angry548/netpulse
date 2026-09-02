package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.EstadoTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEstadoTicketRepository
        extends JpaRepository<EstadoTicket, Integer> {

    Optional<EstadoTicket> findByNombre(String nombre);

    Page<EstadoTicket> findByNombreContainingIgnoreCase(
            String nombre,
            Pageable pageable
    );

    Page<EstadoTicket> findAll(Pageable pageable);
}