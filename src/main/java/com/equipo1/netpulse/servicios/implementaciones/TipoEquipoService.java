package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.TipoEquipo;
import com.equipo1.netpulse.repositorios.ITipoEquipoRepository;
import com.equipo1.netpulse.servicios.interfaces.ITipoEquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoEquipoService implements ITipoEquipoService {

    @Autowired
    private ITipoEquipoRepository tipoEquipoRepository;

    @Override
    public TipoEquipo crear(TipoEquipo tipoEquipo) {
        return tipoEquipoRepository.save(tipoEquipo);
    }

    @Override
    public TipoEquipo buscarPorId(Integer id) {
        return tipoEquipoRepository.findById(id).get();
    }

    @Override
    public TipoEquipo buscarPorNombre(String nombre) {
        return tipoEquipoRepository.findByNombre(nombre).orElse(null);
    }

    @Override
    public List<TipoEquipo> obtenerTodos() {
        return tipoEquipoRepository.findAll();
    }

    @Override
    public Page<TipoEquipo> buscarTodosPaginados(Pageable pageable) {
        return tipoEquipoRepository.findAll(pageable);
    }

    @Override
    public TipoEquipo actualizar(TipoEquipo tipoEquipo) {
        return tipoEquipoRepository.save(tipoEquipo);
    }

    @Override
    public void eliminarPorId(Integer id) {
        tipoEquipoRepository.deleteById(id);
    }
}