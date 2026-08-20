package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.EstadoTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEstadoTicketService {

    List<EstadoTicket> obtenerTodos();

    Page<EstadoTicket> buscarTodosPaginados(Pageable pageable);

    EstadoTicket buscarPorId(Integer id);

    EstadoTicket buscarPorNombre(String nombre);

    EstadoTicket crearOEditar(EstadoTicket estadoTicket);

    void eliminarPorId(Integer id);
}
