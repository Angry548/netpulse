package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Rol;
import com.equipo1.netpulse.repositorios.IRolRepository;
import com.equipo1.netpulse.servicios.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService implements IRolService {

    @Autowired
    private IRolRepository rolRepository;

    @Override
    public Rol crear(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public Rol buscarPorId(Integer id) {
        return rolRepository.findById(id).get();
    }

    @Override
    public Rol buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre).orElse(null);
    }

    @Override
    public List<Rol> obtenerTodos() {
        return rolRepository.findAll();
    }

    @Override
    public Rol actualizar(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public void eliminarPorId(Integer id) {
        rolRepository.deleteById(id);
    }

    @Override
    public void asignarUsuario(Rol rol) {
        rolRepository.save(rol);
    }
}