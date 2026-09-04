package mz.ac.unisced.sistema_expedientes.controller;

import jakarta.servlet.http.HttpSession;
import mz.ac.unisced.sistema_expedientes.model.Usuario;
import mz.ac.unisced.sistema_expedientes.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

private final UsuarioRepository usuarioRepository;

public LoginController(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
}

@GetMapping("/login")
public String mostrarLogin() {
    return "login";
}

@PostMapping("/login")
public String processarLogin(
        @RequestParam String username,
        @RequestParam String password,
        HttpSession session) {

    Usuario usuario = usuarioRepository.findAll()
            .stream()
            .filter(u -> u.getEmail().equals(username)
                    && u.getSenha().equals(password))
            .findFirst()
            .orElse(null);

    if (usuario != null) {

        session.setAttribute("usuarioLogado", usuario);
        session.setAttribute("papel", usuario.getPapel());

        return "redirect:/";
    }

    return "redirect:/login?erro";
}

@GetMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
}

}
