package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stonetek.com.ebdsoft.model.Aluno;
import stonetek.com.ebdsoft.model.Professor;
import stonetek.com.ebdsoft.model.Turma;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TurmaDTO {

    private Long id;

    private String nome;

    //private Turma turma;

    private List<AlunoDTO> alunos;

    private List<ProfessorDTO> professores;

}
