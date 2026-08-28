package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Mantenimiento;
import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.modelos.Ticket;

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
    public Page<Mantenimiento> buscarTodosPaginados(Pageable pageable) {
        return mantenimientoRepository.findAll(pageable);
    }

    @Override
    public List<Mantenimiento> obtenerPorEquipo(Equipo equipo) {
        return mantenimientoRepository.findByEquipo(equipo);
    }

    @Override
    public List<Mantenimiento> obtenerPorUsuario(Usuario usuario) {
        return mantenimientoRepository.findByTecnico(usuario);
    }

    @Override
    public List<Mantenimiento> obtenerPorTicket(Ticket ticket) {
        return mantenimientoRepository.findByTicket(ticket);
    }

    @Override
    public Mantenimiento actualizar(Mantenimiento mantenimiento) {
        return mantenimientoRepository.save(mantenimiento);
    }

    @Override
    public void eliminarPorId(Integer id) {
        mantenimientoRepository.deleteById(id);
    }
}