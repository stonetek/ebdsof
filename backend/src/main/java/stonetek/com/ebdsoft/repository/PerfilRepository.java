package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stonetek.com.ebdsoft.model.Perfil;
import stonetek.com.ebdsoft.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    List<Perfil> findPerfilByOrderByNomeAsc();

    Optional<Perfil> findByNome(String nome);

    @Query("SELECT p FROM Perfil p WHERE UPPER(p.nome) LIKE CONCAT('%', UPPER(:nome), '%') ORDER BY p.nome ASC")
    List<Perfil> findByNomeOrderByNomeAsc(@Param("nome") String nome);

    @Query("SELECT p.nome FROM Perfil p WHERE p.id = :perfilId")
    String findNomeById(@Param("perfilId") Long perfilId);
}
