package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AlunoAulaDTO {

    private Long id;

    private String nomeAluno;

    private String licao;

    private Boolean presente;

    public AlunoAulaDTO(Long id, String licao, String nomeAluno) {
        this.id = id;
        this.licao = licao;
        this.nomeAluno = nomeAluno;
    }

}
