package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
public class MatriculaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long alunoId;

    private Long turmaId;

    private List<Long> alunosIds;
}
