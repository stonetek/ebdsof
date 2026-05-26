package stonetek.com.ebdsoft.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import stonetek.com.ebdsoft.model.PagamentoRevista;
import stonetek.com.ebdsoft.repository.PagamentoRevistaRepository;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PagamentoRevistaService {

    private final PagamentoRevistaRepository pagamentoRevistaRepository;

    /**
     * Retorna todos os pagamentos de revista.
     */
    public List<PagamentoRevista> getAllPagamentos() {
        return pagamentoRevistaRepository.findAll();
    }

    /**
     * Retorna um pagamento de revista pelo ID.
     *
     * @param id ID do pagamento.
     * @return PagamentoRevista encontrado.
     * @throws ResourceNotFoundException se o pagamento não for encontrado.
     */
    public PagamentoRevista getPagamentoById(Long id) {
        return pagamentoRevistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PagamentoRevista não encontrado com id " + id));
    }

    /**
     * Cria um novo pagamento de revista.
     *
     * @param pagamentoRevista Objeto PagamentoRevista a ser criado.
     * @return PagamentoRevista criado.
     */
    public PagamentoRevista createPagamento(PagamentoRevista pagamentoRevista) {
        // Aqui você pode adicionar validações adicionais, se necessário
        return pagamentoRevistaRepository.save(pagamentoRevista);
    }

    /**
     * Atualiza um pagamento de revista existente.
     *
     * @param id                ID do pagamento a ser atualizado.
     * @param pagamentoDetails  Detalhes do pagamento para atualização.
     * @return PagamentoRevista atualizado.
     * @throws ResourceNotFoundException se o pagamento não for encontrado.
     */
    public PagamentoRevista updatePagamento(Long id, PagamentoRevista pagamentoDetails) {
        PagamentoRevista pagamento = getPagamentoById(id);
        pagamento.setRevista(pagamentoDetails.getRevista());
        pagamento.setAluno(pagamentoDetails.getAluno());
        pagamento.setProfessor(pagamentoDetails.getProfessor());
        pagamento.setDataPagamento(pagamentoDetails.getDataPagamento());
        pagamento.setPago(pagamentoDetails.isPago());
        pagamento.setDataVencimento(pagamentoDetails.getDataVencimento());
        pagamento.setParcela(pagamentoDetails.getParcela());
        pagamento.setValorPago(pagamentoDetails.getValorPago());
        return pagamentoRevistaRepository.save(pagamento);
    }

    /**
     * Deleta um pagamento de revista pelo ID.
     *
     * @param id ID do pagamento a ser deletado.
     * @throws ResourceNotFoundException se o pagamento não for encontrado.
     */
    public void deletePagamento(Long id) {
        PagamentoRevista pagamento = getPagamentoById(id);
        pagamentoRevistaRepository.delete(pagamento);
    }

    /**
     * Retorna pagamentos por revista.
     *
     * @param revistaId ID da revista.
     * @return Lista de PagamentoRevista.
     */
    public List<PagamentoRevista> getPagamentosByRevistaId(Long revistaId) {
        return pagamentoRevistaRepository.findByRevistaId(revistaId);
    }

    /**
     * Retorna pagamentos por aluno.
     *
     * @param alunoId ID do aluno.
     * @return Lista de PagamentoRevista.
     */
    public List<PagamentoRevista> getPagamentosByAlunoId(Long alunoId) {
        return pagamentoRevistaRepository.findByAlunoId(alunoId);
    }

    /**
     * Retorna pagamentos por professor.
     *
     * @param professorId ID do professor.
     * @return Lista de PagamentoRevista.
     */
    public List<PagamentoRevista> getPagamentosByProfessorId(Long professorId) {
        return pagamentoRevistaRepository.findByProfessorId(professorId);
    }

    /**
     * Retorna pagamentos pendentes.
     *
     * @return Lista de PagamentoRevista pendentes.
     */
    public List<PagamentoRevista> getPagamentosPendentes() {
        return pagamentoRevistaRepository.findByPagoFalse();
    }

    /**
     * Retorna pagamentos com data de vencimento antes de uma data específica.
     *
     * @param data Data limite para a data de vencimento.
     * @return Lista de PagamentoRevista.
     */
    public List<PagamentoRevista> getPagamentosVencidosAntesDe(LocalDate data) {
        return pagamentoRevistaRepository.findByDataVencimentoBefore(data);
    }
}
