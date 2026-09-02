package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Empleado;
import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.repositorios.IEmpleadoRepository;
import com.equipo1.netpulse.servicios.interfaces.IEmpleadoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpleadoService implements IEmpleadoService {

    private final IEmpleadoRepository empleadoRepository;

    public EmpleadoService(IEmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public Empleado registrar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public Empleado buscarPorId(Integer id) {

        return empleadoRepository
                .findByIdWithUsuario(id)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Empleado buscarPorCodigoEmpleado(
            String codigoEmpleado) {

        return empleadoRepository
                .findByCodigoEmpleado(codigoEmpleado)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empleado> buscarTodosPaginados(
            Pageable pageable) {

        return empleadoRepository
                .findAllWithUsuario(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empleado> buscarPorIdPaginado(
            Integer id,
            Pageable pageable) {

        return empleadoRepository
                .findByIdWithUsuario(id, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empleado> buscarPorCodigoEmpleado(
            String codigoEmpleado,
            Pageable pageable) {

        return empleadoRepository
                .findByCodigoEmpleadoContainingIgnoreCaseWithUsuario(
                        codigoEmpleado,
                        pageable
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empleado> buscarPorDepartamento(
            String departamento,
            Pageable pageable) {

        return empleadoRepository
                .findByDepartamentoContainingIgnoreCaseWithUsuario(
                        departamento,
                        pageable
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Empleado obtenerPorUsuario(Usuario usuario) {

        return empleadoRepository
                .findByUsuario(usuario)
                .orElse(null);
    }

    @Override
    public Empleado actualizar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado activar(Empleado empleado) {

        empleado.setEstadoLaboral(
                com.equipo1.netpulse.modelos.EstadoEmpleado.ACTIVO
        );

        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado desactivar(Empleado empleado) {

        empleado.setEstadoLaboral(
                com.equipo1.netpulse.modelos.EstadoEmpleado.INACTIVO
        );

        return empleadoRepository.save(empleado);
    }

    @Override
    public void eliminarPorId(Integer id) {
        empleadoRepository.deleteById(id);
    }
}