package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stonetek.com.ebdsoft.model.CodigoVerificacao;

import java.util.Optional;

public interface CodigoVerificacaoRepository extends JpaRepository<CodigoVerificacao, Long> {
    Optional<CodigoVerificacao> findByEmail(String email);
}

