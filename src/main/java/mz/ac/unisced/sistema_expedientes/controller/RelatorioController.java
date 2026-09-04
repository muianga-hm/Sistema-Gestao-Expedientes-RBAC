package mz.ac.unisced.sistema_expedientes.controller;

import mz.ac.unisced.sistema_expedientes.repository.ExpedienteRepository;
import mz.ac.unisced.sistema_expedientes.repository.MovimentacaoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RelatorioController {

    private final ExpedienteRepository expedienteRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public RelatorioController(
            ExpedienteRepository expedienteRepository,
            MovimentacaoRepository movimentacaoRepository) {

        this.expedienteRepository = expedienteRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @GetMapping("/relatorios")
    public String relatorios(Model model) {

        long totalExpedientes = expedienteRepository.count();

        long emAndamento = expedienteRepository
                .countByEstadoIgnoreCase("Em andamento");

        long encaminhados = expedienteRepository
                .countByEstadoIgnoreCase("ENCAMINHADO");

        long concluidos = expedienteRepository
                .countByEstadoIgnoreCase("Concluído");

        long arquivados = expedienteRepository
                .countByEstadoIgnoreCase("Arquivado");

        long totalMovimentacoes = movimentacaoRepository.count();

        model.addAttribute("totalExpedientes", totalExpedientes);
        model.addAttribute("emAndamento", emAndamento);
        model.addAttribute("encaminhados", encaminhados);
        model.addAttribute("concluidos", concluidos);
        model.addAttribute("arquivados", arquivados);
        model.addAttribute("totalMovimentacoes", totalMovimentacoes);

        return "relatorios/relatorios";
    }
}