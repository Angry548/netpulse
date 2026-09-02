package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.EstadoEquipo;
import com.equipo1.netpulse.repositorios.IEstadoEquipoRepository;
import com.equipo1.netpulse.servicios.interfaces.IEstadoEquipoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstadoEquipoService implements IEstadoEquipoService {

    private final IEstadoEquipoRepository estadoEquipoRepository;

    public EstadoEquipoService(
            IEstadoEquipoRepository estadoEquipoRepository) {

        this.estadoEquipoRepository = estadoEquipoRepository;
    }

    @Override
    public EstadoEquipo crear(EstadoEquipo estadoEquipo) {

        return estadoEquipoRepository.save(
                estadoEquipo
        );
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoEquipo buscarPorId(Integer id) {

        return estadoEquipoRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoEquipo buscarPorNombre(String nombre) {

        return estadoEquipoRepository
                .findByNombre(nombre)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoEquipo> obtenerTodos() {

        return estadoEquipoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstadoEquipo> buscarTodosPaginados(
            Pageable pageable) {

        return estadoEquipoRepository.findAll(
                pageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstadoEquipo> buscarPorIdPaginado(
            Integer id,
            Pageable pageable) {

        return estadoEquipoRepository.findByIdPaginado(
                id,
                pageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstadoEquipo> buscarPorNombre(
            String nombre,
            Pageable pageable) {

        return estadoEquipoRepository
                .findByNombreContainingIgnoreCase(
                        nombre,
                        pageable
                );
    }

    @Override
    public EstadoEquipo actualizar(
            EstadoEquipo estadoEquipo) {

        return estadoEquipoRepository.save(
                estadoEquipo
        );
    }

    @Override
    public void eliminarPorId(Integer id) {

        estadoEquipoRepository.deleteById(id);
    }
}