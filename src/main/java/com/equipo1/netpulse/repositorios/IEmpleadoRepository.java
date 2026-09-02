package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Empleado;
import com.equipo1.netpulse.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IEmpleadoRepository extends JpaRepository<Empleado, Integer> {

    Optional<Empleado> findByCodigoEmpleado(String codigoEmpleado);

    List<Empleado> findAll();

    Optional<Empleado> findByUsuario(Usuario usuario);

    /*
     * ==========================================================
     * LISTADO GENERAL
     * ==========================================================
     *
     * Traemos también el usuario para poder mostrar sus datos
     * directamente en las vistas sin problemas de Lazy Loading.
     */
    @Query(
            value = """
                    SELECT e
                    FROM Empleado e
                    JOIN FETCH e.usuario
                    """,
            countQuery = """
                    SELECT COUNT(e)
                    FROM Empleado e
                    """
    )
    Page<Empleado> findAllWithUsuario(Pageable pageable);

    /*
     * ==========================================================
     * BÚSQUEDA POR ID
     * ==========================================================
     */
    @Query(
            value = """
                    SELECT e
                    FROM Empleado e
                    JOIN FETCH e.usuario
                    WHERE e.idEmpleado = :id
                    """,
            countQuery = """
                    SELECT COUNT(e)
                    FROM Empleado e
                    WHERE e.idEmpleado = :id
                    """
    )
    Page<Empleado> findByIdWithUsuario(
            @Param("id") Integer id,
            Pageable pageable
    );

    /*
     * ==========================================================
     * BÚSQUEDA POR ID PARA DETALLES / EDITAR / ELIMINAR
     * ==========================================================
     */
    @Query("""
            SELECT e
            FROM Empleado e
            JOIN FETCH e.usuario
            WHERE e.idEmpleado = :id
            """)
    Optional<Empleado> findByIdWithUsuario(
            @Param("id") Integer id
    );

    /*
     * ==========================================================
     * BÚSQUEDA POR CÓDIGO
     * ==========================================================
     */
    @Query(
            value = """
                    SELECT e
                    FROM Empleado e
                    JOIN FETCH e.usuario
                    WHERE LOWER(e.codigoEmpleado)
                    LIKE LOWER(CONCAT('%', :codigoEmpleado, '%'))
                    """,
            countQuery = """
                    SELECT COUNT(e)
                    FROM Empleado e
                    WHERE LOWER(e.codigoEmpleado)
                    LIKE LOWER(CONCAT('%', :codigoEmpleado, '%'))
                    """
    )
    Page<Empleado> findByCodigoEmpleadoContainingIgnoreCaseWithUsuario(
            @Param("codigoEmpleado") String codigoEmpleado,
            Pageable pageable
    );

    /*
     * ==========================================================
     * BÚSQUEDA POR DEPARTAMENTO
     * ==========================================================
     */
    @Query(
            value = """
                    SELECT e
                    FROM Empleado e
                    JOIN FETCH e.usuario
                    WHERE LOWER(e.departamento)
                    LIKE LOWER(CONCAT('%', :departamento, '%'))
                    """,
            countQuery = """
                    SELECT COUNT(e)
                    FROM Empleado e
                    WHERE LOWER(e.departamento)
                    LIKE LOWER(CONCAT('%', :departamento, '%'))
                    """
    )
    Page<Empleado> findByDepartamentoContainingIgnoreCaseWithUsuario(
            @Param("departamento") String departamento,
            Pageable pageable
    );
}
