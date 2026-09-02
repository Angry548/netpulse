package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.PrioridadTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPrioridadTicketService {

    PrioridadTicket crear(PrioridadTicket prioridad);

    PrioridadTicket buscarPorId(Integer id);

    PrioridadTicket buscarPorNombre(String nombre);

    Page<PrioridadTicket> buscarPorNombrePaginado(
            String nombre,
            Pageable pageable
    );

    List<PrioridadTicket> obtenerTodos();

    Page<PrioridadTicket> buscarTodosPaginados(
            Pageable pageable
    );

    PrioridadTicket actualizar(PrioridadTicket prioridad);

    void eliminarPorId(Integer id);
}