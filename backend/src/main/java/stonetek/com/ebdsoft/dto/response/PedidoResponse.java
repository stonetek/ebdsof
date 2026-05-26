package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stonetek.com.ebdsoft.dto.request.RevistaRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class PedidoResponse {

    private Long id;

    private String nome;

    private LocalDate dataPedido;

    private LocalDate dataEntregaPrevista;

    private String descricao;

    private Integer quantidade;

    private BigDecimal total;

    private Long igrejaId;

    private String igrejaNome;

    private String status;

    private String trimestre;

    private String igrejaAreaNome;

    private Set<PedidoRevistaResponse> revistas;
}
