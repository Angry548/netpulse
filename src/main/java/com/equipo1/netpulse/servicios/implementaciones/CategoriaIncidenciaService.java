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
    public CategoriaIncidencia crear(CategoriaIncidencia categoria) {
        return categoriaIncidenciaRepository.save(categoria);
    }

    @Override
    public CategoriaIncidencia buscarPorId(Integer id) {
        return categoriaIncidenciaRepository.findById(id).get();
    }

    @Override
    public CategoriaIncidencia buscarPorNombre(String nombre) {
        return categoriaIncidenciaRepository.findByNombre(nombre).orElse(null);
    }

    @Override
    public List<CategoriaIncidencia> obtenerTodos() {
        return categoriaIncidenciaRepository.findAll();
    }

    @Override
    public Page<CategoriaIncidencia> buscarTodosPaginados(Pageable pageable) {
        return categoriaIncidenciaRepository.findAll(pageable);
    }

    @Override
    public CategoriaIncidencia actualizar(CategoriaIncidencia categoria) {
        return categoriaIncidenciaRepository.save(categoria);
    }

    @Override
    public CategoriaIncidencia activar(CategoriaIncidencia categoria) {
        categoria.setActivo(true);
        return categoriaIncidenciaRepository.save(categoria);
    }

    @Override
    public CategoriaIncidencia desactivar(CategoriaIncidencia categoria) {
        categoria.setActivo(false);
        return categoriaIncidenciaRepository.save(categoria);
    }

    @Override
    public void eliminarPorId(Integer id) {
        categoriaIncidenciaRepository.deleteById(id);
    }
}
