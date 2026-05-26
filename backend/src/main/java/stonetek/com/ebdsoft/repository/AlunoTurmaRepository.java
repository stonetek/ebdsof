package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stonetek.com.ebdsoft.model.Aluno;
import stonetek.com.ebdsoft.model.AlunoTurma;
import stonetek.com.ebdsoft.model.Turma;

import java.util.List;
import java.util.Set;

public interface AlunoTurmaRepository extends JpaRepository<AlunoTurma, Long> {

    List<AlunoTurma> findByAlunoId(Long alunoId);

    boolean existsByAlunoAndTurma(Aluno aluno, Turma turma);

    List<AlunoTurma> findByTurmaId(Long idTurma);

    List<AlunoTurma> findByTurma(Turma turma);

    @Query("SELECT at.aluno FROM AlunoTurma at " +
            "JOIN at.turma t " +
            "JOIN ProfessorTurma pt ON pt.turma = t " +
            "WHERE pt.professor.id = :professorId")
    Set<Aluno> findAlunosByProfessorId(@Param("professorId") Long professorId);
}
