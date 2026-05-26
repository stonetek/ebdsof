package stonetek.com.ebdsoft.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class PedidoRevistaRequest {

    private Long id;

    private Long pedidoId;

    private Long revistaId;

    private Long igrejaId;

    private String igrejaNome;

    private String revistaNome;

    private Integer quantidade;

    private String tipo;

    private String formato;

    private BigDecimal preco;

}
