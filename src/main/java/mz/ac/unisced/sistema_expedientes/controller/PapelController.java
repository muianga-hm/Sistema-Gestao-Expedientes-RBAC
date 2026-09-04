package mz.ac.unisced.sistema_expedientes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class PapelController {

    @GetMapping("/papeis")
    public String listarPapeis(Model model) {

        List<String> papeis = Arrays.asList(
                "ADMIN",
                "GESTOR",
                "FUNCIONARIO"
        );

        model.addAttribute("papeis", papeis);

        return "papeis";
    }
}
