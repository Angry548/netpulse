package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.PrioridadTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPrioridadTicketRepository
        extends JpaRepository<PrioridadTicket, Integer> {

    Optional<PrioridadTicket> findByNombre(String nombre);

    Page<PrioridadTicket> findByNombreContainingIgnoreCase(
            String nombre,
            Pageable pageable
    );

    Page<PrioridadTicket> findAll(Pageable pageable);
}