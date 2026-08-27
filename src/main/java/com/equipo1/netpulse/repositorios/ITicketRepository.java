package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Ticket;
import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import com.equipo1.netpulse.modelos.PrioridadTicket;
import com.equipo1.netpulse.modelos.EstadoTicket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITicketRepository extends JpaRepository<Ticket, Integer> {

    Page<Ticket> findAll(Pageable pageable);

    List<Ticket> findByUsuarioReporta(Usuario usuario);

    List<Ticket> findByUsuarioTecnico(Usuario usuario);

    List<Ticket> findByEquipo(Equipo equipo);

    List<Ticket> findByCategoria(CategoriaIncidencia categoria);

    List<Ticket> findByPrioridad(PrioridadTicket prioridad);

    List<Ticket> findByEstado(EstadoTicket estado);
}
