package mz.ac.unisced.sistema_expedientes.repository;

import mz.ac.unisced.sistema_expedientes.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}