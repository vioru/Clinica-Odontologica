package com.backend.repository;

import com.backend.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TurnoRepository extends JpaRepository<Turno, Long> {
    @Modifying
    @Query("UPDATE Turno t SET t.paciente = NULL WHERE t.paciente.id = :pacienteId")
    void setPacienteToNullByPacienteId(@Param("pacienteId") Long pacienteId);

    @Modifying
    @Query("UPDATE Turno t SET t.odontologo = NULL WHERE t.odontologo.id = :odontologoId")
    void setOdontologoToNullByOdontologoId(@Param("odontologoId") Long odontologoId);
}
