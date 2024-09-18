package com.backend.service.impl;

import com.backend.dto.entrada.TurnoEntradaDto;
import com.backend.dto.salida.DomicilioSalidaDto;
import com.backend.dto.salida.OdontologoSalidaDto;
import com.backend.dto.salida.PacienteSalidaDto;
import com.backend.dto.salida.TurnoSalidaDto;
import com.backend.entity.Domicilio;
import com.backend.entity.Odontologo;
import com.backend.entity.Paciente;
import com.backend.entity.Turno;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import com.backend.exceptions.BadRequestException;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.repository.OdontologoRepository;
import com.backend.repository.PacienteRepository;
import com.backend.repository.TurnoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TurnoServiceTest {
  private final ModelMapper modelMapper = new ModelMapper();

  private final OdontologoRepository odontologoRepositoryMock =
          mock(OdontologoRepository.class);

  private final OdontologoService odontologoService =
          new OdontologoService(odontologoRepositoryMock, modelMapper);

  private final PacienteRepository pacienteRepositoryMock =
          mock(PacienteRepository.class);
  private final PacienteService pacienteService =
          new PacienteService(pacienteRepositoryMock, modelMapper);

  private final TurnoRepository turnoRepositoryMock = mock(TurnoRepository.class);
  private final TurnoService turnoService =
          new TurnoService(turnoRepositoryMock, modelMapper, odontologoService,
                  pacienteService);

  private static final LocalDate fechaIngreso = LocalDate.now();
  private static final LocalDateTime fechaTurno =
          LocalDateTime.now().plusDays(1);
  private static Domicilio domicilio;
  private static DomicilioSalidaDto domicilioSalidaDto;
  private static Paciente paciente;
  private static Paciente paciente2;
  private static PacienteSalidaDto pacienteSalidaDto;
  private static Odontologo odontologo;
  private static Odontologo odontologo2;
  private static OdontologoSalidaDto odontologoSalidaDto;
  private static Turno turnoExistente;
  private static TurnoEntradaDto turnoEntradaDto;
  private static TurnoSalidaDto turnoSalidaDto;

  @BeforeAll
  public static void setup() {

    domicilio = new Domicilio(1L, "Calle Falsa", 123, "Springfield", "Ohio");
    domicilioSalidaDto = new DomicilioSalidaDto(domicilio.getId(),
            domicilio.getCalle(), domicilio.getNumero(), domicilio.getLocalidad(),
            domicilio.getProvincia());


    paciente = new Paciente(1L, "Juan", "Perez", 12345678, fechaIngreso, domicilio);
    paciente2 =new Paciente(2L,"Lida","Soto",10023,fechaIngreso,domicilio);
    pacienteSalidaDto = new PacienteSalidaDto(paciente.getId(),
            paciente.getNombre(), paciente.getApellido(), paciente.getDni(),
            paciente.getFechaIngreso(), domicilioSalidaDto);

    odontologo = new Odontologo(1L, "mat-1001", "Anabel", "Mendez");
    odontologo2 = new Odontologo(2L,"abc-123","John","Zoidberg");

    turnoExistente = new Turno(1L, paciente, odontologo, fechaTurno);

    turnoEntradaDto = new TurnoEntradaDto(1L, 1L, fechaTurno);
    turnoSalidaDto = new TurnoSalidaDto(1L, pacienteSalidaDto, odontologoSalidaDto,
            fechaTurno);
  }

  @Test
  public void dadoUnIdDebeBuscarEnRepositorioYRetornarElTurnoConEseId(){
    Long id =1L;
    when(turnoRepositoryMock.findById(id)).thenReturn(Optional.of(turnoExistente));
    TurnoSalidaDto respuestaSalidaDto = turnoService.buscarTurnoPorId(id);
    assertNotNull(respuestaSalidaDto);
    assertNotNull(turnoSalidaDto.getId());
    assertEquals(1L,respuestaSalidaDto.getId());
  }
  @Test
  public void alBuscarTurnoPorId_Inexistente_DebeRetornarNull() {

    Long idInexistente = 15L;
    when(turnoRepositoryMock.findById(idInexistente)).thenReturn(Optional.empty());

    assertNull(turnoService.buscarTurnoPorId(idInexistente));

    verify(turnoRepositoryMock).findById(idInexistente);
  }
  @Test
  public void alIntentarAactualizarUnTurnoInexistenteLanceResourceNotFoundException() throws ResourceNotFoundException, BadRequestException {
    Long idInexistente = 15L;
    when(turnoRepositoryMock.findById(idInexistente)).thenReturn(Optional.empty());

    assertNull(turnoService.buscarTurnoPorId(idInexistente));
    assertThrows(ResourceNotFoundException.class,
            () -> turnoService.actualizarTurno(turnoEntradaDto, idInexistente));
  }
}