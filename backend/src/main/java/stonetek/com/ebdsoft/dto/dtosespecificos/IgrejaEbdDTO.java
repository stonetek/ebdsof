package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class IgrejaEbdDTO {

    private Long id;

    private EbdDTO ebd;

    private IgrejaDTO igreja;

    private String nomeIgreja;

    private String nomeEbd;

    private Long idEbd;

    private Long idIgreja;

    public IgrejaEbdDTO(Long id, Long idEbd, Long idIgreja, String nomeIgreja, String nomeEbd) {
        this.id = id;
        this.idEbd = idEbd;
        this.idIgreja = idIgreja;
        this.nomeIgreja = nomeIgreja;
        this.nomeEbd = nomeEbd;
    }

}
