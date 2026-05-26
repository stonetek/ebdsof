package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stonetek.com.ebdsoft.model.Pedido;
import stonetek.com.ebdsoft.model.Revista;
import stonetek.com.ebdsoft.model.PedidoRevista;

import java.util.List;
import java.util.Optional;

public interface RevistaRepository extends JpaRepository<Revista, Long> {


    List<Revista> findByTipo(String tipo);

    List<Revista> findByFormato(String formato);

    List<Revista> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT r FROM Revista r WHERE r.id = :id")
    Optional<Revista> findRevistaById(@Param("id") Long id);


    List<Revista> findByNomeContaining(String nome);



}
