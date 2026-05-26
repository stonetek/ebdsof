package stonetek.com.ebdsoft.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProfessorTurmaResponse {

    private Long id;

    private Long idProfessor;

    private String nomeProfessor;

    private Long idTurma;

    private String nomeTurma;
}
