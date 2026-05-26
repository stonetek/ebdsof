package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TrimestreRequest {

    private String trimestre;

    private Integer ano;

    public TrimestreRequest(String trimestre, Integer ano) {
        this.trimestre = trimestre;
        this.ano = ano;
    }

}

