package stonetek.com.ebdsoft.dto.dtosespecificos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AulaTurmaDTO {

    private Long id;

    private String licao;

    private String nomeTurma;

    public AulaTurmaDTO(Long id, String licao, String nomeTurma) {
        this.id = id;
        this.licao = licao;
        this.nomeTurma = nomeTurma;
    }
}
