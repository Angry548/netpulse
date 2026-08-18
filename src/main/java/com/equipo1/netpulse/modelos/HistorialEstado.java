package com.equipo1.netpulse.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estados")
public class HistorialEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "id_estado_anterior", nullable = false)
    private EstadoEquipo estadoAnterior;

    @ManyToOne
    @JoinColumn(name = "id_estado_nuevo", nullable = false)
    private EstadoEquipo estadoNuevo;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_cambio")
    private LocalDateTime fechaCambio;

    @Size(max = 255, message = "El motivo no puede superar los 255 caracteres")
    @Column(name = "motivo", length = 255)
    private String motivo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public EstadoEquipo getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(EstadoEquipo estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public EstadoEquipo getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(EstadoEquipo estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}