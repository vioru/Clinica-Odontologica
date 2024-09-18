package com.backend.controller;

import com.backend.dto.entrada.TurnoEntradaDto;
import com.backend.dto.salida.TurnoSalidaDto;
import com.backend.exceptions.BadRequestException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.service.ITurnoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/turnos")
public class TurnoController {
  private ITurnoService turnoService;

  public TurnoController(ITurnoService turnoService) {
    this.turnoService = turnoService;
  }

  @PostMapping("/registrar")
  public ResponseEntity<TurnoSalidaDto> registrarTurno(@RequestBody @Valid TurnoEntradaDto turnoEntradaDto) throws BadRequestException {
    TurnoSalidaDto turnoSalidaDto = turnoService.registrarTurno(turnoEntradaDto);
    return new ResponseEntity<>(turnoSalidaDto, HttpStatus.CREATED);
  }

  @GetMapping("/listar")
  public ResponseEntity<List<TurnoSalidaDto>> listarTurnos() {
    return new ResponseEntity<>(turnoService.listarTurnos(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TurnoSalidaDto> buscarTurnoPorId(@PathVariable Long id) {
    return new ResponseEntity<>(turnoService.buscarTurnoPorId(id), HttpStatus.OK);
  }

  @PutMapping("/actualizar/{id}")
  public ResponseEntity<TurnoSalidaDto> actualizarTurno(@RequestBody @Valid TurnoEntradaDto turnoEntradaDto,
                                                        @PathVariable Long id) throws
    ResourceNotFoundException, BadRequestException {
    return new ResponseEntity<>(turnoService.actualizarTurno(turnoEntradaDto, id), HttpStatus.OK);
  }

  //DELETE
  @DeleteMapping("/eliminar")
  public ResponseEntity<String> eliminarTurno(@RequestParam Long id) throws ResourceNotFoundException{
    turnoService.eliminarTurno(id);
    return new ResponseEntity<>("Turno eliminado correctamente", HttpStatus.NO_CONTENT);
  }
}
