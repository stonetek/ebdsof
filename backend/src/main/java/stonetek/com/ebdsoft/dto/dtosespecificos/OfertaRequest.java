package stonetek.com.ebdsoft.dto.dtosespecificos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OfertaRequest {

    private Long turmaId;

    private Integer mes;

    private Integer trimestre;

    private Integer ano;
}
