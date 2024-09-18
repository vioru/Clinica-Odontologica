package com.backend.dto.entrada;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class OdontologoEntradaDto {
  @NotBlank(message = "Debe ingresar la matrícula del Odontólogo")
  @Size(max = 20, message = "La matrícula puede poseer hasta 20 carácteres")
  private String matricula;

  @NotBlank(message = "Debe ingresar el nombre del Odontólogo")
  @Size(max = 20, message = "El nombre puede poseer hasta 20 carácteres")
  private String nombre;

  @NotBlank(message = "Debe ingresar la apellido del Odontólogo")
  @Size(max = 20, message = "El apellido puede poseer hasta 20 carácteres")
  private String apellido;

  public OdontologoEntradaDto(String matricula, String nombre, String apellido) {
    this.matricula = matricula;
    this.nombre = nombre;
    this.apellido = apellido;
  }

  public OdontologoEntradaDto() {
  }

  public String getMatricula() {
    return matricula;
  }

  public void setMatricula(String matricula) {
    this.matricula = matricula;
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

  @Override
  public String toString() {
    return "OdontologoEntradaDto{" + "matricula='" + matricula + '\'' + ", nombre='" + nombre + '\'' + ", apellido='" + apellido + '\'' + '}';
  }
}
