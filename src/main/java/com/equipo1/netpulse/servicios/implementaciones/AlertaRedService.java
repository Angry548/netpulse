package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.AlertaRed;
import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.repositorios.IAlertaRedRepository;
import com.equipo1.netpulse.servicios.interfaces.IAlertaRedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertaRedService implements IAlertaRedService {

    @Autowired
    private IAlertaRedRepository alertaRedRepository;

    @Override
    public AlertaRed generar(AlertaRed alerta) {
        return alertaRedRepository.save(alerta);
    }

    @Override
    public AlertaRed buscarPorId(Integer id) {
        return alertaRedRepository.findById(id).get();
    }

    @Override
    public List<AlertaRed> obtenerTodos() {
        return alertaRedRepository.findAll();
    }

    @Override
    public Page<AlertaRed> buscarTodosPaginados(Pageable pageable) {
        return alertaRedRepository.findAll(pageable);
    }

    @Override
    public List<AlertaRed> obtenerPorEquipo(Equipo equipo) {
        return alertaRedRepository.findByEquipo(equipo);
    }

    @Override
    public List<AlertaRed> obtenerNoNotificadas() {
        return alertaRedRepository.findByNotificacionEnviadaFalse();
    }

    @Override
    public List<AlertaRed> obtenerPorTipoEvento(String tipoEvento) {
        return alertaRedRepository.findByTipoEvento(tipoEvento);
    }

    @Override
    public AlertaRed enviarNotificacion(AlertaRed alerta) {
        return alertaRedRepository.save(alerta);
    }

    @Override
    public AlertaRed marcarNotificacionEnviada(AlertaRed alerta) {
        return alertaRedRepository.save(alerta);
    }

    @Override
    public void eliminarPorId(Integer id) {
        alertaRedRepository.deleteById(id);
    }
}
