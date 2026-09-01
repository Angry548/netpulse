package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.AlertaRed;
import com.equipo1.netpulse.modelos.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAlertaRedRepository extends JpaRepository<AlertaRed, Integer> {

    List<AlertaRed> findByEquipo(Equipo equipo);

    List<AlertaRed> findByNotificacionEnviadaFalse();

    List<AlertaRed> findByTipoEvento(String tipoEvento);
}
