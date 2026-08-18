package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITicketRepository extends JpaRepository<Ticket, Integer> {
}
