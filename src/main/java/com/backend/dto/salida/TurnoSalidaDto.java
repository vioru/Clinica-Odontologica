package com.backend.dto.salida;

import java.time.LocalDateTime;

public class TurnoSalidaDto {

  Long id;
  PacienteSalidaDto pacienteSalidaDto;
  OdontologoSalidaDto odontologoSalidaDto;
  LocalDateTime fechaHora;

  public TurnoSalidaDto(Long id, PacienteSalidaDto pacienteSalidaDto, OdontologoSalidaDto odontologoSalidaDto,
                        LocalDateTime fechaHora) {
    this.id = id;
    this.pacienteSalidaDto = pacienteSalidaDto;
    this.odontologoSalidaDto = odontologoSalidaDto;
    this.fechaHora = fechaHora;
  }

  public TurnoSalidaDto() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PacienteSalidaDto getPacienteSalidaDto() {
    return pacienteSalidaDto;
  }

  public void setPacienteSalidaDto(PacienteSalidaDto pacienteSalidaDto) {
    this.pacienteSalidaDto = pacienteSalidaDto;
  }

  public OdontologoSalidaDto getOdontologoSalidaDto() {
    return odontologoSalidaDto;
  }

  public void setOdontologoSalidaDto(OdontologoSalidaDto odontologoSalidaDto) {
    this.odontologoSalidaDto = odontologoSalidaDto;
  }

  public LocalDateTime getFechaHora() {
    return fechaHora;
  }

  public void setFechaHora(LocalDateTime fechaHora) {
    this.fechaHora = fechaHora;
  }
}
