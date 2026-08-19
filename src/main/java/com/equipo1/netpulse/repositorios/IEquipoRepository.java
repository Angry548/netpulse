package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEquipoRepository extends JpaRepository<Equipo, Integer> {

    Optional<Equipo> findByNumeroSerie(String numeroSerie);
}
