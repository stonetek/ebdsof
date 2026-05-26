package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stonetek.com.ebdsoft.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByTrimestre(short trimestre);

    List<Pedido> findByStatus(String status);

    List<Pedido> findByIgreja_Id(Long igrejaId);

    List<Pedido> findByIgrejaId(Long igrejaId);

    @Query("SELECT p FROM Pedido p JOIN FETCH p.pedidoRevistas pr JOIN FETCH pr.revista")
    List<Pedido> findAllWithRevistas();


}
