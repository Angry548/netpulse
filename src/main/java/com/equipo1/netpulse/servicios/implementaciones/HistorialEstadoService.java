package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.HistorialEstado;
import com.equipo1.netpulse.repositorios.IHistorialEstadoRepository;
import com.equipo1.netpulse.servicios.interfaces.IHistorialEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialEstadoService implements IHistorialEstadoService {

    @Autowired
    private IHistorialEstadoRepository historialEstadoRepository;

    @Override
    public List<HistorialEstado> obtenerTodos() {
        return historialEstadoRepository.findAll();
    }

    @Override
    public Page<HistorialEstado> buscarTodosPaginados(Pageable pageable) {
        return historialEstadoRepository.findAll(pageable);
    }

    @Override
    public HistorialEstado buscarPorId(Integer id) {
        return historialEstadoRepository.findById(id).get();
    }

    @Override
    public HistorialEstado crearOEditar(HistorialEstado historialEstado) {
        return historialEstadoRepository.save(historialEstado);
    }

    @Override
    public void eliminarPorId(Integer id) {
        historialEstadoRepository.deleteById(id);
    }
}