package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PagamentoRevistaDTO {

    private Long id;
    private Long revistaId;
    private Long alunoId;
    private Long professorId;
    private LocalDate dataPagamento;
    private Boolean pago;
    private LocalDate dataVencimento;
    private short parcela;
    private Double valorPago;

    public Boolean isPago() {
        return pago;
    }

}