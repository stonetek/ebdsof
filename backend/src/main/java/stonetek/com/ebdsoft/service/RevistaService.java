package stonetek.com.ebdsoft.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import stonetek.com.ebdsoft.model.Revista;
import stonetek.com.ebdsoft.repository.RevistaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RevistaService {

    private final RevistaRepository revistaRepository;

    /**
     * Retorna todas as revistas.
     */
    public List<Revista> getAllRevistas() {
        return revistaRepository.findAll();
    }

    /**
     * Retorna uma revista pelo ID.
     *
     * @param id ID da revista.
     * @return Revista encontrada.
     * @throws ResourceNotFoundException se a revista não for encontrada.
     */
    public Revista getRevistaById(Long id) {
        return revistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Revista não encontrada com id " + id));
    }

    /**
     * Cria uma nova revista.
     *
     * @param revista Objeto Revista a ser criado.
     * @return Revista criada.
     */
    public Revista createRevista(Revista revista) {
        return revistaRepository.save(revista);
    }

    /**
     * Atualiza uma revista existente.
     *
     * @param id           ID da revista a ser atualizada.
     * @param revistaDetails Detalhes da revista para atualização.
     * @return Revista atualizada.
     * @throws ResourceNotFoundException se a revista não for encontrada.
     */
    public Revista updateRevista(Long id, Revista revistaDetails) {
        Revista revista = getRevistaById(id);
        revista.setNome(revistaDetails.getNome());
        revista.setFormato(revistaDetails.getFormato());
        revista.setTipo(revistaDetails.getTipo());
        revista.setPreco(revistaDetails.getPreco());
        //revista.setPedido(revistaDetails.getPedido());
        //revista.setQuantidade(revistaDetails.getQuantidade());
        return revistaRepository.save(revista);
    }

    /**
     * Deleta uma revista pelo ID.
     *
     * @param id ID da revista a ser deletada.
     * @throws ResourceNotFoundException se a revista não for encontrada.
     */
    public void deleteRevista(Long id) {
        Revista revista = getRevistaById(id);
        revistaRepository.delete(revista);
    }

    /**
     * Retorna revistas por tipo.
     *
     * @param tipo Tipo da revista (ALUNO ou PROFESSOR).
     * @return Lista de revistas do tipo especificado.
     */
    public List<Revista> getRevistasByTipo(String tipo) {
        return revistaRepository.findByTipo(tipo);
    }

    /**
     * Retorna revistas cujo nome contenha a string especificada.
     *
     * @param nome Parte do nome a ser pesquisada.
     * @return Lista de revistas cujo nome contém a string especificada.
     */
    public List<Revista> getRevistasByNomeContaining(String nome) {
        return revistaRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Retorna revistas por formato.
     *
     * @param formato Formato da revista.
     * @return Lista de revistas com o formato especificado.
     */
    public List<Revista> getRevistasByFormato(String formato) {
        return revistaRepository.findByFormato(formato);
    }

    /**
     * Retorna revistas associadas a um pedido específico.
     *
     * @param pedidoId ID do pedido.
     * @return Lista de revistas associadas ao pedido.
     */
   /* public List<Revista> getRevistasByPedidoId(Long pedidoId) {
        return revistaRepository.findByPedidoId(pedidoId);
    }*/


    // personalizados


}
