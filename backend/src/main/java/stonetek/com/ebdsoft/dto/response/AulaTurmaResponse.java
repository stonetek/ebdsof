package stonetek.com.ebdsoft.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stonetek.com.ebdsoft.model.AulaTurma;

@Setter
@Getter
@NoArgsConstructor
public class AulaTurmaResponse {

    private Long id;

    private Long idAula;

    private String licao;

    private Long idTurma;

    private String nomeTurma;

    public AulaTurmaResponse(AulaTurma aulaTurma) {
        this.id = aulaTurma.getId();
        this.idAula = aulaTurma.getAula().getId();
        this.licao = aulaTurma.getAula().getLicao();
        this.idTurma = aulaTurma.getTurma().getId();
        this.nomeTurma = aulaTurma.getTurma().getNome();
    }
}
