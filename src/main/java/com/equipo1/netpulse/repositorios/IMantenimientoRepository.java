package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Mantenimiento;
import com.equipo1.netpulse.modelos.Equipo;
import com.equipo1.netpulse.modelos.Usuario;
import com.equipo1.netpulse.modelos.Ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IMantenimientoRepository
        extends JpaRepository<Mantenimiento, Integer> {

    List<Mantenimiento> findByEquipo(Equipo equipo);

    List<Mantenimiento> findByTecnico(Usuario tecnico);

    List<Mantenimiento> findByTicket(Ticket ticket);

    @Query(
            value = """
                    SELECT m
                    FROM Mantenimiento m
                    JOIN FETCH m.equipo
                    JOIN FETCH m.tecnico
                    LEFT JOIN FETCH m.ticket
                    """,
            countQuery = """
                    SELECT COUNT(m)
                    FROM Mantenimiento m
                    """
    )
    Page<Mantenimiento> findAllWithRelaciones(Pageable pageable);

    @Query(
            value = """
                    SELECT m
                    FROM Mantenimiento m
                    JOIN FETCH m.equipo
                    JOIN FETCH m.tecnico
                    LEFT JOIN FETCH m.ticket
                    WHERE m.id = :id
                    """,
            countQuery = """
                    SELECT COUNT(m)
                    FROM Mantenimiento m
                    WHERE m.id = :id
                    """
    )
    Page<Mantenimiento> findByIdPaginado(
            @Param("id") Integer id,
            Pageable pageable
    );

    @Query("""
            SELECT m
            FROM Mantenimiento m
            JOIN FETCH m.equipo
            JOIN FETCH m.tecnico
            LEFT JOIN FETCH m.ticket
            WHERE m.id = :id
            """)
    Optional<Mantenimiento> findByIdWithRelaciones(
            @Param("id") Integer id
    );
}