package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.EstadoTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadoTicketRepository extends JpaRepository<EstadoTicket, Integer> {
}

