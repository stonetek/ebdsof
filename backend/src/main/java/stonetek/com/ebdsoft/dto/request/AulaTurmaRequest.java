package stonetek.com.ebdsoft.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AulaTurmaRequest {

    private Long id;

    private Long idAula;

    private String licao;

    private Long idTurma;

    private String nomeTurma;
}
