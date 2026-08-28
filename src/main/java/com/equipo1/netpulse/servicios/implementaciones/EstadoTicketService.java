package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.EstadoTicket;
import com.equipo1.netpulse.repositorios.IEstadoTicketRepository;
import com.equipo1.netpulse.servicios.interfaces.IEstadoTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoTicketService implements IEstadoTicketService {

    @Autowired
    private IEstadoTicketRepository estadoTicketRepository;

    @Override
    public EstadoTicket crear(EstadoTicket estado) {
        return estadoTicketRepository.save(estado);
    }

    @Override
    public EstadoTicket buscarPorId(Integer id) {
        return estadoTicketRepository.findById(id).get();
    }

    @Override
    public EstadoTicket buscarPorNombre(String nombre) {
        return estadoTicketRepository.findByNombre(nombre).orElse(null);
    }

    @Override
    public List<EstadoTicket> obtenerTodos() {
        return estadoTicketRepository.findAll();
    }

    @Override
    public EstadoTicket actualizar(EstadoTicket estado) {
        return estadoTicketRepository.save(estado);
    }

    @Override
    public void eliminarPorId(Integer id) {
        estadoTicketRepository.deleteById(id);
    }
}