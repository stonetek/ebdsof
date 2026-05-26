package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stonetek.com.ebdsoft.model.ProfessorAula;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ProfessorAulaResponse {

    private Long id;

    private Long idProfessor;

    private String nomeProfessor;

    private LocalDate aniversario;

    private Long idAula;

    private String licao;



    public ProfessorAulaResponse(ProfessorAula professorAula) {
        this.id = professorAula.getId();
        this.idProfessor = professorAula.getProfessor().getId();
        this.nomeProfessor = professorAula.getProfessor().getNome();
        this.aniversario = professorAula.getProfessor().getAniversario();
        this.idAula = professorAula.getAula().getId();
        this.licao = professorAula.getAula().getLicao();
    }


}
