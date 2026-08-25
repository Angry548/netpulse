package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.repositorios.IEquipoRepository;
import com.equipo1.netpulse.servicios.interfaces.IEquipoService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return equipoRepository.findByNumeroSerie(numeroSerie).orElse(null);
    }

    @Override
    public List<Equipo> obtenerTodos() {
        return equipoRepository.findAll();
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