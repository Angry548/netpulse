package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Rol;
import com.equipo1.netpulse.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreo(String correo);

    List<Usuario> findByRol(Rol rol);

    /*
     * Obtiene todos los usuarios junto con su rol.
     * Se utiliza para la tabla principal de usuarios.
     */
    @Query(
            value = """
                    SELECT u
                    FROM Usuario u
                    JOIN FETCH u.rol
                    """,
            countQuery = """
                    SELECT COUNT(u)
                    FROM Usuario u
                    """
    )
    Page<Usuario> findAllWithRol(Pageable pageable);

    /*
     * Busca usuarios por nombre junto con su rol.
     */
    @Query(
            value = """
                    SELECT u
                    FROM Usuario u
                    JOIN FETCH u.rol
                    WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
                    """,
            countQuery = """
                    SELECT COUNT(u)
                    FROM Usuario u
                    WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
                    """
    )
    Page<Usuario> findByNombreContainingIgnoreCaseWithRol(
            @Param("nombre") String nombre,
            Pageable pageable
    );

    /*
     * Busca usuarios por correo junto con su rol.
     */
    @Query(
            value = """
                    SELECT u
                    FROM Usuario u
                    JOIN FETCH u.rol
                    WHERE LOWER(u.correo) LIKE LOWER(CONCAT('%', :correo, '%'))
                    """,
            countQuery = """
                    SELECT COUNT(u)
                    FROM Usuario u
                    WHERE LOWER(u.correo) LIKE LOWER(CONCAT('%', :correo, '%'))
                    """
    )
    Page<Usuario> findByCorreoContainingIgnoreCaseWithRol(
            @Param("correo") String correo,
            Pageable pageable
    );

    /*
     * Busca un usuario específico por ID junto con su rol.
     * Este método se utiliza en detalles, editar y eliminar.
     */
    @Query("""
            SELECT u
            FROM Usuario u
            JOIN FETCH u.rol
            WHERE u.id = :id
            """)
    Optional<Usuario> findByIdWithRol(
            @Param("id") Integer id
    );

    /*
     * Busca un usuario por ID para la paginación
     * de la búsqueda principal.
     */
    @Query(
            value = """
                    SELECT u
                    FROM Usuario u
                    JOIN FETCH u.rol
                    WHERE u.id = :id
                    """,
            countQuery = """
                    SELECT COUNT(u)
                    FROM Usuario u
                    WHERE u.id = :id
                    """
    )
    Page<Usuario> findByIdWithRol(
            @Param("id") Integer id,
            Pageable pageable
    );
}