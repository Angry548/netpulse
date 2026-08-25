package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.EstadoTicket;
import com.equipo1.netpulse.repositorios.IEstadoTicketRepository;
import com.equipo1.netpulse.servicios.interfaces.IEstadoTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoTicketService implements IEstadoTicketService {

    @Autowired
    private IEstadoTicketRepository estadoTicketRepository;

    @Override
    public List<EstadoTicket> obtenerTodos() {
        return estadoTicketRepository.findAll();
    }

    @Override
    public Page<EstadoTicket> buscarTodosPaginados(Pageable pageable) {
        return estadoTicketRepository.findAll(pageable);
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
    public EstadoTicket crearOEditar(EstadoTicket estadoTicket) {
        return estadoTicketRepository.save(estadoTicket);
    }

    @Override
    public void eliminarPorId(Integer id) {
        estadoTicketRepository.deleteById(id);
    }
}