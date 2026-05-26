package stonetek.com.ebdsoft.dto.dtosespecificos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class AlunoFaixaEtariaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nomeTurma;
}
