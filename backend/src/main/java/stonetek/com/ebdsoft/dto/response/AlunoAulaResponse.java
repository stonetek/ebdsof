package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stonetek.com.ebdsoft.model.AlunoAula;
import stonetek.com.ebdsoft.model.ProfessorAula;

@Setter
@Getter
@NoArgsConstructor
public class AlunoAulaResponse {

    private Long id;

    private Long idAluno;

    private String nomeAluno;

    private Long idAula;

    private String licao;

    private Boolean presente;

    public AlunoAulaResponse(AlunoAula alunoAula) {
        this.id = alunoAula.getId();
        this.idAluno = alunoAula.getAluno().getId();
        this.nomeAluno = alunoAula.getAluno().getNome();
        this.idAula = alunoAula.getAula().getId();
        this.licao = alunoAula.getAula().getLicao();
        this.presente = alunoAula.isPresente();
    }


    public Boolean isPresente() {
        return this.presente;
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }

}
