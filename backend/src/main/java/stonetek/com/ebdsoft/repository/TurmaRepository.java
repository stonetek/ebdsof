package stonetek.com.ebdsoft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import stonetek.com.ebdsoft.model.Turma;

public interface TurmaRepository extends JpaRepository<Turma, Long>{

    List<Turma> findAllByOrderByNomeAsc();

    Turma findByNome(String nomeTurma);

}
