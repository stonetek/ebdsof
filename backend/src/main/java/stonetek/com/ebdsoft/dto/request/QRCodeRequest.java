package stonetek.com.ebdsoft.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class QRCodeRequest {

    private Long pagamentoId;

    private Integer numeroParcela;

    private String chavePix;

    private BigDecimal valor;

    private String nomeRecebedor;

    private String cidade;

}
