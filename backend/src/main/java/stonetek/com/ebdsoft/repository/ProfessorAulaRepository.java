package stonetek.com.ebdsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stonetek.com.ebdsoft.model.Aula;
import stonetek.com.ebdsoft.model.Professor;
import stonetek.com.ebdsoft.model.ProfessorAula;
import stonetek.com.ebdsoft.model.Turma;

import java.util.List;

public interface ProfessorAulaRepository extends JpaRepository<ProfessorAula, Long> {
    List<ProfessorAula> findByAulaId(Long aulaId);

    Boolean existsByProfessorAndAula(Professor professor, Aula aula );
}
