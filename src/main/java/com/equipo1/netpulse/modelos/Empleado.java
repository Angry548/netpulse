package com.equipo1.netpulse.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @NotBlank(message = "El código del empleado es requerido")
    @Size(max = 30, message = "El código del empleado no puede superar los 30 caracteres")
    @Column(name = "codigo_empleado", unique = true, nullable = false, length = 30)
    private String codigoEmpleado;

    @NotBlank(message = "El departamento es requerido")
    @Size(max = 100, message = "El departamento no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String departamento;

    @NotBlank(message = "El cargo es requerido")
    @Size(max = 100, message = "El cargo no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String cargo;

    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    @Column(length = 20)
    private String telefono;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_laboral", nullable = false, length = 20)
    private EstadoEmpleado estadoLaboral;

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public EstadoEmpleado getEstadoLaboral() {
        return estadoLaboral;
    }

    public void setEstadoLaboral(EstadoEmpleado estadoLaboral) {
        this.estadoLaboral = estadoLaboral;
    }
}
