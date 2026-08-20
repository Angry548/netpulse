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
    public Page<TipoEquipo> obtenerTodosPaginados(Pageable pageable) {
        return tipoEquipoRepository.findAll(pageable);
    }

    @Override
    public List<TipoEquipo> obtenerTodos() {
        return tipoEquipoRepository.findAll();
    }

    @Override
    public TipoEquipo obtenerPorId(Integer id) {
        return tipoEquipoRepository.findById(id).get();
    }

    @Override
    public TipoEquipo crearOEditar(TipoEquipo tipoEquipo) {
        return tipoEquipoRepository.save(tipoEquipo);
    }

    @Override
    public void eliminarPorId(Integer id) {
        tipoEquipoRepository.deleteById(id);
    }

    @Override
    public TipoEquipo obtenerPorNombre(String nombre) {
        return tipoEquipoRepository.findByNombre(nombre).orElse(null);
    }
}