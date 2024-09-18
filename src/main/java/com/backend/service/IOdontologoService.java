package com.backend.service;

import com.backend.dto.entrada.OdontologoEntradaDto;
import com.backend.dto.salida.OdontologoSalidaDto;
import com.backend.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOdontologoService {
  OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologo);

  OdontologoSalidaDto buscarOdontologoPorId(Long id);

  List<OdontologoSalidaDto> listarOdontologos();

  void eliminarOdontologo(Long id) throws ResourceNotFoundException;

  OdontologoSalidaDto actualizarOdontologo(OdontologoEntradaDto odontologoEntradaDto, Long id) throws
    ResourceNotFoundException;

}
