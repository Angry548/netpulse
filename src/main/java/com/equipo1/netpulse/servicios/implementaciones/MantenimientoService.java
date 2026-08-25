package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Mantenimiento;
import com.equipo1.netpulse.repositorios.IMantenimientoRepository;
import com.equipo1.netpulse.servicios.interfaces.IMantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MantenimientoService implements IMantenimientoService {

    @Autowired
    private IMantenimientoRepository mantenimientoRepository;

    @Override
    public List<Mantenimiento> obtenerTodos() {
        return mantenimientoRepository.findAll();
    }

    @Override
    public Page<Mantenimiento> buscarTodosPaginados(Pageable pageable) {
        return mantenimientoRepository.findAll(pageable);
    }

    @Override
    public Mantenimiento buscarPorId(Integer id) {
        return mantenimientoRepository.findById(id).get();
    }

    @Override
    public Mantenimiento crearOEditar(Mantenimiento mantenimiento) {
        return mantenimientoRepository.save(mantenimiento);
    }

    @Override
    public void eliminarPorId(Integer id) {
        mantenimientoRepository.deleteById(id);
    }
}
