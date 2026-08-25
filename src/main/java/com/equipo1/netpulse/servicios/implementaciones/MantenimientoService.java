package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Mantenimiento;
import com.equipo1.netpulse.repositorios.IMantenimientoRepository;
import com.equipo1.netpulse.servicios.interfaces.IMantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MantenimientoService implements IMantenimientoService {

    @Autowired
    private IMantenimientoRepository mantenimientoRepository;

    @Override
    public Mantenimiento registrar(Mantenimiento mantenimiento) {
        return mantenimientoRepository.save(mantenimiento);
    }

    @Override
    public Mantenimiento buscarPorId(Integer id) {
        return mantenimientoRepository.findById(id).get();
    }

    @Override
    public List<Mantenimiento> obtenerTodos() {
        return mantenimientoRepository.findAll();
    }

    @Override
    public Mantenimiento actualizar(Mantenimiento mantenimiento) {
        return mantenimientoRepository.save(mantenimiento);
    }

    @Override
    public Mantenimiento finalizar(Mantenimiento mantenimiento) {
        return mantenimientoRepository.save(mantenimiento);
    }

    @Override
    public void eliminarPorId(Integer id) {
        mantenimientoRepository.deleteById(id);
    }
}
