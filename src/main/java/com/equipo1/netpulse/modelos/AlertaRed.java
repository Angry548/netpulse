package com.equipo1.netpulse.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;


@Entity
@Table(name = "alertas_red")
public class AlertaRed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    @NotBlank(message = "El tipo de evento es requerido")
    @Size(max = 50, message = "El tipo de evento no puede superar los 50 caracteres")
    @Column(name = "tipo_evento", nullable = false, length = 50)
    private String tipoEvento;

    @NotBlank(message = "El mensaje es requerido")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "notificacion_enviada", nullable = false)
    private Boolean notificacionEnviada = false;

    @Size(max = 20, message = "El medio de notificación no puede superar los 20 caracteres")
    @Column(name = "medio_notificacion", length = 20)
    private String medioNotificacion;

    @Column(name = "fecha")
    private LocalDateTime fecha;

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

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getNotificacionEnviada() {
        return notificacionEnviada;
    }

    public void setNotificacionEnviada(Boolean notificacionEnviada) {
        this.notificacionEnviada = notificacionEnviada;
    }

    public String getMedioNotificacion() {
        return medioNotificacion;
    }

    public void setMedioNotificacion(String medioNotificacion) {
        this.medioNotificacion = medioNotificacion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
