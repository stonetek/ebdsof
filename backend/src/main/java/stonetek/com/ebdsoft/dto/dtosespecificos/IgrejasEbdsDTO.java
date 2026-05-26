package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class IgrejasEbdsDTO {

    private Long id;
    private Long igrejaId;
    private Long ebdId;
    private String nomeIgreja;
    private String nomeEbd;

    public IgrejasEbdsDTO(Long id, Long igrejaId, Long ebdId, String nomeIgreja, String nomeEbd) {
        this.id = id;
        this.igrejaId = igrejaId;
        this.ebdId = ebdId;
        this.nomeIgreja = nomeIgreja;
        this.nomeEbd = nomeEbd;
    }
}
