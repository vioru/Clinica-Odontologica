package com.backend.service;

import com.backend.dto.entrada.PacienteEntradaDto;
import com.backend.dto.salida.PacienteSalidaDto;
import com.backend.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPacienteService {

  PacienteSalidaDto registrarPaciente(PacienteEntradaDto paciente);

  PacienteSalidaDto buscarPacientePorId(Long id);

  List<PacienteSalidaDto> listarPacientes();

  void eliminarPaciente(Long id) throws ResourceNotFoundException;

  PacienteSalidaDto actualizarPaciente(PacienteEntradaDto pacienteEntradaDto, Long id) throws ResourceNotFoundException;
}
