package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stonetek.com.ebdsoft.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findUsuarioByOrderByUsernameAsc();

    Optional<Usuario> findByUsername(String username);

    @Query("SELECT u FROM Usuario u WHERE UPPER(u.nome) LIKE CONCAT('%', UPPER(:nome), '%')")
    List<Usuario> findByNome(@Param("nome") String nome);

    List<Usuario> findByIgrejaId(Long igrejaId);


}
