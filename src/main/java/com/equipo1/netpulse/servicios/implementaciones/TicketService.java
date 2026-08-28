package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Ticket;
import com.equipo1.netpulse.repositorios.ITicketRepository;
import com.equipo1.netpulse.servicios.interfaces.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService implements ITicketService {

    @Autowired
    private ITicketRepository ticketRepository;

    @Override
    public Ticket registrar(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket buscarPorId(Integer id) {
        return ticketRepository.findById(id).get();
    }

    @Override
    public List<Ticket> obtenerTodos() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket asignarTecnico(Ticket ticket) {
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
