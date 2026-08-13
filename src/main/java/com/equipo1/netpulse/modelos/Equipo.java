package com.equipo1.netpulse.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo")
    private Integer idEquipo;

    @ManyToOne
    @JoinColumn(name = "id_tipo", nullable = false)
    private TipoEquipo tipo;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoEquipo estado;

    @ManyToOne
    @JoinColumn(name = "id_responsable")
    private Usuario responsable;

    @NotBlank(message = "El nombre del equipo es requerido")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La marca es requerida")
    @Size(max = 50, message = "La marca no puede superar los 50 caracteres")
    @Column(nullable = false, length = 50)
    private String marca;

    @NotBlank(message = "El modelo es requerido")
    @Size(max = 50, message = "El modelo no puede superar los 50 caracteres")
    @Column(nullable = false, length = 50)
    private String modelo;

    @NotBlank(message = "El número de serie es requerido")
    @Size(max = 100, message = "El número de serie no puede superar los 100 caracteres")
    @Column(name = "numero_serie", unique = true, nullable = false, length = 100)
    private String numeroSerie;

    @NotBlank(message = "La ubicación es requerida")
    @Size(max = 100, message = "La ubicación no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String ubicacion;

    @Size(max = 45, message = "La dirección IP no puede superar los 45 caracteres")
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Size(max = 17, message = "La dirección MAC no puede superar los 17 caracteres")
    @Column(name = "mac_address", length = 17)
    private String macAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_conexion", nullable = false, length = 20)
    private EstadoConexion estadoConexion;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public TipoEquipo getTipo() {
        return tipo;
    }

    public void setTipo(TipoEquipo tipo) {
        this.tipo = tipo;
    }

    public EstadoEquipo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEquipo estado) {
        this.estado = estado;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public EstadoConexion getEstadoConexion() {
        return estadoConexion;
    }

    public void setEstadoConexion(EstadoConexion estadoConexion) {
        this.estadoConexion = estadoConexion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}