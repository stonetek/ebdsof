package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class AlunoDetalhesDTO {

    private Long alunoId;

    private String nomeAluno;

    private List<TurmaDTO> turmas;

    private List<AulaDTO> aulas;

    private Map<Integer, Map<Integer, Map<Integer, List<AulaDTO>>>> aulasPorAnoMesTrimestre;

}
