package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stonetek.com.ebdsoft.model.Pagamento;
import stonetek.com.ebdsoft.model.PagamentoParcela;

import java.util.List;
import java.util.Optional;

public interface PagamentoParcelaRepository extends JpaRepository<PagamentoParcela, Long> {
    Optional<PagamentoParcela> findByParcelaId(Long parcelaId);

    List<PagamentoParcela> findByPagamentoId(Long id);

    @Query("SELECT pp FROM PagamentoParcela pp WHERE pp.pagamento.id = :pagamentoId AND pp.parcela.numero = :numeroParcela")
    Optional<PagamentoParcela> findByPagamentoIdAndNumeroParcela(@Param("pagamentoId") Long pagamentoId, @Param("numeroParcela") Integer numeroParcela);

    @Query("""
        SELECT pp FROM PagamentoParcela pp
        JOIN pp.pagamento p
        JOIN PedidoPagamento ppag ON ppag.pagamento = p
        WHERE ppag.igreja.id = :igrejaId
    """)
    List<PagamentoParcela> findParcelasByIgreja(@Param("igrejaId") Long igrejaId);

}
