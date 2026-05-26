package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TurmasEbdsDTO {

    private Long id;
    private String nomeTurma;
    private String nomeEbd;

    public TurmasEbdsDTO(Long id, String nomeTurma, String nomeEbd) {
        this.id = id;
        this.nomeTurma = nomeTurma;
        this.nomeEbd = nomeEbd;
    }
}
