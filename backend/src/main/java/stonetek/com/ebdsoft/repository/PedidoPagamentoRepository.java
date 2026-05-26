package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stonetek.com.ebdsoft.model.PedidoPagamento;

import java.util.List;

public interface PedidoPagamentoRepository extends JpaRepository<PedidoPagamento, Long> {
    List<PedidoPagamento> findByIgrejaId(Long igrejaId);
}
