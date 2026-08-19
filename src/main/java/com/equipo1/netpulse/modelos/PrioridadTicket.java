package com.equipo1.netpulse.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "prioridades_ticket")
public class PrioridadTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prioridad")
    private Integer idPrioridad;

    @NotBlank(message = "El nombre de la prioridad es requerido")
    @Size(max = 20, message = "El nombre de la prioridad no puede superar los 20 caracteres")
    @Column(name = "nombre", nullable = false, unique = true, length = 20)
    private String nombre;

    public Integer getIdPrioridad() {
        return idPrioridad;
    }

    public void setIdPrioridad(Integer idPrioridad) {
        this.idPrioridad = idPrioridad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}