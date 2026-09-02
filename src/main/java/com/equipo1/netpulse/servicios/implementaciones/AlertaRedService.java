package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.AlertaRed;
import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.repositorios.IAlertaRedRepository;
import com.equipo1.netpulse.servicios.interfaces.IAlertaRedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaRedService implements IAlertaRedService {

    private final IAlertaRedRepository alertaRedRepository;

    public AlertaRedService(
            IAlertaRedRepository alertaRedRepository) {

        this.alertaRedRepository = alertaRedRepository;
    }

    @Override
    public AlertaRed generar(AlertaRed alerta) {

        if (alerta.getFecha() == null) {
            alerta.setFecha(LocalDateTime.now());
        }

        if (alerta.getNotificacionEnviada() == null) {
            alerta.setNotificacionEnviada(false);
        }

        return alertaRedRepository.save(alerta);
    }

    @Override
    @Transactional(readOnly = true)
    public AlertaRed buscarPorId(Integer id) {

        return alertaRedRepository
                .findByIdWithEquipo(id)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaRed> obtenerTodos() {

        return alertaRedRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertaRed> buscarTodosPaginados(
            Pageable pageable) {

        return alertaRedRepository
                .findAllWithEquipo(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertaRed> buscarPorIdPaginado(
            Integer id,
            Pageable pageable) {

        return alertaRedRepository
                .findByIdPaginado(id, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertaRed> buscarPorTipoEvento(
            String tipoEvento,
            Pageable pageable) {

        return alertaRedRepository
                .findByTipoEventoContainingIgnoreCase(
                        tipoEvento,
                        pageable
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaRed> obtenerPorEquipo(
            Equipo equipo) {

        return alertaRedRepository
                .findByEquipo(equipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaRed> obtenerNoNotificadas() {

        return alertaRedRepository
                .findByNotificacionEnviadaFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaRed> obtenerPorTipoEvento(
            String tipoEvento) {

        return alertaRedRepository
                .findByTipoEvento(tipoEvento);
    }

    @Override
    public AlertaRed enviarNotificacion(
            AlertaRed alerta) {

        alerta.setNotificacionEnviada(true);

        return alertaRedRepository.save(alerta);
    }

    @Override
    public AlertaRed marcarNotificacionEnviada(
            AlertaRed alerta) {

        alerta.setNotificacionEnviada(true);

        return alertaRedRepository.save(alerta);
    }


    @Override
    public void eliminarPorId(Integer id) {

        alertaRedRepository.deleteById(id);
    }
}