package mz.ac.unisced.sistema_expedientes.controller;

import mz.ac.unisced.sistema_expedientes.model.Auditoria;
import mz.ac.unisced.sistema_expedientes.repository.AuditoriaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuditoriaController {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaController(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @GetMapping("/auditoria")
    public String listarAuditoria(Model model) {

        model.addAttribute(
                "auditorias",
                auditoriaRepository.findAllByOrderByDataHoraDesc()
        );

        return "auditoria/auditoria";
    }
}