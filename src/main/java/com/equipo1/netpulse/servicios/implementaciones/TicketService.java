package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.EstadoTicket;
import com.equipo1.netpulse.modelos.PrioridadTicket;
import com.equipo1.netpulse.modelos.Ticket;
import com.equipo1.netpulse.modelos.Usuario;

import com.equipo1.netpulse.repositorios.ITicketRepository;
import com.equipo1.netpulse.servicios.interfaces.ITicketService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService implements ITicketService {

    private final ITicketRepository ticketRepository;

    public TicketService(ITicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    @Transactional
    public Ticket registrar(Ticket ticket) {

        if (ticket.getFechaCreacion() == null) {
            ticket.setFechaCreacion(LocalDateTime.now());
        }

        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public Ticket buscarPorId(Integer id) {

        return ticketRepository
                .findByIdWithRelations(id)
                .orElse(null);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Ticket> obtenerTodos() {

        return ticketRepository.findAllWithRelations();
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Ticket> buscarTodosPaginados(Pageable pageable) {

        return ticketRepository.findAllWithRelations(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Ticket> obtenerPorUsuarioReporta(
            Usuario usuario) {

        return ticketRepository
                .findByUsuarioReportaWithRelations(usuario);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Ticket> obtenerPorTecnico(
            Usuario usuario) {

        return ticketRepository
                .findByTecnicoWithRelations(usuario);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Ticket> obtenerPorEquipo(
            Equipo equipo) {

        return ticketRepository
                .findByEquipoWithRelations(equipo);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Ticket> obtenerPorCategoria(
            CategoriaIncidencia categoria) {

        return ticketRepository
                .findByCategoriaWithRelations(categoria);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Ticket> obtenerPorPrioridad(
            PrioridadTicket prioridad) {

        return ticketRepository
                .findByPrioridadWithRelations(prioridad);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Ticket> obtenerPorEstado(
            EstadoTicket estado) {

        return ticketRepository
                .findByEstadoTicketWithRelations(estado);
    }


    @Override
    @Transactional
    public Ticket asignarTecnico(Ticket ticket) {

        if (ticket.getTecnico() != null
                && ticket.getFechaAsignacion() == null) {

            ticket.setFechaAsignacion(
                    LocalDateTime.now()
            );
        }

        return ticketRepository.save(ticket);
    }


    @Override
    @Transactional
    public Ticket cambiarEstado(Ticket ticket) {

        return ticketRepository.save(ticket);
    }


    @Override
    @Transactional
    public Ticket cambiarPrioridad(Ticket ticket) {

        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public Ticket resolver(Ticket ticket) {

        if (ticket.getFechaResolucion() == null) {

            ticket.setFechaResolucion(
                    LocalDateTime.now()
            );
        }

        return ticketRepository.save(ticket);
    }


    @Override
    @Transactional
    public Ticket actualizar(Ticket ticket) {

        return ticketRepository.save(ticket);
    }


    @Override
    @Transactional
    public void eliminarPorId(Integer id) {

        ticketRepository.deleteById(id);
    }

}