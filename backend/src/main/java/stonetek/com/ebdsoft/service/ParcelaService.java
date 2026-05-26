package stonetek.com.ebdsoft.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stonetek.com.ebdsoft.dto.mapper.ParcelaMapper;
import stonetek.com.ebdsoft.dto.request.ParcelaRequest;
import stonetek.com.ebdsoft.dto.response.PagamentoResponse;
import stonetek.com.ebdsoft.dto.response.ParcelaResponse;
import stonetek.com.ebdsoft.exception.EntityNotFoundException;
import stonetek.com.ebdsoft.model.Pagamento;
import stonetek.com.ebdsoft.model.PagamentoParcela;
import stonetek.com.ebdsoft.model.Parcela;
import stonetek.com.ebdsoft.repository.PagamentoParcelaRepository;
import stonetek.com.ebdsoft.repository.PagamentoRepository;
import stonetek.com.ebdsoft.repository.ParcelaRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParcelaService {

    private final ParcelaRepository parcelaRepository;

    private final PagamentoRepository pagamentoRepository;

    private final PagamentoParcelaRepository pagamentoParcelaRepository;

    private final PagamentoService pagamentoService;


    /**
     * Cria uma nova parcela
     * @param request
     * @return
     */
    public ParcelaResponse criarParcela(ParcelaRequest request) {
        Parcela parcela = ParcelaMapper.converter(request);
        parcela = parcelaRepository.save(parcela);
        return ParcelaMapper.converter(parcela);
    }


    /**
     * Edita parcelas existentes
     * @param id
     * @param request
     * @return
     */
    public ParcelaResponse editarParcela(Long id, ParcelaRequest request) {
        Parcela parcelaExistente = parcelaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parcela não encontrada com o ID " + id));
        ParcelaMapper.copyToProperties(request, parcelaExistente);
        parcelaExistente = parcelaRepository.save(parcelaExistente);
        return ParcelaMapper.converter(parcelaExistente);
    }


    /**
     * Busca todas as percelas do sistema
     * @return
     */

    public List<ParcelaResponse> buscarTodas() {
        List<Parcela> parcelas = parcelaRepository.findAll();
        return ParcelaMapper.converter(parcelas);
    }


    /**
     * Exclui a parcela existente
     * @param id
     */
    public void excluirParcela(Long id) {
        Parcela parcela = parcelaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parcela não encontrada com o ID " + id));
        parcelaRepository.delete(parcela);
    }

    /**
     * Busca as percelas pelo ID do pagamento
     * @param pagamentoId
     * @return
     */
    public List<ParcelaResponse> buscarPorPagamento(Long pagamentoId) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado com o ID " + pagamentoId));

        Set<PagamentoParcela> pagamentoParcelas = pagamento.getPagamentoParcelas();

        if (pagamentoParcelas.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma parcela encontrada para o pagamento ID " + pagamentoId);
        }

        List<Parcela> parcelas = pagamentoParcelas.stream()
                .map(PagamentoParcela::getParcela)
                .collect(Collectors.toList());

        return ParcelaMapper.converter(parcelas);
    }



    @Transactional
    public void pagarParcela(Long parcelaId) {
        // Buscar a relação PagamentoParcela que contém a Parcela
        PagamentoParcela pagamentoParcela = pagamentoParcelaRepository.findByParcelaId(parcelaId)
                .orElseThrow(() -> new EntityNotFoundException("Relacionamento de parcela não encontrado com ID: " + parcelaId));

        Parcela parcela = pagamentoParcela.getParcela();

        // Evita NPE ao verificar status
        if (Objects.equals(parcela.getStatus(), "PAGO")) {
            throw new IllegalStateException("Essa parcela já foi paga.");
        }

        // Atualizar o status da parcela
        parcela.setStatus("PAGO");
        parcela.setDataPagamento(LocalDate.now());

        // Verificar se todas as parcelas do pagamento estão pagas
        Pagamento pagamento = pagamentoParcela.getPagamento();
        boolean todasParcelasPagas = pagamento.getPagamentoParcelas().stream()
                .map(PagamentoParcela::getParcela)  // Obtém as parcelas
                .allMatch(p -> Objects.equals(p.getStatus(), "PAGO"));  // Verifica se todas são "PAGO"

        if (todasParcelasPagas) {
            pagamento.setStatus("CONCLUÍDO");
        }
    }



    public List<ParcelaResponse> buscarParcelasPorIgreja(Long igrejaId) {
        // Buscar pagamentos da igreja
        List<PagamentoParcela> pagamentoParcelas = pagamentoParcelaRepository.findParcelasByIgreja(igrejaId);

        return pagamentoParcelas.stream()
                .map(pp -> {
                    Parcela parcela = pp.getParcela();
                    ParcelaResponse parcelaResponse = new ParcelaResponse();
                    parcelaResponse.setId(parcela.getId());
                    parcelaResponse.setNumero(parcela.getNumero());
                    parcelaResponse.setValor(parcela.getValor());
                    parcelaResponse.setDataVencimento(parcela.getDataVencimento());
                    parcelaResponse.setStatus(parcela.getStatus());
                    return parcelaResponse;
                })
                .collect(Collectors.toList());
    }




}
