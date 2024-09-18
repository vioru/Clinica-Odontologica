package com.backend.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "turnos")

public class Turno {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  //@ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "paciente_id", nullable = false)
  private Paciente paciente;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  //@ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "odontologo_id", nullable = false)
  private Odontologo odontologo;
  private LocalDateTime fechaHora;

  public Turno(Long id, Paciente paciente, Odontologo odontologo, LocalDateTime fechaHora) {
    this.id = id;
    this.paciente = paciente;
    this.odontologo = odontologo;
    this.fechaHora = fechaHora;
  }

  public Turno(Paciente paciente, Odontologo odontologo, LocalDateTime fechaHora) {
    this.paciente = paciente;
    this.odontologo = odontologo;
    this.fechaHora = fechaHora;
  }

  public Turno() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Paciente getPaciente() {
    return paciente;
  }

  public void setPaciente(Paciente paciente) {
    this.paciente = paciente;
  }

  public Odontologo getOdontologo() {
    return odontologo;
  }

  public void setOdontologo(Odontologo odontologo) {
    this.odontologo = odontologo;
  }

  public LocalDateTime getFechaHora() {
    return fechaHora;
  }

  public void setFechaHora(LocalDateTime fechaHora) {
    this.fechaHora = fechaHora;
  }

  @Override
  public String toString() {
    return "Turno{" + "id=" + id + ", paciente=" + paciente + ", odontologo=" + odontologo + ", fechaHora=" + fechaHora + '}';
  }
}
