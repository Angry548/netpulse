package com.equipo1.netpulse.servicios.interfaces;

import com.equipo1.netpulse.modelos.Ticket;

import java.util.List;

public interface ITicketService {

    Ticket registrar(Ticket ticket);

    Ticket buscarPorId(Integer id);

    List<Ticket> obtenerTodos();

    Ticket asignarTecnico(Ticket ticket);

    Ticket cambiarEstado(Ticket ticket);

    Ticket cambiarPrioridad(Ticket ticket);

    Ticket resolver(Ticket ticket);

    Ticket actualizar(Ticket ticket);

    void eliminarPorId(Integer id);
}

