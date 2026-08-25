package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.HistorialEstado;
import com.equipo1.netpulse.repositorios.IHistorialEstadoRepository;
import com.equipo1.netpulse.servicios.interfaces.IHistorialEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialEstadoService implements IHistorialEstadoService {

    @Autowired
    private IHistorialEstadoRepository historialEstadoRepository;

    @Override
    public HistorialEstado registrarCambio(HistorialEstado historial) {
        return historialEstadoRepository.save(historial);
    }

    @Override
    public HistorialEstado buscarPorId(Integer id) {
        return historialEstadoRepository.findById(id).get();
    }

    @Override
    public List<HistorialEstado> obtenerTodos() {
        return historialEstadoRepository.findAll();
    }

    @Override
    public void eliminarPorId(Integer id) {
        historialEstadoRepository.deleteById(id);
    }
}