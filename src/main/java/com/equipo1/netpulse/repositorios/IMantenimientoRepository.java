package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Mantenimiento;
import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.modelos.Ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IMantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {

    Page<Mantenimiento> findAll(Pageable pageable);

    List<Mantenimiento> findByEquipo(Equipo equipo);

    List<Mantenimiento> findByUsuario(Usuario usuario);

    List<Mantenimiento> findByTicket(Ticket ticket);
}


