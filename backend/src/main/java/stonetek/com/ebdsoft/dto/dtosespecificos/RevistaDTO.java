package stonetek.com.ebdsoft.dto.dtosespecificos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class RevistaDTO {

    private Long id;
    private String nome;
    private String formato;
    private String tipo;
    private BigDecimal preco;
    private Long pedidoId;
    private Integer quantidade;
}
