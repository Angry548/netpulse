package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Empleado;
import com.equipo1.netpulse.modelos.EstadoEmpleado;
import com.equipo1.netpulse.modelos.Usuario;
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
    public Empleado registrar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado buscarPorId(Integer id) {
        return empleadoRepository.findById(id).get();
    }

    @Override
    public Empleado buscarPorCodigoEmpleado(String codigoEmpleado) {
        return empleadoRepository.findByCodigoEmpleado(codigoEmpleado).get();
    }

    @Override
    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAll();
    }

    @Override
    public Page<Empleado> buscarTodosPaginados(Pageable pageable) {
        return empleadoRepository.findAll(pageable);
    }

    @Override
    public Empleado obtenerPorUsuario(Usuario usuario) {
        return empleadoRepository.findByUsuario(usuario).get();
    }

    @Override
    public Empleado actualizar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado activar(Empleado empleado) {
        empleado.setEstadoLaboral(EstadoEmpleado.ACTIVO);
        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado desactivar(Empleado empleado) {
        empleado.setEstadoLaboral(EstadoEmpleado.INACTIVO);
        return empleadoRepository.save(empleado);
    }

    @Override
    public void eliminarPorId(Integer id) {
        empleadoRepository.deleteById(id);
    }
}