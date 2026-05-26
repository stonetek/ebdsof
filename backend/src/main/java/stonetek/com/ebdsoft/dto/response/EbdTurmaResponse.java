package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class EbdTurmaResponse {

    private Long id;

    private Long ebdId;

    private Long turmaId;

    private String nomeTurma;

    private String nomeEbd;

}
