package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.TipoEquipo;
import com.equipo1.netpulse.repositorios.ITipoEquipoRepository;
import com.equipo1.netpulse.servicios.interfaces.ITipoEquipoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoEquipoService implements ITipoEquipoService {

    private final ITipoEquipoRepository tipoEquipoRepository;

    public TipoEquipoService(
            ITipoEquipoRepository tipoEquipoRepository) {

        this.tipoEquipoRepository = tipoEquipoRepository;
    }

    @Override
    public TipoEquipo crear(TipoEquipo tipoEquipo) {

        return tipoEquipoRepository.save(tipoEquipo);
    }

    @Override
    @Transactional(readOnly = true)
    public TipoEquipo buscarPorId(Integer id) {

        return tipoEquipoRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public TipoEquipo buscarPorNombre(String nombre) {

        return tipoEquipoRepository
                .findByNombre(nombre)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoEquipo> obtenerTodos() {

        return tipoEquipoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoEquipo> buscarTodosPaginados(
            Pageable pageable) {

        return tipoEquipoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoEquipo> buscarPorIdPaginado(
            Integer id,
            Pageable pageable) {

        return tipoEquipoRepository.findById(
                id,
                pageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoEquipo> buscarPorNombre(
            String nombre,
            Pageable pageable) {

        return tipoEquipoRepository
                .findByNombreContainingIgnoreCase(
                        nombre,
                        pageable
                );
    }

    @Override
    public TipoEquipo actualizar(TipoEquipo tipoEquipo) {

        return tipoEquipoRepository.save(tipoEquipo);
    }

    @Override
    public void eliminarPorId(Integer id) {

        tipoEquipoRepository.deleteById(id);
    }
}