package stonetek.com.ebdsoft.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ParcelaRequest {

    private Long id;

    private Integer numero;

    private BigDecimal valor;

    private LocalDate dataVencimento;

    private Integer atraso;

    private String status;

    private LocalDate dataPagamento;
}
