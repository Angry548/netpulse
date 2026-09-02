package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.AlertaRed;
import com.equipo1.netpulse.modelos.Equipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAlertaRedRepository
        extends JpaRepository<AlertaRed, Integer> {

    List<AlertaRed> findByEquipo(Equipo equipo);

    List<AlertaRed> findByNotificacionEnviadaFalse();

    List<AlertaRed> findByTipoEvento(String tipoEvento);

    @Query(
            value = """
                    SELECT a
                    FROM AlertaRed a
                    JOIN FETCH a.equipo
                    """,
            countQuery = """
                    SELECT COUNT(a)
                    FROM AlertaRed a
                    """
    )
    Page<AlertaRed> findAllWithEquipo(Pageable pageable);

    @Query(
            value = """
                    SELECT a
                    FROM AlertaRed a
                    JOIN FETCH a.equipo
                    WHERE a.id = :id
                    """,
            countQuery = """
                    SELECT COUNT(a)
                    FROM AlertaRed a
                    WHERE a.id = :id
                    """
    )
    Page<AlertaRed> findByIdPaginado(
            @Param("id") Integer id,
            Pageable pageable
    );

    @Query(
            value = """
                    SELECT a
                    FROM AlertaRed a
                    JOIN FETCH a.equipo
                    WHERE LOWER(a.tipoEvento)
                    LIKE LOWER(CONCAT('%', :tipoEvento, '%'))
                    """,
            countQuery = """
                    SELECT COUNT(a)
                    FROM AlertaRed a
                    WHERE LOWER(a.tipoEvento)
                    LIKE LOWER(CONCAT('%', :tipoEvento, '%'))
                    """
    )
    Page<AlertaRed> findByTipoEventoContainingIgnoreCase(
            @Param("tipoEvento") String tipoEvento,
            Pageable pageable
    );


    @Query("""
            SELECT a
            FROM AlertaRed a
            JOIN FETCH a.equipo
            WHERE a.id = :id
            """)
    java.util.Optional<AlertaRed> findByIdWithEquipo(
            @Param("id") Integer id
    );
}