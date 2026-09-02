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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService implements ITicketService {

    private final ITicketRepository ticketRepository;

    public TicketService(ITicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket registrar(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket buscarPorId(Integer id) {
        return ticketRepository.findById(id).orElse(null);
    }

    @Override
    public List<Ticket> obtenerTodos() {
        return ticketRepository.findAll();
    }

    @Override
    public Page<Ticket> buscarTodosPaginados(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    @Override
    public List<Ticket> obtenerPorUsuarioReporta(Usuario usuario) {
        return ticketRepository.findByUsuarioReporta(usuario);
    }

    @Override
    public List<Ticket> obtenerPorTecnico(Usuario usuario) {
        return ticketRepository.findByTecnico(usuario);
    }

    @Override
    public List<Ticket> obtenerPorEquipo(Equipo equipo) {
        return ticketRepository.findByEquipo(equipo);
    }

    @Override
    public List<Ticket> obtenerPorCategoria(CategoriaIncidencia categoria) {
        return ticketRepository.findByCategoria(categoria);
    }

    @Override
    public List<Ticket> obtenerPorPrioridad(PrioridadTicket prioridad) {
        return ticketRepository.findByPrioridad(prioridad);
    }

    @Override
    public List<Ticket> obtenerPorEstado(EstadoTicket estado) {
        return ticketRepository.findByEstadoTicket(estado);
    }

    @Override
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
    public Ticket cambiarEstado(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket cambiarPrioridad(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket resolver(Ticket ticket) {

        if (ticket.getFechaResolucion() == null) {

            ticket.setFechaResolucion(
                    LocalDateTime.now()
            );
        }

        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket actualizar(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void eliminarPorId(Integer id) {
        ticketRepository.deleteById(id);
    }

}