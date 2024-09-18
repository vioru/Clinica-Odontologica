package com.backend.service.impl;

import com.backend.dto.entrada.DomicilioEntradaDto;
import com.backend.dto.entrada.PacienteEntradaDto;
import com.backend.dto.salida.PacienteSalidaDto;
import com.backend.entity.Domicilio;
import com.backend.entity.Paciente;
import com.backend.exceptions.ResourceNotFoundException;
import com.backend.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class PacienteServiceTest {
  private final PacienteRepository pacienteRepositoryMock =
    mock(PacienteRepository.class);
  private final ModelMapper modelMapper = new ModelMapper();

  private final PacienteService pacienteService =
    new PacienteService(pacienteRepositoryMock, modelMapper);

  private static PacienteEntradaDto pacienteEntradaDto;
  private static Paciente pacienteExistente;

  @BeforeAll
  static void setUp() {
    // Inicializa el objeto común antes de cada prueba
    pacienteExistente = new Paciente(1L, "Juan", "Perez", 123456, LocalDate.of(2024, 6
      , 22), new Domicilio(1L, "Calle", 123, "Localidad", "Provincia"));
    pacienteEntradaDto = new PacienteEntradaDto("Juan", "Perez", 123456,
      LocalDate.of(2024, 6, 22), new DomicilioEntradaDto("Calle", 123,
      "Localidad", "Provincia"));
  }
  @Test
  void deberiaMandarAlRepositorioUnPacienteDeNombreJuan_yRetornarUnSalidaDtoConSuId() {
    when(pacienteRepositoryMock.save(any(Paciente.class))).thenReturn(pacienteExistente);

    PacienteSalidaDto pacienteSalidaDto =
      pacienteService.registrarPaciente(pacienteEntradaDto);

    assertNotNull(pacienteSalidaDto);
    assertNotNull(pacienteSalidaDto.getId());
    assertEquals("Juan", pacienteSalidaDto.getNombre());
    verify(pacienteRepositoryMock, times(1)).save(any(Paciente.class));
  }

  @Test
  void deberiaDevolverUnListadoNoVacioDePacientes() {
    List<Paciente> pacientes = new java.util.ArrayList<>(List.of(pacienteExistente));
    when(pacienteRepositoryMock.findAll()).thenReturn(pacientes);

    List<PacienteSalidaDto> listadoDePacientes =
      pacienteService.listarPacientes();
    assertFalse(listadoDePacientes.isEmpty());
  }

  @Test
  void deberiaEliminarElPacienteConId1() {
    when(pacienteRepositoryMock.findById(1L)).thenReturn(Optional.of(pacienteExistente));
    doNothing().when(pacienteRepositoryMock).delete(pacienteExistente);
    assertDoesNotThrow(() -> pacienteService.eliminarPaciente(1L));
    verify(pacienteRepositoryMock, times(1)).findById(1L);
    verify(pacienteRepositoryMock, times(1)).delete(pacienteExistente);
  }


/*
  @Test
  void deberiaEliminarElPacienteConId2() {
    pacienteExistente.setId(2L);
    when(pacienteRepositoryMock.findById(2L)).thenReturn(Optional.of(pacienteExistente));
    doNothing().when(pacienteRepositoryMock).delete(pacienteExistente);

    try {
      pacienteService.eliminarPaciente(2L);
    } catch (ResourceNotFoundException resourceNotFoundException) {
      fail("No debería lanzarse la excepción");
    }
    // Act & Assert
    assertDoesNotThrow(() -> pacienteService.eliminarPaciente(2L));
    verify(pacienteRepositoryMock, times(1)).findById(2L);
    verify(pacienteRepositoryMock, times(1)).delete(pacienteExistente);
  }
*/
@Test
void deberiaEliminarElPacienteConId2() {
  // Arrange
  Long id = 2L;
  Paciente paciente2 = new Paciente(); // Crea un nuevo paciente para este test
  paciente2.setId(id);
  when(pacienteRepositoryMock.findById(id)).thenReturn(Optional.of(paciente2));
  doNothing().when(pacienteRepositoryMock).delete(paciente2);

  try {
    pacienteService.eliminarPaciente(id);
  }catch(ResourceNotFoundException rnf){
    fail("No deberia lanzarse la excepción");
  }

  // Assert
  verify(pacienteRepositoryMock, times(1)).findById(id);
  verify(pacienteRepositoryMock, times(1)).delete(paciente2);
}
  @Test
  void deberiaDevolverUnaListaVaciaDePacientes() {
    when(pacienteRepositoryMock.findAll()).thenReturn(new ArrayList<>());
    List<PacienteSalidaDto> pacientes = pacienteService.listarPacientes();
    assertTrue(pacientes.isEmpty());
    verify(pacienteRepositoryMock, times(1)).findAll();
  }

  @Test
  void deberiaLanzarExcepcionCuandoElPacienteAActualizarNoSeaEncontrado() {
    when(pacienteRepositoryMock.findById(2L)).thenReturn(Optional.empty());
    pacienteEntradaDto.setDni(66666666);
    assertThrows(ResourceNotFoundException.class,
      () -> pacienteService.actualizarPaciente(pacienteEntradaDto, 2L));

  }

  //Agregados de Tarea :)
  @Test
  void dadoElIdUnoDebeBuscarEnRepositorioYRetornarElPacienteConEseId() {
    Long id = 1L;
    when(pacienteRepositoryMock.findById(id)).thenReturn(Optional.of(pacienteExistente));
    PacienteSalidaDto pacienteSalidaDto =
      pacienteService.buscarPacientePorId(id);
    assertNotNull(pacienteSalidaDto);
    assertNotNull(pacienteSalidaDto.getId());
    assertEquals("Perez", pacienteSalidaDto.getApellido());
  }
  @Test
  public void debeSerPosibleActualizarDatosDePacienteExistente() throws ResourceNotFoundException {
    when(pacienteRepositoryMock.findById(1L)).thenReturn(Optional.of(pacienteExistente));

    Domicilio nuevoDomicilio = new Domicilio(2L, "Sucre",
      120,"Loja","Loja");
    DomicilioEntradaDto nuevoDomicilioEntradaDto =new DomicilioEntradaDto(
      "Sucre",120,"Loja","Loja");

    pacienteEntradaDto = new PacienteEntradaDto("Ana","Mendez",11223344,
      LocalDate.of(2024, 6, 22),nuevoDomicilioEntradaDto);

    Paciente pacienteActualizado = new Paciente();
    pacienteActualizado.setId(1L);
    pacienteActualizado.setNombre(pacienteEntradaDto.getNombre());
    pacienteActualizado.setApellido(pacienteEntradaDto.getApellido());
    pacienteActualizado.setDni(pacienteEntradaDto.getDni());
    pacienteActualizado.setFechaIngreso(pacienteEntradaDto.getFechaIngreso());
    pacienteActualizado.setDomicilio(nuevoDomicilio);

    when(pacienteRepositoryMock.save(any(Paciente.class))).thenReturn(pacienteActualizado);

    PacienteSalidaDto resultado =
      pacienteService.actualizarPaciente(pacienteEntradaDto, 1L);

    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    assertEquals("Mendez", resultado.getApellido());

    verify(pacienteRepositoryMock).save(argThat(paciente -> paciente.getId().equals(1L)
      && paciente.getDni()==(11223344)
      && paciente.getNombre().equals("Ana")
      && paciente.getApellido().equals("Mendez")
    ));
  }

  @Test
  public void alBuscarPacientePorId_Inexistente_DebeRetornarNull() {

    Long idInexistente = 15L;
    when(pacienteRepositoryMock.findById(idInexistente)).thenReturn(Optional.empty());

    assertNull(pacienteService.buscarPacientePorId(idInexistente));

    verify(pacienteRepositoryMock).findById(idInexistente);
  }
}
