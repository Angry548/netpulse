package com.equipo1.netpulse.servicios.implementaciones;

import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.EstadoEquipo;
import com.equipo1.netpulse.modelos.HistorialEstado;
import com.equipo1.netpulse.modelos.TipoEquipo;
import com.equipo1.netpulse.modelos.Usuario;

import com.equipo1.netpulse.repositorios.IEquipoRepository;

import com.equipo1.netpulse.servicios.interfaces.IEquipoService;
import com.equipo1.netpulse.servicios.interfaces.IHistorialEstadoService;
import com.equipo1.netpulse.servicios.interfaces.IUsuarioService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EquipoService implements IEquipoService {

    private final IEquipoRepository equipoRepository;
    private final IHistorialEstadoService historialEstadoService;
    private final IUsuarioService usuarioService;

    public EquipoService(
            IEquipoRepository equipoRepository,
            IHistorialEstadoService historialEstadoService,
            IUsuarioService usuarioService) {

        this.equipoRepository = equipoRepository;
        this.historialEstadoService = historialEstadoService;
        this.usuarioService = usuarioService;
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
    public Equipo buscarPorNumeroSerie(
            String numeroSerie) {

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

        return equipoRepository.findAllWithRelations(
                pageable
        );
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
    @Transactional
    public Equipo actualizar(Equipo equipo) {

        if (equipo == null
                || equipo.getIdEquipo() == null) {

            throw new IllegalArgumentException(
                    "El equipo que se desea actualizar no es válido."
            );
        }

        Equipo equipoExistente =
                equipoRepository
                        .findById(equipo.getIdEquipo())
                        .orElse(null);

        if (equipoExistente == null) {

            throw new IllegalArgumentException(
                    "El equipo que se desea actualizar no existe."
            );
        }

        EstadoEquipo estadoAnterior =
                equipoExistente.getEstado();

        EstadoEquipo estadoNuevo =
                equipo.getEstado();

        boolean cambioEstado = false;

        if (estadoAnterior == null
                && estadoNuevo != null) {

            cambioEstado = true;

        } else if (estadoAnterior != null
                && estadoNuevo == null) {

            cambioEstado = true;

        } else if (estadoAnterior != null
                && estadoNuevo != null) {

            String nombreAnterior =
                    estadoAnterior.getNombre();

            String nombreNuevo =
                    estadoNuevo.getNombre();

            cambioEstado =
                    nombreAnterior == null
                            ? nombreNuevo != null
                            : !nombreAnterior.equalsIgnoreCase(
                            nombreNuevo
                    );
        }

        if (cambioEstado) {

            /*
             * Obtener el usuario actualmente autenticado.
             */
            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            if (authentication == null
                    || !authentication.isAuthenticated()
                    || authentication.getName() == null
                    || authentication.getName()
                    .equalsIgnoreCase("anonymousUser")) {

                throw new IllegalStateException(
                        "No se pudo identificar al usuario autenticado."
                );
            }

            String correo =
                    authentication.getName();

            Usuario usuario =
                    usuarioService.buscarPorCorreo(
                            correo
                    );

            if (usuario == null) {

                throw new IllegalStateException(
                        "No se pudo encontrar el usuario que realizó el cambio."
                );
            }

            HistorialEstado historial =
                    new HistorialEstado();

            historial.setEquipo(
                    equipoExistente
            );

            historial.setEstadoAnterior(
                    estadoAnterior
            );

            historial.setEstadoNuevo(
                    estadoNuevo
            );

            historial.setUsuario(
                    usuario
            );

            historial.setFechaCambio(
                    LocalDateTime.now()
            );

            historial.setMotivo(
                    "Cambio de estado del equipo"
            );

            historialEstadoService.registrarCambio(
                    historial
            );
        }


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
    @Transactional
    public Equipo cambiarEstado(
            Equipo equipo,
            EstadoEquipo estado) {

        if (equipo == null
                || equipo.getIdEquipo() == null) {

            throw new IllegalArgumentException(
                    "El equipo que se desea modificar no es válido."
            );
        }

        equipo.setEstado(estado);

        return actualizar(equipo);
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