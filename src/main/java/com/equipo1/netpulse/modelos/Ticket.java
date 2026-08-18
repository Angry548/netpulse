package com.equipo1.netpulse.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@EntityListeners(AuditingEntityListener.class)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Integer idTicket;

    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    @NotNull(message = "El equipo es requerido")
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "id_usuario_reporta", nullable = false)
    @NotNull(message = "El usuario que reporta es requerido")
    private Usuario usuarioReporta;

    @ManyToOne
    @JoinColumn(name = "id_tecnico")
    private Usuario tecnico;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    @NotNull(message = "La categoría es requerida")
    private CategoriaIncidencia categoria;

    @ManyToOne
    @JoinColumn(name = "id_prioridad", nullable = false)
    @NotNull(message = "La prioridad es requerida")
    private PrioridadTicket prioridad;

    @ManyToOne
    @JoinColumn(name = "id_estado_ticket", nullable = false)
    @NotNull(message = "El estado del ticket es requerido")
    private EstadoTicket estadoTicket;

    @NotBlank(message = "La descripción es requerida")
    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    public Integer getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Integer idTicket) {
        this.idTicket = idTicket;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Usuario getUsuarioReporta() {
        return usuarioReporta;
    }

    public void setUsuarioReporta(Usuario usuarioReporta) {
        this.usuarioReporta = usuarioReporta;
    }

    public Usuario getTecnico() {
        return tecnico;
    }

    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    }

    public CategoriaIncidencia getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaIncidencia categoria) {
        this.categoria = categoria;
    }

    public PrioridadTicket getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(PrioridadTicket prioridad) {
        this.prioridad = prioridad;
    }

    public EstadoTicket getEstadoTicket() {
        return estadoTicket;
    }

    public void setEstadoTicket(EstadoTicket estadoTicket) {
        this.estadoTicket = estadoTicket;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }
}
