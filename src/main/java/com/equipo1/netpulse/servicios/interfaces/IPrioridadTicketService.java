package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.PrioridadTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPrioridadTicketService {

    Page<PrioridadTicket> obtenerTodosPaginados(Pageable pageable);

    List<PrioridadTicket> obtenerTodos();

    PrioridadTicket obtenerPorId(Integer id);

    PrioridadTicket obtenerPorNombre(String nombre);

    PrioridadTicket crearOEditar(PrioridadTicket prioridadTicket);

    void eliminarPorId(Integer id);
}
