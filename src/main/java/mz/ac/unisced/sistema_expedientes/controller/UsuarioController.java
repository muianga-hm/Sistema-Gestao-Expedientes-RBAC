package mz.ac.unisced.sistema_expedientes.controller;

import mz.ac.unisced.sistema_expedientes.model.Usuario;
import mz.ac.unisced.sistema_expedientes.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {

        model.addAttribute("usuarios", usuarioRepository.findAll());

        return "usuario/usuario";
    }

    @GetMapping("/usuarios/novo")
    public String novoUsuario(Model model) {

        model.addAttribute("usuario", new Usuario());

        return "usuario/novo";
    }

    @PostMapping("/usuarios/salvar")
    public String salvarUsuario(Usuario usuario) {

        usuarioRepository.save(usuario);

        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/excluir/{id}")
    public String excluirUsuario(@PathVariable Long id) {

        usuarioRepository.deleteById(id);

        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        model.addAttribute("usuario", usuario);

        return "usuario/editar";
    }
}

