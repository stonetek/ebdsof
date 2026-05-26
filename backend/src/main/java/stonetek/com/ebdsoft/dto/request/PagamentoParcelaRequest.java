package stonetek.com.ebdsoft.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PagamentoParcelaRequest {

    private Long id;

    private Long pagamentoId;

    private Long parcelaId;

}
