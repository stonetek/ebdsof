package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AlunoTurmaResponse {

    private Long id;

    private Long alunoId;

    private Long turmaId;

    private String nomeAluno;

    private String nomeTurma;

}
