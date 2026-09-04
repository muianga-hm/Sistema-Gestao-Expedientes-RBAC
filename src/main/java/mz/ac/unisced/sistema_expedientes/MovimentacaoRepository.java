package mz.ac.unisced.sistema_expedientes.repository;

import mz.ac.unisced.sistema_expedientes.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    List<Movimentacao> findByExpedienteIdOrderByDataMovimentacaoDesc(Long expedienteId);
}