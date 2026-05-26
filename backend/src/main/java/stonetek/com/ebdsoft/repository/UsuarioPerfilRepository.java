package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stonetek.com.ebdsoft.model.Usuario;
import stonetek.com.ebdsoft.model.UsuarioPerfil;

import java.util.Optional;

public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, Long> {

    Optional<UsuarioPerfil> findByUsuario(Usuario usuario);

}
