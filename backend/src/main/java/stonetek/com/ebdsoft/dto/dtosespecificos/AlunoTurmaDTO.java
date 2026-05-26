package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AlunoTurmaDTO {

    private Long id;

    private AlunoDTO aluno;

    private Long alunoId;

    private String nomeAluno;

    private TurmaDTO turma;

    private Long turmaId;

    private String nomeTurma;


    public AlunoTurmaDTO(Long id, String nomeTurma, String nomeAluno){
        this.id = id;
        this.nomeTurma = nomeTurma;
        this.nomeAluno = nomeAluno;
    }
}
