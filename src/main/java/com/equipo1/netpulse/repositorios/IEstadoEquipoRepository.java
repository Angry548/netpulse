package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.EstadoEquipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IEstadoEquipoRepository
        extends JpaRepository<EstadoEquipo, Integer> {

    Optional<EstadoEquipo> findByNombre(String nombre);

    Page<EstadoEquipo> findByNombreContainingIgnoreCase(
            String nombre,
            Pageable pageable
    );

    @Query(
            value = """
                    SELECT e
                    FROM EstadoEquipo e
                    WHERE e.id = :id
                    """,
            countQuery = """
                    SELECT COUNT(e)
                    FROM EstadoEquipo e
                    WHERE e.id = :id
                    """
    )
    Page<EstadoEquipo> findByIdPaginado(
            @Param("id") Integer id,
            Pageable pageable
    );
}