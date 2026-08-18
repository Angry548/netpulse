package com.equipo1.netpulse.repositorios;

import com.equipo1.netpulse.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
}