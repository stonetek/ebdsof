package stonetek.com.ebdsoft.dto.dtosespecificos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TurmaEbdDTO {

    private Long id;
    private String nomeTurma;
    private String nomeEbd;
    private Long idTurma;
    private Long idEbd;

    public TurmaEbdDTO(Long id, String nomeTurma, String nomeEbd, Long idTurma, Long idEbd) {
        this.id = id;
        this.nomeTurma = nomeTurma;
        this.nomeEbd = nomeEbd;
        this.idEbd = idEbd;
        this.idTurma = idTurma;
    }
}
