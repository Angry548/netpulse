package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.EstadoEquipo;
import com.equipo1.netpulse.repositorios.IEstadoEquipoRepository;
import com.equipo1.netpulse.servicios.interfaces.IEstadoEquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoEquipoService implements IEstadoEquipoService {

    @Autowired
    private IEstadoEquipoRepository estadoEquipoRepository;

    @Override
    public EstadoEquipo crear(EstadoEquipo estadoEquipo) {
        return estadoEquipoRepository.save(estadoEquipo);
    }

    @Override
    public EstadoEquipo buscarPorId(Integer id) {
        return estadoEquipoRepository.findById(id).get();
    }

    @Override
    public EstadoEquipo buscarPorNombre(String nombre) {
        return estadoEquipoRepository.findByNombre(nombre).get();
    }

    @Override
    public List<EstadoEquipo> obtenerTodos() {
        return estadoEquipoRepository.findAll();
    }

    @Override
    public Page<EstadoEquipo> buscarTodosPaginados(Pageable pageable) {
        return estadoEquipoRepository.findAll(pageable);
    }

    @Override
    public EstadoEquipo actualizar(EstadoEquipo estadoEquipo) {
        return estadoEquipoRepository.save(estadoEquipo);
    }

    @Override
    public void eliminarPorId(Integer id) {
        estadoEquipoRepository.deleteById(id);
    }
}