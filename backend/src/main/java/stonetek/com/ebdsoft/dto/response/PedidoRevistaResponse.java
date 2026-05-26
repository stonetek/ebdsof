package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stonetek.com.ebdsoft.model.PedidoRevista;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PedidoRevistaResponse {

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
