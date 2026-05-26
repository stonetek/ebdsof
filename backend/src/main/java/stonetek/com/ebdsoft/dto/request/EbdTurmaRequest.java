package stonetek.com.ebdsoft.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class EbdTurmaRequest {

    private Long id;

    private Long ebdId;

    private Long turmaId;

    private String nomeTurma;

    private String nomeEbd;

}
