package mz.ac.unisced.sistema_expedientes.repository;

import mz.ac.unisced.sistema_expedientes.model.Expediente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpedienteRepository extends JpaRepository<Expediente, Long> {

    long countByEstadoIgnoreCase(String estado);

}