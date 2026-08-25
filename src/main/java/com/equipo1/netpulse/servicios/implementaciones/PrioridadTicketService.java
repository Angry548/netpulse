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
    public Page<PrioridadTicket> obtenerTodosPaginados(Pageable pageable) {
        return prioridadTicketRepository.findAll(pageable);
    }

    @Override
    public List<PrioridadTicket> obtenerTodos() {
        return prioridadTicketRepository.findAll();
    }

    @Override
    public PrioridadTicket obtenerPorId(Integer id) {
        return prioridadTicketRepository.findById(id).get();
    }

    @Override
    public PrioridadTicket obtenerPorNombre(String nombre) {
        return prioridadTicketRepository.findByNombre(nombre).orElse(null);
    }

    @Override
    public PrioridadTicket crearOEditar(PrioridadTicket prioridadTicket) {
        return prioridadTicketRepository.save(prioridadTicket);
    }

    @Override
    public void eliminarPorId(Integer id) {
        prioridadTicketRepository.deleteById(id);
    }
}