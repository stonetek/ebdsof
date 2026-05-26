package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import stonetek.com.ebdsoft.model.Parcela;

import java.util.List;

public interface ParcelaRepository extends JpaRepository<Parcela, Long> {

    @EntityGraph(attributePaths = {
            "pagamentoParcelas",
            "pagamentoParcelas.pagamento",
            "pagamentoParcelas.pagamento.pedidoPagamentos",
            "pagamentoParcelas.pagamento.pedidoPagamentos.igreja"
    })
    List<Parcela> findAll();
}
