package stonetek.com.ebdsoft.dto.mapper;

import org.modelmapper.ModelMapper;
import stonetek.com.ebdsoft.dto.request.ParcelaRequest;
import stonetek.com.ebdsoft.dto.response.ParcelaResponse;
import stonetek.com.ebdsoft.model.PagamentoParcela;
import stonetek.com.ebdsoft.model.Parcela;
import stonetek.com.ebdsoft.model.PedidoPagamento;
import stonetek.com.ebdsoft.model.Igreja;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ParcelaMapper {

    private final static ModelMapper mapper = new ModelMapper();


    public static ParcelaResponse converter(Parcela parcela) {
        return mapper.map(parcela, ParcelaResponse.class);
    }

    public static Parcela converter(ParcelaRequest request) {
        return mapper.map(request, Parcela.class);
    }

    public static Parcela converter(ParcelaResponse response) {
        return mapper.map(response, Parcela.class);
    }

    public static List<ParcelaResponse> converter(List<Parcela> parcelas) {
        return parcelas.stream().map(parcela -> {
            ParcelaResponse response = converter(parcela); // Usa o ModelMapper

            // Agora, busca o nome da igreja:
            String igrejaNome = parcela.getPagamentoParcelas().stream()
                    .map(PagamentoParcela::getPagamento)
                    .filter(Objects::nonNull)
                    .flatMap(pag -> pag.getPedidoPagamentos().stream())
                    .map(PedidoPagamento::getIgreja)
                    .filter(Objects::nonNull)
                    .map(Igreja::getNome)
                    .findFirst()
                    .orElse(null);

            response.setIgrejaNome(igrejaNome);

            return response;
        }).collect(Collectors.toList());
    }


    public static void copyToProperties(ParcelaRequest request, Parcela parcela) {
        mapper.map(request, parcela);
    }

    public static ParcelaResponse converters(Parcela parcela) {
        ParcelaResponse response = new ParcelaResponse();
        response.setId(parcela.getId());
        response.setNumero(parcela.getNumero());
        response.setValor(parcela.getValor());
        response.setDataVencimento(parcela.getDataVencimento());
        response.setStatus(parcela.getStatus());
        return response;
    }
}
