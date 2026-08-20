package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import com.equipo1.netpulse.repositorios.ICategoriaIncidenciaRepository;
import com.equipo1.netpulse.servicios.interfaces.ICategoriaIncidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaIncidenciaService implements ICategoriaIncidenciaService {

    @Autowired
    private ICategoriaIncidenciaRepository categoriaIncidenciaRepository;

    @Override
    public Page<CategoriaIncidencia> obtenerTodosPaginados(Pageable pageable) {
        return categoriaIncidenciaRepository.findAll(pageable);
    }

    @Override
    public List<CategoriaIncidencia> obtenerTodos() {
        return categoriaIncidenciaRepository.findAll();
    }

    @Override
    public CategoriaIncidencia obtenerPorId(Integer id) {
        return categoriaIncidenciaRepository.findById(id).get();
    }

    @Override
    public CategoriaIncidencia obtenerPorNombre(String nombre) {
        return categoriaIncidenciaRepository.findByNombre(nombre).orElse(null);
    }

    @Override
    public CategoriaIncidencia crearOEditar(CategoriaIncidencia categoriaIncidencia) {
        return categoriaIncidenciaRepository.save(categoriaIncidencia);
    }

    @Override
    public void eliminarPorId(Integer id) {
        categoriaIncidenciaRepository.deleteById(id);
    }
}
