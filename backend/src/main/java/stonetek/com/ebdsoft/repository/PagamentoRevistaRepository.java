package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stonetek.com.ebdsoft.model.PagamentoRevista;

import java.time.LocalDate;
import java.util.List;

public interface PagamentoRevistaRepository extends JpaRepository<PagamentoRevista, Long> {

    List<PagamentoRevista> findByAlunoId(Long alunoId);

    List<PagamentoRevista> findByProfessorId(Long professorId);

    List<PagamentoRevista> findByPagoFalse();

    List<PagamentoRevista> findByDataVencimentoBefore(LocalDate data);

    List<PagamentoRevista> findByRevistaId(Long revistaId);
}
