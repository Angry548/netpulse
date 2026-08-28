package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.EstadoEquipo;
import com.equipo1.netpulse.modelos.TipoEquipo;
import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.repositorios.IEquipoRepository;
import com.equipo1.netpulse.servicios.interfaces.IEquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipoService implements IEquipoService {

    @Autowired
    private IEquipoRepository equipoRepository;

    @Override
    public Equipo registrar(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    @Override
    public Equipo buscarPorId(Integer id) {
        return equipoRepository.findById(id).get();
    }

    @Override
    public Equipo buscarPorNumeroSerie(String numeroSerie) {
        return equipoRepository.findByNumeroSerie(numeroSerie).get();
    }

    @Override
    public List<Equipo> obtenerTodos() {
        return equipoRepository.findAll();
    }

    @Override
    public Page<Equipo> buscarTodosPaginados(Pageable pageable) {
        return equipoRepository.findAll(pageable);
    }

    @Override
    public List<Equipo> obtenerPorTipo(TipoEquipo tipoEquipo) {
        return equipoRepository.findByTipo(tipoEquipo);
    }

    @Override
    public List<Equipo> obtenerPorEstado(EstadoEquipo estadoEquipo) {
        return equipoRepository.findByEstado(estadoEquipo);
    }

    @Override
    public List<Equipo> obtenerPorResponsable(Usuario usuario) {
        return equipoRepository.findByResponsable(usuario);
    }

    @Override
    public Equipo actualizar(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    @Override
    public Equipo asignarResponsable(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    @Override
    public Equipo cambiarEstado(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    @Override
    public Equipo actualizarConexion(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    @Override
    public void eliminarPorId(Integer id) {
        equipoRepository.deleteById(id);
    }
}