package com.backend.dto.entrada;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class TurnoEntradaDto {
  @Positive(message = "El Id de Paciente no puede ser nulo o menor a cero")
  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Long idPaciente;

  @Positive(message = "El Id de Odontólogo no puede ser nulo o menor a cero")
  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Long idOdontologo;

  @FutureOrPresent(message = "La fecha no puede ser anterior al día de hoy")
  @NotNull(message = "Debe especificarse la fecha de atención al paciente")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime fechaHora;

  public TurnoEntradaDto(Long idPaciente, Long idOdontologo, LocalDateTime fechaHora) {
    this.idPaciente = idPaciente;
    this.idOdontologo = idOdontologo;
    this.fechaHora = fechaHora;
  }

  public TurnoEntradaDto(Long idPaciente, Long idOdontologo) {
    this.idPaciente = idPaciente;
    this.idOdontologo = idOdontologo;
    this.fechaHora=LocalDateTime.now().plusDays(1);
  }

  public TurnoEntradaDto() {
  }

  public Long getPacienteId() {
    return idPaciente;
  }

  public void setPacienteId(Long idPaciente) {
    this.idPaciente = idPaciente;
  }

  public Long getOdontologoId() {
    return idOdontologo;
  }

  public void setOdontologoId(Long idOdontologo) {
    this.idOdontologo = idOdontologo;
  }

  public LocalDateTime getFechaHora() {
    return fechaHora;
  }

  public void setFechaHora(LocalDateTime fechaHora) {
    this.fechaHora = fechaHora;
  }
}
