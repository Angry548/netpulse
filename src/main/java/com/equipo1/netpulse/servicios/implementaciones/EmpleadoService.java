package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Empleado;
import com.equipo1.netpulse.repositorios.IEmpleadoRepository;
import com.equipo1.netpulse.servicios.interfaces.IEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService implements IEmpleadoService {

    @Autowired
    private IEmpleadoRepository empleadoRepository;

    @Override
    public Page<Empleado> obtenerTodosPaginados(Pageable pageable) {
        return empleadoRepository.findAll(pageable);
    }

    @Override
    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAll();
    }

    @Override
    public Empleado obtenerPorId(Integer id) {
        return empleadoRepository.findById(id).get();
    }

    @Override
    public Empleado crearOEditar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public void eliminarPorId(Integer id) {
        empleadoRepository.deleteById(id);
    }

    @Override
    public Empleado obtenerPorCodigoEmpleado(String codigoEmpleado) {
        return empleadoRepository.findByCodigoEmpleado(codigoEmpleado).orElse(null);
    }
}