package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.EstadoEquipo;
import com.equipo1.netpulse.modelos.TipoEquipo;
import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.repositorios.IEquipoRepository;
import com.equipo1.netpulse.servicios.interfaces.IEquipoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipoService implements IEquipoService {

    private final IEquipoRepository equipoRepository;

    public EquipoService(IEquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    @Override
    public Equipo registrar(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    @Override
    @Transactional(readOnly = true)
    public Equipo buscarPorId(Integer id) {
        return equipoRepository
                .findByIdWithRelations(id)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Equipo buscarPorNumeroSerie(String numeroSerie) {
        return equipoRepository
                .findByNumeroSerie(numeroSerie)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> obtenerTodos() {
        return equipoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Equipo> buscarTodosPaginados(
            Pageable pageable) {

        return equipoRepository.findAllWithRelations(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Equipo> buscarPorIdPaginado(
            Integer id,
            Pageable pageable) {

        return equipoRepository.findByIdWithRelations(
                id,
                pageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Equipo> buscarPorNumeroSerie(
            String numeroSerie,
            Pageable pageable) {

        return equipoRepository
                .findByNumeroSerieContainingIgnoreCaseWithRelations(
                        numeroSerie,
                        pageable
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Equipo> buscarPorNombre(
            String nombre,
            Pageable pageable) {

        return equipoRepository
                .findByNombreContainingIgnoreCaseWithRelations(
                        nombre,
                        pageable
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> obtenerPorTipo(
            TipoEquipo tipoEquipo) {

        return equipoRepository.findByTipo(tipoEquipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> obtenerPorEstado(
            EstadoEquipo estadoEquipo) {

        return equipoRepository.findByEstado(estadoEquipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> obtenerPorResponsable(
            Usuario usuario) {

        return equipoRepository.findByResponsable(usuario);
    }

    @Override
    public Equipo actualizar(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    @Override
    public Equipo asignarResponsable(
            Equipo equipo,
            Usuario usuario) {

        equipo.setResponsable(usuario);

        return equipoRepository.save(equipo);
    }

    @Override
    public Equipo cambiarEstado(
            Equipo equipo,
            EstadoEquipo estado) {

        equipo.setEstado(estado);

        return equipoRepository.save(equipo);
    }

    @Override
    public Equipo actualizarConexion(
            Equipo equipo) {

        return equipoRepository.save(equipo);
    }

    @Override
    public void eliminarPorId(Integer id) {
        equipoRepository.deleteById(id);
    }
}