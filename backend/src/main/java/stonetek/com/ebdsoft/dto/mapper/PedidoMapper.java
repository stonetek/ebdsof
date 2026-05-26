package stonetek.com.ebdsoft.dto.mapper;

import org.modelmapper.ModelMapper;
import stonetek.com.ebdsoft.dto.request.PedidoRequest;
import stonetek.com.ebdsoft.dto.response.PedidoResponse;
import stonetek.com.ebdsoft.dto.response.PedidoRevistaResponse;
import stonetek.com.ebdsoft.dto.response.RevistaResponse;
import stonetek.com.ebdsoft.model.Pedido;
import stonetek.com.ebdsoft.model.PedidoRevista;
import stonetek.com.ebdsoft.model.Revista;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PedidoMapper {

    private final static ModelMapper mapper = new ModelMapper();

    public static PedidoResponse converter(Pedido pedido) {
        return mapper.map(pedido, PedidoResponse.class);
    }

    public static Pedido converter(PedidoRequest request) {
        return mapper.map(request, Pedido.class);
    }

    public static Pedido converter(PedidoResponse response) {
        return mapper.map(response, Pedido.class);
    }

    public static List<PedidoResponse> converter(List<Pedido> pedidos) {
        return pedidos.stream().map(PedidoMapper::converter).collect(Collectors.toList());
    }

    public static void copyToProperties(PedidoRequest request, Pedido pedido) {
        mapper.map(request, pedido);
    }

    public static PedidoResponse toResponse(Pedido pedido) {
        PedidoResponse response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setNome(pedido.getNome());
        response.setDataPedido(pedido.getDataPedido());
        response.setDataEntregaPrevista(pedido.getDataEntregaPrevista());
        response.setDescricao(pedido.getDescricao());
        response.setQuantidade(pedido.getPedidoRevistas().stream()
                .mapToInt(PedidoRevista::getQuantidade)
                .sum());
        response.setTotal(pedido.getTotal());
        response.setIgrejaId(pedido.getIgreja().getId());
        response.setIgrejaNome(pedido.getIgreja().getNome());
        response.setIgrejaAreaNome(pedido.getIgreja().getArea().getDescricao());
        response.setStatus(pedido.getStatus());
        response.setTrimestre(pedido.getTrimestre());

        // Mapear PedidoRevista para PedidoRevistaResponse
        Set<PedidoRevistaResponse> revistasResponse = pedido.getPedidoRevistas().stream()
                .map(pedidoRevista -> {
                    PedidoRevistaResponse revistaResponse = new PedidoRevistaResponse();
                    revistaResponse.setId(pedidoRevista.getId());
                    revistaResponse.setPedidoId(pedidoRevista.getPedido().getId());
                    revistaResponse.setRevistaId(pedidoRevista.getRevista().getId());
                    revistaResponse.setRevistaNome(pedidoRevista.getRevista().getNome());
                    revistaResponse.setQuantidade(pedidoRevista.getQuantidade());
                    revistaResponse.setTipo(pedidoRevista.getRevista().getTipo());
                    revistaResponse.setFormato(pedidoRevista.getRevista().getFormato());
                    revistaResponse.setPreco(pedidoRevista.getRevista().getPreco());
                    return revistaResponse;
                })
                .collect(Collectors.toSet());
        response.setRevistas(revistasResponse);

        return response;
    }


    public static PedidoRevistaResponse toRevistaResponse(PedidoRevista pedidoRevista) {
        PedidoRevistaResponse response = new PedidoRevistaResponse();
        response.setId(pedidoRevista.getId());
        response.setPedidoId(pedidoRevista.getPedido().getId());
        response.setRevistaId(pedidoRevista.getRevista().getId());
        response.setRevistaNome(pedidoRevista.getRevista().getNome());
        response.setQuantidade(pedidoRevista.getQuantidade());
        response.setTipo(pedidoRevista.getRevista().getTipo());
        response.setFormato(pedidoRevista.getRevista().getFormato());
        response.setPreco(pedidoRevista.getRevista().getPreco());
        return response;
    }

}
