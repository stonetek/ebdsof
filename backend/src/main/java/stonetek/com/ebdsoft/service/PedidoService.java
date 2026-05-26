package stonetek.com.ebdsoft.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import stonetek.com.ebdsoft.dto.mapper.PagamentoMapper;
import stonetek.com.ebdsoft.dto.mapper.PagamentoParcelaMapper;
import stonetek.com.ebdsoft.dto.mapper.PedidoMapper;
import stonetek.com.ebdsoft.dto.request.PagamentoParcelaRequest;
import stonetek.com.ebdsoft.dto.request.PagamentoRequest;
import stonetek.com.ebdsoft.dto.request.PedidoRequest;
import stonetek.com.ebdsoft.dto.request.PedidoRevistaRequest;
import stonetek.com.ebdsoft.dto.response.IgrejaResponse;
import stonetek.com.ebdsoft.dto.response.PagamentoResponse;
import stonetek.com.ebdsoft.dto.response.PedidoResponse;
import stonetek.com.ebdsoft.dto.response.PedidoRevistaResponse;
import stonetek.com.ebdsoft.exception.EntityNotFoundException;
import stonetek.com.ebdsoft.model.*;
import stonetek.com.ebdsoft.repository.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final PedidoRevistaRepository pedidoRevistaRepository;

    private final RevistaRepository revistaRepository;

    private final IgrejaRepository  igrejaRepository;

    private final PagamentoRepository pagamentoRepository;

    private final PedidoPagamentoRepository pedidoPagamentoRepository;

    private final ParcelaRepository parcelaRepository;

    private final PagamentoParcelaRepository pagamentoParcelaRepository;


    @Transactional
    public PedidoResponse createPedido(PedidoRequest pedidoRequest) {
        Pedido pedido = new Pedido();
        pedido.setNome(pedidoRequest.getNome());
        pedido.setDataPedido(pedidoRequest.getDataPedido());
        pedido.setDataEntregaPrevista(pedidoRequest.getDataEntregaPrevista());
        pedido.setDescricao(pedidoRequest.getDescricao());
        pedido.setTotal(pedidoRequest.getTotal());
        pedido.setStatus(pedidoRequest.getStatus());
        pedido.setTrimestre(pedidoRequest.getTrimestre());
        Igreja igreja = igrejaRepository.findById(pedidoRequest.getIgrejaId())
                .orElseThrow(() -> new EntityNotFoundException("Igreja não encontrada com ID: " + pedidoRequest.getIgrejaId()));
        pedido.setIgreja(igreja);
        pedido = pedidoRepository.save(pedido);
        if (pedidoRequest.getRevistas() != null && !pedidoRequest.getRevistas().isEmpty()) {
            for (PedidoRevistaRequest revistaRequest : pedidoRequest.getRevistas()) {
                Revista revista = revistaRepository.findRevistaById(revistaRequest.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Revista não encontrada com ID: " + revistaRequest.getId()));

                PedidoRevista pedidoRevista = new PedidoRevista();
                pedidoRevista.setPedido(pedido);
                pedidoRevista.setRevista(revista);
                pedidoRevista.setQuantidade(revistaRequest.getQuantidade());

                pedidoRevistaRepository.save(pedidoRevista);
                pedido.getPedidoRevistas().add(pedidoRevista);
            }
            pedido = pedidoRepository.save(pedido);
        }
        criarPagamentoParaPedido(pedido.getId());
        return PedidoMapper.toResponse(pedido);
    }



    public List<PedidoResponse> obterTodosPedidosComRevistas() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        return pedidos.stream()
                .map(pedido -> {
                    Set<PedidoRevistaResponse> revistaResponses = pedidoRevistaRepository.findByPedidoId(pedido.getId())
                            .stream()
                            .map(this::mapToPedidoRevistaResponse)
                            .collect(Collectors.toSet());

                    return mapToPedidoResponse(pedido, revistaResponses);
                })
                .collect(Collectors.toList());
    }


    public PedidoResponse obterPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com id " + id));

        Set<PedidoRevistaResponse> revistaResponses = pedidoRevistaRepository.findByPedidoId(pedido.getId())
                .stream()
                .map(this::mapToPedidoRevistaResponse)
                .collect(Collectors.toSet());

        return mapToPedidoResponse(pedido, revistaResponses);
    }

    private PedidoRevistaResponse mapToPedidoRevistaResponse(PedidoRevista pedidoRevista) {
        PedidoRevistaResponse response = new PedidoRevistaResponse();
        response.setId(pedidoRevista.getId());
        response.setPedidoId(pedidoRevista.getPedido().getId());
        response.setIgrejaId(pedidoRevista.getPedido().getIgreja().getId());
        response.setIgrejaNome(pedidoRevista.getPedido().getIgreja().getNome());
        response.setRevistaNome(pedidoRevista.getRevista().getNome());
        response.setRevistaNome(String.valueOf(pedidoRevista.getRevista().getId()));
        response.setQuantidade(pedidoRevista.getQuantidade());
        response.setTipo(pedidoRevista.getRevista().getTipo());
        response.setFormato(pedidoRevista.getRevista().getFormato());
        response.setRevistaId(pedidoRevista.getRevista().getId());
        response.setPreco(pedidoRevista.getRevista().getPreco());
        return response;
    }

    private PedidoResponse mapToPedidoResponse(Pedido pedido, Set<PedidoRevistaResponse> revistaResponses) {
        PedidoResponse response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setNome(pedido.getNome());
        response.setDataPedido(pedido.getDataPedido());
        response.setDataEntregaPrevista(pedido.getDataEntregaPrevista());
        response.setDescricao(pedido.getDescricao());
        response.setQuantidade(pedido.getPedidoRevistas().stream().mapToInt(PedidoRevista::getQuantidade).sum());
        response.setTotal(pedido.getTotal());
        response.setIgrejaId(pedido.getIgreja().getId());
        response.setIgrejaNome(pedido.getIgreja().getNome());
        response.setStatus(pedido.getStatus());
        response.setTrimestre(pedido.getTrimestre());
        response.setRevistas(revistaResponses);
        response.setIgrejaAreaNome(String.valueOf(pedido.getIgreja().getArea()));
        return response;
    }

    public PedidoResponse atualizarPedido(Long id, PedidoRequest pedidoRequest) {
        if (id == null) {
            throw new IllegalArgumentException("The given id must not be null!");
        }

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com id " + id));

        pedido.setNome(pedidoRequest.getNome());
        pedido.setDataPedido(pedidoRequest.getDataPedido());
        pedido.setDataEntregaPrevista(pedidoRequest.getDataEntregaPrevista());
        pedido.setDescricao(pedidoRequest.getDescricao());
        pedido.setTotal(pedidoRequest.getTotal());
        pedido.setStatus(pedidoRequest.getStatus());
        pedido.setTrimestre(pedidoRequest.getTrimestre());

        Pedido pedidoAtualizado = pedidoRepository.save(pedido);

        Set<PedidoRevistaResponse> revistaResponses = pedido.getPedidoRevistas().stream()
                .map(this::mapToPedidoRevistaResponse)
                .collect(Collectors.toSet());

        return mapToPedidoResponse(pedidoAtualizado, revistaResponses);
    }


    public Pedido getPedidoById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com id " + id));
    }

    public Pedido updatePedido(Long id, Pedido pedidoDetails) {
        Pedido pedido = getPedidoById(id);
        pedido.setTrimestre(pedidoDetails.getTrimestre());
        pedido.setNome(pedidoDetails.getNome());
        pedido.setDataPedido(pedidoDetails.getDataPedido());
        pedido.setDataEntregaPrevista(pedidoDetails.getDataEntregaPrevista());
        pedido.setStatus(pedidoDetails.getStatus());
        pedido.setIgreja(pedidoDetails.getIgreja());
        pedido.setDescricao(pedidoDetails.getDescricao());
        return pedidoRepository.save(pedido);
    }

    public void deletePedido(Long id) {
        Pedido pedido = getPedidoById(id);
        pedidoRepository.delete(pedido);
    }


    public Pedido addRevistaToPedido(Long pedidoId, Revista revista) {
        Pedido pedido = getPedidoById(pedidoId);
        //revista.setPedido(pedido);
        revistaRepository.save(revista);
        return pedido;
    }

    public Pedido removeRevistaFromPedido(Long pedidoId, Long revistaId) {
        Pedido pedido = getPedidoById(pedidoId);
        Revista revista = revistaRepository.findById(revistaId)
                .orElseThrow(() -> new ResourceNotFoundException("Revista não encontrada com id " + revistaId));
        //revista.setPedido(null);
        revistaRepository.save(revista);
        return pedido;
    }

    public List<Pedido> buscarPedidosPorIgreja(Long igrejaId) {
        return pedidoRepository.findByIgreja_Id(igrejaId);
    }


    //Pedido e Pagamento

    @Transactional
    public void criarPagamentoParaPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + pedidoId));

        BigDecimal total = pedido.getTotal();
        if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O pedido deve ter um valor total maior que zero.");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setValorTotal(total);
        Integer parcelas = 2; // Definindo o número de parcelas como 2
        pagamento.setParcelas(parcelas);

        BigDecimal valorParcela = total.divide(BigDecimal.valueOf(parcelas), RoundingMode.HALF_UP);

        pagamento.setStatus("PENDENTE");
        pagamentoRepository.save(pagamento);

        // Criar e salvar as parcelas
        LocalDate dataCriacaoPedido = pedido.getDataPedido();

        Parcela parcela1 = new Parcela();
        parcela1.setNumero(1);
        parcela1.setValor(valorParcela);
        parcela1.setDataVencimento(dataCriacaoPedido.plusDays(30)); // 30 dias após a data de criação do pedido
        parcela1.setAtraso(0);
        parcela1 = parcelaRepository.save(parcela1);

        Parcela parcela2 = new Parcela();
        parcela2.setNumero(2);
        parcela2.setValor(valorParcela);
        parcela2.setDataVencimento(dataCriacaoPedido.plusDays(60)); // 60 dias após a data de criação do pedido
        parcela2.setAtraso(0);
        parcela2 = parcelaRepository.save(parcela2);

        // Relacionar as parcelas ao pagamento através de PagamentoParcela
        PagamentoParcelaRequest pagamentoParcelaRequest1 = new PagamentoParcelaRequest();
        pagamentoParcelaRequest1.setPagamentoId(pagamento.getId());
        pagamentoParcelaRequest1.setParcelaId(parcela1.getId());

        PagamentoParcela pagamentoParcela1 = PagamentoParcelaMapper.toEntity(pagamentoParcelaRequest1);
        pagamentoParcelaRepository.save(pagamentoParcela1);

        PagamentoParcelaRequest pagamentoParcelaRequest2 = new PagamentoParcelaRequest();
        pagamentoParcelaRequest2.setPagamentoId(pagamento.getId());
        pagamentoParcelaRequest2.setParcelaId(parcela2.getId());

        PagamentoParcela pagamentoParcela2 = PagamentoParcelaMapper.toEntity(pagamentoParcelaRequest2);
        pagamentoParcelaRepository.save(pagamentoParcela2);

        // Adicionar as relações de pagamento e parcela ao pagamento
        pagamento.getPagamentoParcelas().add(pagamentoParcela1);
        pagamento.getPagamentoParcelas().add(pagamentoParcela2);

        // Atualizar o pagamento com as parcelas relacionadas
        pagamentoRepository.save(pagamento);

        Long igrejaId = pedido.getIgreja().getId();
        Igreja igreja = igrejaRepository.findById(igrejaId)
                .orElseThrow(() -> new EntityNotFoundException("Igreja não encontrada com ID: " + igrejaId));

        PedidoPagamento pedidoPagamento = new PedidoPagamento();
        pedidoPagamento.setPedido(pedido);
        pedidoPagamento.setPagamento(pagamento);
        pedidoPagamento.setIgreja(igreja);

        pedidoPagamentoRepository.save(pedidoPagamento);
    }

    public List<PedidoResponse> findPedidosByIgrejaId(Long igrejaId) {
        List<Pedido> pedidos = pedidoRepository.findByIgrejaId(igrejaId);

        return pedidos.stream()
                .map(pedido -> {
                    Set<PedidoRevistaResponse> revistaResponses = pedidoRevistaRepository.findByPedidoId(pedido.getId())
                            .stream()
                            .map(this::mapToPedidoRevistaResponse)
                            .collect(Collectors.toSet());

                    return mapToPedidoResponse(pedido, revistaResponses);
                })
                .collect(Collectors.toList());
    }

}
