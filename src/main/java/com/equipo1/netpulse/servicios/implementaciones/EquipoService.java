package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Equipo;
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
    public Page<Equipo> obtenerTodosPaginados(Pageable pageable) {
        return equipoRepository.findAll(pageable);
    }

    @Override
    public List<Equipo> obtenerTodos() {
        return equipoRepository.findAll();
    }

    @Override
    public Equipo obtenerPorId(Integer id) {
        return equipoRepository.findById(id).get();
    }

    @Override
    public Equipo obtenerPorNumeroSerie(String numeroSerie) {
        return equipoRepository.findByNumeroSerie(numeroSerie).orElse(null);
    }

    @Override
    public Equipo crearOEditar(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    @Override
    public void eliminarPorId(Integer id) {
        equipoRepository.deleteById(id);
    }
}