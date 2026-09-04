package mz.ac.unisced.sistema_expedientes.controller;

import jakarta.servlet.http.HttpSession;

import mz.ac.unisced.sistema_expedientes.model.Auditoria;
import mz.ac.unisced.sistema_expedientes.model.Expediente;
import mz.ac.unisced.sistema_expedientes.model.Movimentacao;
import mz.ac.unisced.sistema_expedientes.model.Usuario;

import mz.ac.unisced.sistema_expedientes.repository.AuditoriaRepository;
import mz.ac.unisced.sistema_expedientes.repository.ExpedienteRepository;
import mz.ac.unisced.sistema_expedientes.repository.MovimentacaoRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ExpedienteRepository expedienteRepository;
    private final AuditoriaRepository auditoriaRepository;

    public MovimentacaoController(
            MovimentacaoRepository movimentacaoRepository,
            ExpedienteRepository expedienteRepository,
            AuditoriaRepository auditoriaRepository) {

        this.movimentacaoRepository = movimentacaoRepository;
        this.expedienteRepository = expedienteRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    // Abrir formulário para nova movimentação
    @GetMapping("/novo/{expedienteId}")
    public String novaMovimentacao(
            @PathVariable Long expedienteId,
            Model model) {

        Expediente expediente = expedienteRepository.findById(expedienteId)
                .orElseThrow(() ->
                        new RuntimeException("Expediente não encontrado"));

        Movimentacao movimentacao = new Movimentacao();

        movimentacao.setExpediente(expediente);
        movimentacao.setDataMovimentacao(LocalDateTime.now());
        movimentacao.setEstado("Em andamento");

        model.addAttribute("movimentacao", movimentacao);
        model.addAttribute("expediente", expediente);

        return "movimentacoes/novo";
    }

    // Guardar movimentação
    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute Movimentacao movimentacao,
            HttpSession session) {

        Long expedienteId = movimentacao.getExpediente().getId();

        Expediente expediente = expedienteRepository.findById(expedienteId)
                .orElseThrow(() ->
                        new RuntimeException("Expediente não encontrado"));

        movimentacao.setExpediente(expediente);
        movimentacao.setDataMovimentacao(LocalDateTime.now());

        movimentacaoRepository.save(movimentacao);

        // ==========================================
        // REGISTAR AÇÃO NA AUDITORIA
        // ==========================================

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        String nomeUtilizador = "Utilizador desconhecido";

        if (usuario != null) {
            nomeUtilizador = usuario.getEmail();
        }

        Auditoria auditoria = new Auditoria();

        auditoria.setUtilizador(nomeUtilizador);
        auditoria.setDataHora(LocalDateTime.now());
        auditoria.setAcao("CRIAR MOVIMENTAÇÃO");

        auditoria.setDescricao(
                "Foi registada uma movimentação do expediente "
                + expediente.getNumero()
                + " de "
                + movimentacao.getRemetente()
                + " para "
                + movimentacao.getDestinatario()
        );

        auditoriaRepository.save(auditoria);

        return "redirect:/expedientes";
    }

    // Ver movimentações de um expediente
    @GetMapping("/expediente/{expedienteId}")
    public String listarPorExpediente(
            @PathVariable Long expedienteId,
            Model model) {

        Expediente expediente = expedienteRepository.findById(expedienteId)
                .orElseThrow(() ->
                        new RuntimeException("Expediente não encontrado"));

        model.addAttribute("expediente", expediente);

        model.addAttribute(
                "movimentacoes",
                movimentacaoRepository
                        .findByExpedienteIdOrderByDataMovimentacaoDesc(expedienteId)
        );

        return "movimentacoes/lista";
    }
}