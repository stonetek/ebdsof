package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stonetek.com.ebdsoft.model.Aula;
import stonetek.com.ebdsoft.model.AulaTurma;
import stonetek.com.ebdsoft.model.Professor;
import stonetek.com.ebdsoft.model.Turma;

import java.util.Collection;
import java.util.List;

public interface AulaTurmaRepository extends JpaRepository<AulaTurma, Long> {

    List<AulaTurma> findByAulaId(Long aulaId);

    Boolean existsByAulaAndTurma(Aula aula, Turma turma );

    @Query("SELECT at FROM AulaTurma at WHERE at.turma IN :turmas")
    List<AulaTurma> findByTurmas(@Param("turmas") List<Turma> turmas);

    boolean existsByTurmaAndAula(Turma turma, Aula savedAula);


}
