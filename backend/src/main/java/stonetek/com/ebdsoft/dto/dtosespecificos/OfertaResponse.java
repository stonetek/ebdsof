package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class OfertaResponse {

    private Long turmaId;

    private Integer mes;

    private Integer trimestre;

    private Integer ano;

    private BigDecimal totalOfertas;
}
