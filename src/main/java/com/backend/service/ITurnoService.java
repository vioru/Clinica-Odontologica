package com.backend.service;

import com.backend.dto.entrada.TurnoEntradaDto;
import com.backend.dto.salida.TurnoSalidaDto;
import com.backend.exceptions.BadRequestException;
import com.backend.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITurnoService {
  TurnoSalidaDto registrarTurno(TurnoEntradaDto turno) throws BadRequestException;
  TurnoSalidaDto buscarTurnoPorId(Long id);
  List<TurnoSalidaDto> listarTurnos();
  void eliminarTurno(Long id) throws ResourceNotFoundException;
  TurnoSalidaDto actualizarTurno(TurnoEntradaDto turnoEntradaDto, Long id) throws
    ResourceNotFoundException, BadRequestException;
}
