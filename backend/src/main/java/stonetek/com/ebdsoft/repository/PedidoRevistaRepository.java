package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stonetek.com.ebdsoft.model.PedidoRevista;
import stonetek.com.ebdsoft.model.Revista;

import java.util.List;

public interface PedidoRevistaRepository extends JpaRepository<PedidoRevista,Long> {

    List<PedidoRevista> findByPedidoId(Long pedidoId);
}
