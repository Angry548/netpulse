package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITicketService {

    List<Ticket> obtenerTodos();

    Page<Ticket> buscarTodosPaginados(Pageable pageable);

    Ticket buscarPorId(Integer id);

    Ticket crearOEditar(Ticket ticket);

    void eliminarPorId(Integer id);
}
