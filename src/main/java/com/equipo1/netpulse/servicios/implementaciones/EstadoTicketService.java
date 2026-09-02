package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.EstadoTicket;
import com.equipo1.netpulse.repositorios.IEstadoTicketRepository;
import com.equipo1.netpulse.servicios.interfaces.IEstadoTicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoTicketService implements IEstadoTicketService {

    private final IEstadoTicketRepository estadoTicketRepository;

    public EstadoTicketService(
            IEstadoTicketRepository estadoTicketRepository) {

        this.estadoTicketRepository = estadoTicketRepository;
    }

    @Override
    public EstadoTicket crear(
            EstadoTicket estado) {

        return estadoTicketRepository.save(estado);
    }

    @Override
    public EstadoTicket buscarPorId(
            Integer id) {

        Optional<EstadoTicket> estado =
                estadoTicketRepository.findById(id);

        return estado.orElse(null);
    }

    @Override
    public EstadoTicket buscarPorNombre(
            String nombre) {

        Optional<EstadoTicket> estado =
                estadoTicketRepository.findByNombre(nombre);

        return estado.orElse(null);
    }

    @Override
    public Page<EstadoTicket> buscarPorNombrePaginado(
            String nombre,
            Pageable pageable) {

        return estadoTicketRepository
                .findByNombreContainingIgnoreCase(
                        nombre,
                        pageable
                );
    }

    @Override
    public List<EstadoTicket> obtenerTodos() {

        return estadoTicketRepository.findAll();
    }

    @Override
    public Page<EstadoTicket> buscarTodosPaginados(
            Pageable pageable) {

        return estadoTicketRepository.findAll(pageable);
    }

    @Override
    public EstadoTicket actualizar(
            EstadoTicket estado) {

        return estadoTicketRepository.save(estado);
    }

    @Override
    public void eliminarPorId(
            Integer id) {

        estadoTicketRepository.deleteById(id);
    }
}