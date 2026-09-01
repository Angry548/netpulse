package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.EstadoEquipo;
import com.equipo1.netpulse.modelos.TipoEquipo;
import com.equipo1.netpulse.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IEquipoRepository extends JpaRepository<Equipo, Integer> {

    Optional<Equipo> findByNumeroSerie(String numeroSerie);

    List<Equipo> findByTipo(TipoEquipo tipoEquipo);

    List<Equipo> findByEstado(EstadoEquipo estadoEquipo);

    List<Equipo> findByResponsable(Usuario usuario);
}