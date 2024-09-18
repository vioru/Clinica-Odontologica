package com.backend.service.impl;

import com.backend.dto.entrada.TurnoEntradaDto;
import com.backend.dto.salida.OdontologoSalidaDto;
import com.backend.dto.salida.PacienteSalidaDto;
import com.backend.dto.salida.TurnoSalidaDto;
import com.backend.entity.Odontologo;
import com.backend.entity.Paciente;
import com.backend.entity.Turno;
import com.backend.exceptions.BadRequestException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.repository.TurnoRepository;
import com.backend.service.ITurnoService;
import com.backend.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TurnoService implements ITurnoService {

  private final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);

  private final TurnoRepository turnoRepository;
  private final ModelMapper modelMapper;
  private final OdontologoService odontologoService;
  private final PacienteService pacienteService;

  public TurnoService(TurnoRepository turnoRepository, ModelMapper modelMapper, OdontologoService odontologoService,
                      PacienteService pacienteService) {
    this.turnoRepository = turnoRepository;
    this.modelMapper = modelMapper;
    this.odontologoService = odontologoService;
    this.pacienteService = pacienteService;
    configureMapping();
  }
  @Override
  @Transactional
  public TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto)throws BadRequestException {
    Long idPaciente = turnoEntradaDto.getPacienteId();
    Long idOdontologo = turnoEntradaDto.getOdontologoId();

    PacienteSalidaDto pacienteEncontrado = pacienteService.buscarPacientePorId(idPaciente);
    OdontologoSalidaDto odontologoEncontrado = odontologoService.buscarOdontologoPorId(idOdontologo);

    if(pacienteEncontrado == null && odontologoEncontrado == null ) {
      throw new BadRequestException("Paciente con ID " + idPaciente + " no existe." + "Odontólogo con ID " + idOdontologo + " no existe." );
    }
    if(pacienteEncontrado == null) {
      throw new BadRequestException("Paciente con ID " + idPaciente + " no existe.");
    }
    if(odontologoEncontrado == null) {
      throw new BadRequestException("Odontólogo con ID " + idOdontologo + " no existe.");
    }

    LOGGER.info("TurnoEntradaDto: {}", JsonPrinter.toString(turnoEntradaDto));
    Turno entidadTurno = modelMapper.map(turnoEntradaDto, Turno.class);
    LOGGER.info("EntidadTurno antes de setear paciente y odontologo: {}", JsonPrinter.toString(entidadTurno));

    entidadTurno.setPaciente(modelMapper.map(pacienteEncontrado, Paciente.class));
    entidadTurno.setOdontologo(modelMapper.map(odontologoEncontrado, Odontologo.class));
    entidadTurno.setFechaHora(turnoEntradaDto.getFechaHora());
    LOGGER.info("EntidadTurno después de setear paciente y odontologo: {}", JsonPrinter.toString(entidadTurno));

    Turno turnoRegistrado = turnoRepository.save(entidadTurno);
    LOGGER.info("TurnoRegistrado: {}", JsonPrinter.toString(turnoRegistrado));

    TurnoSalidaDto turnoSalidaDto = modelMapper.map(turnoRegistrado, TurnoSalidaDto.class);
    LOGGER.info("TurnoSalidaDto: {}", JsonPrinter.toString(turnoSalidaDto));

    return turnoSalidaDto;
  }

  @Override
  public TurnoSalidaDto buscarTurnoPorId(Long id) {
    Turno turnoBuscado = turnoRepository.findById(id).orElse(null);
    LOGGER.info("Turno buscado: {}", JsonPrinter.toString(turnoBuscado));
    TurnoSalidaDto turnoEncontrado = null;
    if(turnoBuscado != null) {
      turnoEncontrado = modelMapper.map(turnoBuscado, TurnoSalidaDto.class);
      LOGGER.info("Turno encontrado: {}", JsonPrinter.toString(turnoEncontrado));
    } else {
      LOGGER.error("No se ha encontrado el turno con id {}", id);
    }
    return turnoEncontrado;
  }

  @Override
  public List<TurnoSalidaDto> listarTurnos() {
    List<TurnoSalidaDto> turnosSalidaDtos = turnoRepository.findAll().stream().map(turno -> modelMapper.map(turno,
            TurnoSalidaDto.class)).toList();
    LOGGER.info("Listado de todos los turnos: {}", JsonPrinter.toString(turnosSalidaDtos));

    return turnosSalidaDtos;
  }

  @Override
  @Transactional
  public void eliminarTurno(Long id) throws ResourceNotFoundException {
    Turno turno = turnoRepository.findById(id)
      .orElseThrow(()-> new ResourceNotFoundException("No se encontró Turno " +
        "con ese id: "+id));
    // Opcional. Desvincular el paciente y odontólogo del turno, si es necesario
    /*
    if(turno.getPaciente()!=null){
      turno.getPaciente().getTurnos().remove(turno);
    }
    if(turno.getOdontologo()!=null){
      turno.getOdontologo().getTurnos().remove(turno);
    } */
    //Eliminar el turno
    turnoRepository.delete(turno);
    LOGGER.warn("Se ha eliminado el turno con id: {}",id);
  }

  @Override
  @Transactional
  public TurnoSalidaDto actualizarTurno(TurnoEntradaDto turnoEntradaDto, Long id) throws
    ResourceNotFoundException, BadRequestException {
    Turno turnoExistente = turnoRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con id " + id));
    LOGGER.warn("turno Existente: {}", JsonPrinter.toString(turnoExistente));

    PacienteSalidaDto pacienteSalidaDto =
      pacienteService.buscarPacientePorId(turnoEntradaDto.getPacienteId());
    LOGGER.info("paciente encontrado salidaDto: {}",
      JsonPrinter.toString(pacienteSalidaDto));
    if (pacienteSalidaDto == null) {
      throw new BadRequestException("Paciente no encontrado con id " + turnoEntradaDto.getPacienteId());
    }
    Paciente pacienteExistente = modelMapper.map(pacienteSalidaDto,
      Paciente.class);
    LOGGER.info("Paciente Existente Entidad: {}",
      JsonPrinter.toString(pacienteExistente));

    OdontologoSalidaDto odontologoSalidaDto =
      odontologoService.buscarOdontologoPorId((turnoEntradaDto.getOdontologoId()));
    LOGGER.info("odontólogo encontrado salidaDto: {}",
      JsonPrinter.toString(odontologoSalidaDto));
    if (odontologoSalidaDto == null) {
      throw new BadRequestException("Odontologo no encontrado con id " + turnoEntradaDto.getOdontologoId());
    }
    Odontologo odontologoExistente = modelMapper.map(odontologoSalidaDto,
      Odontologo.class);
    LOGGER.info("Odontólogo Existente Entidad: {}",
      JsonPrinter.toString(odontologoExistente));

    turnoExistente.setFechaHora(turnoEntradaDto.getFechaHora());
    turnoExistente.setPaciente(pacienteExistente);
    turnoExistente.setOdontologo(odontologoExistente);

    Turno turnoActualizado = turnoRepository.save(turnoExistente);
    LOGGER.warn("turnoActualizado, ya con datos actuales: {}",
      JsonPrinter.toString(turnoActualizado));

    TurnoSalidaDto turnoSalidaDto = modelMapper.map(turnoActualizado,
      TurnoSalidaDto.class);
    LOGGER.info("Turno Salida DTO: {}",JsonPrinter.toString(turnoSalidaDto));
    return turnoSalidaDto;
  }

  private void configureMapping() {
    modelMapper.emptyTypeMap(TurnoEntradaDto.class, Turno.class)
            .addMappings(mapper -> mapper.map(TurnoEntradaDto::getFechaHora,
              Turno::setFechaHora));

    modelMapper.typeMap(Turno.class, TurnoSalidaDto.class)
            .addMappings(mapper -> mapper.map(Turno::getPaciente, TurnoSalidaDto::setPacienteSalidaDto))
            .addMappings(mapper -> mapper.map(Turno::getOdontologo, TurnoSalidaDto::setOdontologoSalidaDto));

    modelMapper.typeMap(OdontologoSalidaDto.class, Odontologo.class);
    modelMapper.typeMap(PacienteSalidaDto.class, Paciente.class)
            .addMappings(mapper -> mapper.map(PacienteSalidaDto::getDomicilioSalidaDto, Paciente::setDomicilio));
  }
};
