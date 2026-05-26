package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class RevistaResponse {

    private Long id;

    private String nome;

    private String formato;

    private String tipo;

    private BigDecimal preco;

    private Integer quantidade;
}
