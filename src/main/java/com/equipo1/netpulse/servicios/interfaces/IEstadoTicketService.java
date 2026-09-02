package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.EstadoTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEstadoTicketService {

    EstadoTicket crear(EstadoTicket estado);

    EstadoTicket buscarPorId(Integer id);

    EstadoTicket buscarPorNombre(String nombre);

    Page<EstadoTicket> buscarPorNombrePaginado(
            String nombre,
            Pageable pageable
    );

    List<EstadoTicket> obtenerTodos();

    Page<EstadoTicket> buscarTodosPaginados(
            Pageable pageable
    );

    EstadoTicket actualizar(EstadoTicket estado);

    void eliminarPorId(Integer id);
}