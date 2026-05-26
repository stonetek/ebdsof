package stonetek.com.ebdsoft.dto.mapper;

import stonetek.com.ebdsoft.dto.request.PagamentoParcelaRequest;
import stonetek.com.ebdsoft.dto.response.PagamentoParcelaResponse;
import stonetek.com.ebdsoft.dto.response.ParcelaResponse;
import stonetek.com.ebdsoft.model.Pagamento;
import stonetek.com.ebdsoft.model.PagamentoParcela;
import stonetek.com.ebdsoft.model.Parcela;

public class PagamentoParcelaMapper {
    public static PagamentoParcela toEntity(PagamentoParcelaRequest request) {
        PagamentoParcela pagamentoParcela = new PagamentoParcela();
        Pagamento pagamento = new Pagamento();
        pagamento.setId(request.getPagamentoId());
        pagamentoParcela.setPagamento(pagamento);

        Parcela parcela = new Parcela();
        parcela.setId(request.getParcelaId());
        pagamentoParcela.setParcela(parcela);

        return pagamentoParcela;
    }

    public static PagamentoParcelaResponse toResponse(PagamentoParcela pagamentoParcela) {
        PagamentoParcelaResponse response = new PagamentoParcelaResponse();
        response.setId(pagamentoParcela.getId());
        response.setPagamentoId(pagamentoParcela.getPagamento().getId());
        response.setParcelaId(pagamentoParcela.getParcela().getId());
        return response;
    }

    public static ParcelaResponse toParcelaResponse(PagamentoParcela pagamentoParcela) {
        Parcela parcela = pagamentoParcela.getParcela();
        ParcelaResponse parcelaResponse = new ParcelaResponse();
        parcelaResponse.setId(parcela.getId());
        parcelaResponse.setValor(parcela.getValor());
        parcelaResponse.setDataVencimento(parcela.getDataVencimento());
        return parcelaResponse;
    }
}
