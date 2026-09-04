package mz.ac.unisced.sistema_expedientes.controller;

import mz.ac.unisced.sistema_expedientes.model.Auditoria;
import mz.ac.unisced.sistema_expedientes.model.Expediente;
import mz.ac.unisced.sistema_expedientes.repository.AuditoriaRepository;
import mz.ac.unisced.sistema_expedientes.repository.ExpedienteRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@Controller
public class ExpedienteController {

    private final ExpedienteRepository expedienteRepository;
    private final AuditoriaRepository auditoriaRepository;

    public ExpedienteController(
            ExpedienteRepository expedienteRepository,
            AuditoriaRepository auditoriaRepository) {

        this.expedienteRepository = expedienteRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    // Listar expedientes
    @GetMapping("/expedientes")
    public String listarExpedientes(Model model) {
        model.addAttribute("expedientes", expedienteRepository.findAll());
        return "expediente/expedientes";
    }

    // Formulário para novo expediente
    @GetMapping("/expedientes/novo")
    public String novoExpediente(Model model) {
        model.addAttribute("expediente", new Expediente());
        return "expediente/novo";
    }

    // Guardar expediente
    @PostMapping("/expedientes/salvar")
    public String salvarExpediente(Expediente expediente) {

        expedienteRepository.save(expediente);

        Auditoria auditoria = new Auditoria();
        auditoria.setUtilizador("Utilizador autenticado");
        auditoria.setDataHora(LocalDateTime.now());
        auditoria.setAcao("CRIAR EXPEDIENTE");
        auditoria.setDescricao(
                "Foi criado o expediente " + expediente.getNumero()
        );

        auditoriaRepository.save(auditoria);

        return "redirect:/expedientes";
    }

    // Formulário para editar expediente
    @GetMapping("/expedientes/editar/{id}")
    public String editarExpediente(
            @PathVariable Long id,
            Model model) {

        Expediente expediente = expedienteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Expediente não encontrado"));

        model.addAttribute("expediente", expediente);

        return "expediente/editar";
    }

    // Excluir expediente
    @GetMapping("/expedientes/excluir/{id}")
    public String excluirExpediente(@PathVariable Long id) {

        Expediente expediente = expedienteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Expediente não encontrado"));

        String numeroExpediente = expediente.getNumero();

        expedienteRepository.deleteById(id);

        Auditoria auditoria = new Auditoria();
        auditoria.setUtilizador("Utilizador autenticado");
        auditoria.setDataHora(LocalDateTime.now());
        auditoria.setAcao("EXCLUIR EXPEDIENTE");
        auditoria.setDescricao(
                "Foi excluído o expediente " + numeroExpediente
        );

        auditoriaRepository.save(auditoria);

        return "redirect:/expedientes";
    }

    // Abrir formulário para nova movimentação
    @GetMapping("/expedientes/{id}/movimentar")
    public String movimentarExpediente(@PathVariable Long id) {
        return "redirect:/movimentacoes/novo/" + id;
    }

    // Ver histórico de movimentações
    @GetMapping("/expedientes/{id}/movimentacoes")
    public String verMovimentacoes(@PathVariable Long id) {
        return "redirect:/movimentacoes/expediente/" + id;
    }
}