package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.EstadoEquipo;
import com.equipo1.netpulse.modelos.TipoEquipo;
import com.equipo1.netpulse.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IEquipoRepository extends JpaRepository<Equipo, Integer> {

    Optional<Equipo> findByNumeroSerie(String numeroSerie);

    List<Equipo> findByTipo(TipoEquipo tipoEquipo);

    List<Equipo> findByEstado(EstadoEquipo estadoEquipo);

    List<Equipo> findByResponsable(Usuario usuario);

    /*
     * ==========================================================
     * LISTADO PAGINADO
     * ==========================================================
     */

    @Query(
            value = """
                    SELECT e
                    FROM Equipo e
                    JOIN FETCH e.tipo
                    JOIN FETCH e.estado
                    LEFT JOIN FETCH e.responsable
                    """,
            countQuery = """
                    SELECT COUNT(e)
                    FROM Equipo e
                    """
    )
    Page<Equipo> findAllWithRelations(Pageable pageable);


    /*
     * ==========================================================
     * BUSCAR POR ID
     * ==========================================================
     */

    @Query(
            value = """
                    SELECT e
                    FROM Equipo e
                    JOIN FETCH e.tipo
                    JOIN FETCH e.estado
                    LEFT JOIN FETCH e.responsable
                    WHERE e.idEquipo = :id
                    """,
            countQuery = """
                    SELECT COUNT(e)
                    FROM Equipo e
                    WHERE e.idEquipo = :id
                    """
    )
    Page<Equipo> findByIdWithRelations(
            @Param("id") Integer id,
            Pageable pageable
    );


    @Query("""
            SELECT e
            FROM Equipo e
            JOIN FETCH e.tipo
            JOIN FETCH e.estado
            LEFT JOIN FETCH e.responsable
            WHERE e.idEquipo = :id
            """)
    Optional<Equipo> findByIdWithRelations(
            @Param("id") Integer id
    );


    /*
     * ==========================================================
     * BUSCAR POR NÚMERO DE SERIE
     * ==========================================================
     */

    @Query(
            value = """
                    SELECT e
                    FROM Equipo e
                    JOIN FETCH e.tipo
                    JOIN FETCH e.estado
                    LEFT JOIN FETCH e.responsable
                    WHERE LOWER(e.numeroSerie)
                    LIKE LOWER(CONCAT('%', :numeroSerie, '%'))
                    """,
            countQuery = """
                    SELECT COUNT(e)
                    FROM Equipo e
                    WHERE LOWER(e.numeroSerie)
                    LIKE LOWER(CONCAT('%', :numeroSerie, '%'))
                    """
    )
    Page<Equipo> findByNumeroSerieContainingIgnoreCaseWithRelations(
            @Param("numeroSerie") String numeroSerie,
            Pageable pageable
    );


    /*
     * ==========================================================
     * BUSCAR POR NOMBRE
     * ==========================================================
     */

    @Query(
            value = """
                    SELECT e
                    FROM Equipo e
                    JOIN FETCH e.tipo
                    JOIN FETCH e.estado
                    LEFT JOIN FETCH e.responsable
                    WHERE LOWER(e.nombre)
                    LIKE LOWER(CONCAT('%', :nombre, '%'))
                    """,
            countQuery = """
                    SELECT COUNT(e)
                    FROM Equipo e
                    WHERE LOWER(e.nombre)
                    LIKE LOWER(CONCAT('%', :nombre, '%'))
                    """
    )
    Page<Equipo> findByNombreContainingIgnoreCaseWithRelations(
            @Param("nombre") String nombre,
            Pageable pageable
    );
}