package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stonetek.com.ebdsoft.model.EntregaRevista;
import stonetek.com.ebdsoft.model.PagamentoRevista;

import java.time.LocalDate;
import java.util.List;

public interface EntregaRevistaRepository extends JpaRepository<EntregaRevista, Long> {

    List<EntregaRevista> findByAlunoId(Long alunoId);

    List<EntregaRevista> findByProfessorId(Long professorId);

    List<EntregaRevista> findByDataEntrega(LocalDate dataEntrega);
}
