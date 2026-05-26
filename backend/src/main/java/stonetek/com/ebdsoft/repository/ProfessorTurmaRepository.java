package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stonetek.com.ebdsoft.model.*;

import java.util.List;
import java.util.Set;

public interface ProfessorTurmaRepository extends JpaRepository<ProfessorTurma, Long> {
    List<ProfessorTurma> findByProfessorId(Long professorId);

    Boolean existsByProfessorAndTurma(Professor professor, Turma turma );

    @Query("SELECT pt.turma FROM ProfessorTurma pt WHERE pt.professor.id = :professorId")
    Set<Turma> findTurmasByProfessorId(@Param("professorId") Long professorId);
}
