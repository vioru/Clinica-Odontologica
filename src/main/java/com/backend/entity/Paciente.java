package com.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pacientes")
public class Paciente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 30, nullable = false)
  private String nombre;

  @Column(length = 30, nullable = false)
  private String apellido;

  @Column(length = 30, nullable = false)
  private int dni;

  private LocalDate fechaIngreso;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "domicilio_id")
  private Domicilio domicilio;

  /*
  //@OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
  @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Turno> turnos = new ArrayList<>();
  */

  public Paciente() {
  }

  public Paciente(String nombre, String apellido, int dni, LocalDate fechaIngreso, Domicilio domicilio) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.dni = dni;
    this.fechaIngreso = fechaIngreso;
    this.domicilio = domicilio;
  }

  public Paciente(Long id, String nombre, String apellido, int dni, LocalDate fechaIngreso, Domicilio domicilio) {
    this.id = id;
    this.nombre = nombre;
    this.apellido = apellido;
    this.dni = dni;
    this.fechaIngreso = fechaIngreso;
    this.domicilio = domicilio;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public int getDni() {
    return dni;
  }

  public void setDni(int dni) {
    this.dni = dni;
  }

  public LocalDate getFechaIngreso() {
    return fechaIngreso;
  }

  public void setFechaIngreso(LocalDate fechaIngreso) {
    this.fechaIngreso = fechaIngreso;
  }

  public Domicilio getDomicilio() {
    return domicilio;
  }

  public void setDomicilio(Domicilio domicilio) {
    this.domicilio = domicilio;
  }

  /*public List<Turno> getTurnos() {
    return turnos;
  }

  public void setTurnos(List<Turno> turnos) {
    this.turnos = turnos;
  }*/

  @Override
  public String toString() {
    return "Paciente{" + "id=" + id + ", nombre='" + nombre + '\'' + ", apellido='" + apellido + '\'' + ", dni=" + dni + ", fechaIngreso=" + fechaIngreso + ", domicilio=" + domicilio + '}';
  }
}
