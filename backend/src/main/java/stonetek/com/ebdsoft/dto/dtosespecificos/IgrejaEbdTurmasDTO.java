package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class IgrejaEbdTurmasDTO {

    private Long idEbd;
    private String ebdNome;
    private List<EbdTurmaDTO> turmas;
}
