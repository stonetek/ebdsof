package stonetek.com.ebdsoft.dto.dtosespecificos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProfessorTurmaDTO {

    private Long id;

    private String nomeProfessor;

    private String nomeTurma;


    public ProfessorTurmaDTO(Long id, String nomeTurma, String nomeProfessor) {
        this.id = id;
        this.nomeProfessor = nomeProfessor;
        this.nomeTurma = nomeTurma;
    }
}
