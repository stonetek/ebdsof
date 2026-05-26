package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import stonetek.com.ebdsoft.model.Aluno;
import stonetek.com.ebdsoft.model.AlunoAula;
import stonetek.com.ebdsoft.model.Aula;

import java.util.List;

public interface AlunoAulaRepository extends JpaRepository<AlunoAula, Long>{
    List<AlunoAula> findByAulaId(Long aulaId);

    List<AlunoAula> findByAlunoId(Long alunoId);

    boolean existsByAlunoAndAula(Aluno aluno, Aula savedAula);
}
