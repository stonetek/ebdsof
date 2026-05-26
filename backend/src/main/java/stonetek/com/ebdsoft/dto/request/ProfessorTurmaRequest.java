package stonetek.com.ebdsoft.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProfessorTurmaRequest {

    private Long id;

    private Long idProfessor;

    private String professorNome;

    private Long idTurma;

    private String turmaNome;
}
