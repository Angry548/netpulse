package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.PrioridadTicket;
import com.equipo1.netpulse.repositorios.IPrioridadTicketRepository;
import com.equipo1.netpulse.servicios.interfaces.IPrioridadTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrioridadTicketService implements IPrioridadTicketService {

    @Autowired
    private IPrioridadTicketRepository prioridadTicketRepository;

    @Override
    public PrioridadTicket crear(PrioridadTicket prioridad) {
        return prioridadTicketRepository.save(prioridad);
    }

    @Override
    public PrioridadTicket buscarPorId(Integer id) {
        return prioridadTicketRepository.findById(id).get();
    }

    @Override
    public PrioridadTicket buscarPorNombre(String nombre) {
        return prioridadTicketRepository.findByNombre(nombre).orElse(null);
    }

    @Override
    public List<PrioridadTicket> obtenerTodos() {
        return prioridadTicketRepository.findAll();
    }

    @Override
    public Page<PrioridadTicket> buscarTodosPaginados(Pageable pageable) {
        return prioridadTicketRepository.findAll(pageable);
    }

    @Override
    public PrioridadTicket actualizar(PrioridadTicket prioridad) {
        return prioridadTicketRepository.save(prioridad);
    }

    @Override
    public void eliminarPorId(Integer id) {
        prioridadTicketRepository.deleteById(id);
    }
}
