package stonetek.com.ebdsoft.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AlunoAulaRequest {

    private Long id;

    private Long idAluno;

    private String nomeAluno;

    private Long idAula;

    private String licao;

    private Boolean presente;

    public Boolean isPresente() {
        return this.presente;
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }

}
