package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.HistorialEstado;
import com.equipo1.netpulse.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IHistorialEstadoRepository extends JpaRepository<HistorialEstado, Integer> {

    List<HistorialEstado> findByEquipo(Equipo equipo);

    List<HistorialEstado> findByUsuario(Usuario usuario);
}