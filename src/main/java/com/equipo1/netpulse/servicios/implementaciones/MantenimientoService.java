package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Mantenimiento;
import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.modelos.Ticket;

import com.equipo1.netpulse.repositorios.IMantenimientoRepository;
import com.equipo1.netpulse.servicios.interfaces.IMantenimientoService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MantenimientoService
        implements IMantenimientoService {

    private final IMantenimientoRepository mantenimientoRepository;

    public MantenimientoService(
            IMantenimientoRepository mantenimientoRepository) {

        this.mantenimientoRepository =
                mantenimientoRepository;
    }

    @Override
    public Mantenimiento registrar(
            Mantenimiento mantenimiento) {

        return mantenimientoRepository.save(
                mantenimiento
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Mantenimiento buscarPorId(
            Integer id) {

        return mantenimientoRepository
                .findByIdWithRelaciones(id)
                .orElse(null);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Mantenimiento> buscarPorIdPaginado(
            Integer id,
            Pageable pageable) {

        return mantenimientoRepository
                .findByIdWithRelaciones(
                        id,
                        pageable
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Mantenimiento> buscarTodosPaginados(
            Pageable pageable) {

        return mantenimientoRepository
                .findAllWithRelaciones(
                        pageable
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mantenimiento> obtenerTodos() {

        return mantenimientoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mantenimiento> obtenerPorEquipo(
            Equipo equipo) {

        return mantenimientoRepository
                .findByEquipo(equipo);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Mantenimiento> obtenerPorTecnico(
            Usuario tecnico) {

        return mantenimientoRepository
                .findByTecnico(tecnico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mantenimiento> obtenerPorTicket(
            Ticket ticket) {

        return mantenimientoRepository
                .findByTicket(ticket);
    }


    @Override
    public Mantenimiento actualizar(
            Mantenimiento mantenimiento) {

        return mantenimientoRepository.save(
                mantenimiento
        );
    }

    @Override
    public void eliminarPorId(
            Integer id) {

        mantenimientoRepository.deleteById(
                id
        );
    }
}