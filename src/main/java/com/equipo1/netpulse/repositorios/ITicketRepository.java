package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.CategoriaIncidencia;
import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.EstadoTicket;
import com.equipo1.netpulse.modelos.PrioridadTicket;
import com.equipo1.netpulse.modelos.Ticket;
import com.equipo1.netpulse.modelos.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ITicketRepository extends JpaRepository<Ticket, Integer> {

    @Query(
            value = """
            SELECT t
            FROM Ticket t
            JOIN FETCH t.equipo
            JOIN FETCH t.usuarioReporta
            LEFT JOIN FETCH t.tecnico
            JOIN FETCH t.categoria
            JOIN FETCH t.prioridad
            JOIN FETCH t.estadoTicket
            """,
            countQuery = """
            SELECT COUNT(t)
            FROM Ticket t
            """
    )
    Page<Ticket> findAllWithRelations(Pageable pageable);

    @Query("""
    SELECT t
    FROM Ticket t
    JOIN FETCH t.equipo
    JOIN FETCH t.usuarioReporta
    LEFT JOIN FETCH t.tecnico
    JOIN FETCH t.categoria
    JOIN FETCH t.prioridad
    JOIN FETCH t.estadoTicket
    """)
    List<Ticket> findAllWithRelations();

    @Query("""
    SELECT t
    FROM Ticket t
    JOIN FETCH t.equipo
    JOIN FETCH t.usuarioReporta
    LEFT JOIN FETCH t.tecnico
    JOIN FETCH t.categoria
    JOIN FETCH t.prioridad
    JOIN FETCH t.estadoTicket
    WHERE t.idTicket = :id
    """)
    Optional<Ticket> findByIdWithRelations(
            @Param("id") Integer id
    );

    @Query(
            value = """
            SELECT t
            FROM Ticket t
            JOIN FETCH t.equipo
            JOIN FETCH t.usuarioReporta
            LEFT JOIN FETCH t.tecnico
            JOIN FETCH t.categoria
            JOIN FETCH t.prioridad
            JOIN FETCH t.estadoTicket
            WHERE t.idTicket = :id
            """,
            countQuery = """
            SELECT COUNT(t)
            FROM Ticket t
            WHERE t.idTicket = :id
            """
    )
    Page<Ticket> findByIdWithRelations(
            @Param("id") Integer id,
            Pageable pageable
    );

    @Query("""
    SELECT t
    FROM Ticket t
    JOIN FETCH t.equipo
    JOIN FETCH t.usuarioReporta
    LEFT JOIN FETCH t.tecnico
    JOIN FETCH t.categoria
    JOIN FETCH t.prioridad
    JOIN FETCH t.estadoTicket
    WHERE t.usuarioReporta = :usuario
    """)
    List<Ticket> findByUsuarioReportaWithRelations(
            @Param("usuario") Usuario usuario
    );

    @Query("""
    SELECT t
    FROM Ticket t
    JOIN FETCH t.equipo
    JOIN FETCH t.usuarioReporta
    LEFT JOIN FETCH t.tecnico
    JOIN FETCH t.categoria
    JOIN FETCH t.prioridad
    JOIN FETCH t.estadoTicket
    WHERE t.tecnico = :usuario
    """)
    List<Ticket> findByTecnicoWithRelations(
            @Param("usuario") Usuario usuario
    );

    @Query("""
    SELECT t
    FROM Ticket t
    JOIN FETCH t.equipo
    JOIN FETCH t.usuarioReporta
    LEFT JOIN FETCH t.tecnico
    JOIN FETCH t.categoria
    JOIN FETCH t.prioridad
    JOIN FETCH t.estadoTicket
    WHERE t.equipo = :equipo
    """)
    List<Ticket> findByEquipoWithRelations(
            @Param("equipo") Equipo equipo
    );

    @Query("""
    SELECT t
    FROM Ticket t
    JOIN FETCH t.equipo
    JOIN FETCH t.usuarioReporta
    LEFT JOIN FETCH t.tecnico
    JOIN FETCH t.categoria
    JOIN FETCH t.prioridad
    JOIN FETCH t.estadoTicket
    WHERE t.categoria = :categoria
    """)
    List<Ticket> findByCategoriaWithRelations(
            @Param("categoria") CategoriaIncidencia categoria
    );

    @Query("""
    SELECT t
    FROM Ticket t
    JOIN FETCH t.equipo
    JOIN FETCH t.usuarioReporta
    LEFT JOIN FETCH t.tecnico
    JOIN FETCH t.categoria
    JOIN FETCH t.prioridad
    JOIN FETCH t.estadoTicket
    WHERE t.prioridad = :prioridad
    """)
    List<Ticket> findByPrioridadWithRelations(
            @Param("prioridad") PrioridadTicket prioridad
    );

    @Query("""
    SELECT t
    FROM Ticket t
    JOIN FETCH t.equipo
    JOIN FETCH t.usuarioReporta
    LEFT JOIN FETCH t.tecnico
    JOIN FETCH t.categoria
    JOIN FETCH t.prioridad
    JOIN FETCH t.estadoTicket
    WHERE t.estadoTicket = :estadoTicket
    """)
    List<Ticket> findByEstadoTicketWithRelations(
            @Param("estadoTicket") EstadoTicket estadoTicket
    );


    /*
     * ============================================================
     * REPORTES DE INCIDENCIAS
     * ============================================================
     */

    @Query("""
    SELECT t
    FROM Ticket t
    JOIN FETCH t.equipo
    JOIN FETCH t.usuarioReporta
    LEFT JOIN FETCH t.tecnico
    JOIN FETCH t.categoria
    JOIN FETCH t.prioridad
    JOIN FETCH t.estadoTicket
    WHERE (:fechaInicio IS NULL OR t.fechaCreacion >= :fechaInicio)
    AND (:fechaFin IS NULL OR t.fechaCreacion <= :fechaFin)
    AND (:estadoTicket IS NULL OR t.estadoTicket = :estadoTicket)
    AND (:categoria IS NULL OR t.categoria = :categoria)
    ORDER BY t.fechaCreacion DESC
    """)
    List<Ticket> buscarParaReporte(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("estadoTicket") EstadoTicket estadoTicket,
            @Param("categoria") CategoriaIncidencia categoria
    );

    /*
     * ============================================================
     * DASHBOARD DE KPIs
     * ============================================================
     */

    @Query("""
        SELECT COUNT(t)
        FROM Ticket t
        WHERE (:fechaInicio IS NULL OR t.fechaCreacion >= :fechaInicio)
        AND (:fechaFin IS NULL OR t.fechaCreacion <= :fechaFin)
        """)
    long contarTicketsParaDashboard(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );


    @Query("""
        SELECT t.estadoTicket.nombre, COUNT(t)
        FROM Ticket t
        WHERE (:fechaInicio IS NULL OR t.fechaCreacion >= :fechaInicio)
        AND (:fechaFin IS NULL OR t.fechaCreacion <= :fechaFin)
        GROUP BY t.estadoTicket.nombre
        ORDER BY COUNT(t) DESC
        """)
    List<Object[]> contarTicketsPorEstadoParaDashboard(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );


    @Query("""
        SELECT t.categoria.nombre, COUNT(t)
        FROM Ticket t
        WHERE (:fechaInicio IS NULL OR t.fechaCreacion >= :fechaInicio)
        AND (:fechaFin IS NULL OR t.fechaCreacion <= :fechaFin)
        GROUP BY t.categoria.nombre
        ORDER BY COUNT(t) DESC
        """)
    List<Object[]> contarTicketsPorCategoriaParaDashboard(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );


    @Query("""
        SELECT t.prioridad.nombre, COUNT(t)
        FROM Ticket t
        WHERE (:fechaInicio IS NULL OR t.fechaCreacion >= :fechaInicio)
        AND (:fechaFin IS NULL OR t.fechaCreacion <= :fechaFin)
        GROUP BY t.prioridad.nombre
        ORDER BY COUNT(t) DESC
        """)
    List<Object[]> contarTicketsPorPrioridadParaDashboard(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

}
