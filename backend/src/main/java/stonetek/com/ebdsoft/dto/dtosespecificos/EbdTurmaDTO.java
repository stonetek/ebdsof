package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class EbdTurmaDTO {

    private Long id;

    private EbdDTO ebd;

    private TurmaDTO turma;

    private String nomeTurma;

}
