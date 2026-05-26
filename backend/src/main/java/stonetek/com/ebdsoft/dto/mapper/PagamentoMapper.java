package stonetek.com.ebdsoft.dto.mapper;

import org.modelmapper.ModelMapper;
import stonetek.com.ebdsoft.dto.request.PagamentoRequest;
import stonetek.com.ebdsoft.dto.response.PagamentoResponse;
import stonetek.com.ebdsoft.dto.response.ParcelaResponse;
import stonetek.com.ebdsoft.model.Pagamento;
import stonetek.com.ebdsoft.model.PagamentoParcela;
import stonetek.com.ebdsoft.model.Parcela;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PagamentoMapper {
    private final static ModelMapper mapper = new ModelMapper();

    public static PagamentoResponse converter(Pagamento pagamento) {
        return mapper.map(pagamento, PagamentoResponse.class);
    }

    public static Pagamento converter(PagamentoRequest request) {
        return mapper.map(request, Pagamento.class);
    }

    public static Pagamento converter(PagamentoResponse response) {
        return mapper.map(response, Pagamento.class);
    }

    public static List<PagamentoResponse> converter(List<Pagamento> pagamentos) {
        return pagamentos.stream().map(PagamentoMapper::converter).collect(Collectors.toList());
    }

    public static void copyToProperties(PagamentoRequest request, Pagamento pagamento) {
        mapper.map(request, pagamento);
    }


    public static PagamentoResponse converter(Pagamento pagamento, List<PagamentoParcela> pagamentoParcelas) {
        PagamentoResponse response = new PagamentoResponse();
        response.setId(pagamento.getId());
        response.setValorTotal(pagamento.getValorTotal());

        // Mapear parcelas usando PagamentoParcelaMapper
        Set<ParcelaResponse> parcelasSet = pagamentoParcelas.stream()
                .map(pp -> {
                    Parcela parcela = pp.getParcela();
                    ParcelaResponse parcelaResponse = new ParcelaResponse();
                    parcelaResponse.setId(parcela.getId());
                    parcelaResponse.setValor(parcela.getValor());
                    parcelaResponse.setDataVencimento(parcela.getDataVencimento());
                    return parcelaResponse;
                })
                .collect(Collectors.toSet());

        response.setParcelasSet(parcelasSet);
        return response;
    }
}
