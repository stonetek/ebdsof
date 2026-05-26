package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stonetek.com.ebdsoft.model.Pagamento;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    @Query("SELECT p FROM Pagamento p JOIN PedidoPagamento pp ON p.id = pp.pagamento.id WHERE pp.pedido.igreja.id = :igrejaId")
    List<Pagamento> findByPedidoIgrejaId(@Param("igrejaId") Long igrejaId);
}
