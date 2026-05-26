package stonetek.com.ebdsoft.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stonetek.com.ebdsoft.dto.dtosespecificos.RevistaDTO;
import stonetek.com.ebdsoft.dto.request.IgrejaRequest;
import stonetek.com.ebdsoft.model.Pedido;
import stonetek.com.ebdsoft.model.Revista;
import stonetek.com.ebdsoft.service.PedidoService;
import stonetek.com.ebdsoft.service.RevistaService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/revistas")
public class RevistaController {

    private final RevistaService revistaService;

    private final PedidoService pedidoService;


    /**
     * GET /api/revistas
     * Retorna todas as revistas.
     */
    @GetMapping
    public ResponseEntity<List<Revista>> getAllRevistas() {
        List<Revista> revistas = revistaService.getAllRevistas();
        return new ResponseEntity<>(revistas, HttpStatus.OK);
    }

    /**
     * GET /api/revistas/{id}
     * Retorna uma revista pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Revista> getRevistaById(@PathVariable Long id) {
        Revista revista = revistaService.getRevistaById(id);
        return new ResponseEntity<>(revista, HttpStatus.OK);
    }

    /**
     * POST /api/revistas
     * Cria uma nova revista.
     */
    @PostMapping
    public ResponseEntity<Revista> createRevista(@RequestBody Revista revista) {
        Revista novaRevista = revistaService.createRevista(revista);
        return new ResponseEntity<>(novaRevista, HttpStatus.CREATED);
    }

    /**
     * PUT /api/revistas/{id}
     * Atualiza uma revista existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Revista> updateRevista(@PathVariable Long id, @RequestBody Revista revistaDetails) {
        Revista revistaAtualizada = revistaService.updateRevista(id, revistaDetails);
        return new ResponseEntity<>(revistaAtualizada, HttpStatus.OK);
    }

    /**
     * DELETE /api/revistas/{id}
     * Deleta uma revista pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRevista(@PathVariable Long id) {
        revistaService.deleteRevista(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * GET /api/revistas/tipo/{tipo}
     * Retorna revistas por tipo.
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Revista>> getRevistasByTipo(@PathVariable String tipo) {
        List<Revista> revistas = revistaService.getRevistasByTipo(tipo);
        return new ResponseEntity<>(revistas, HttpStatus.OK);
    }

    /**
     * GET /api/revistas/nome/{nome}
     * Retorna revistas cujo nome contenha a string especificada.
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Revista>> getRevistasByNome(@PathVariable String nome) {
        List<Revista> revistas = revistaService.getRevistasByNomeContaining(nome);
        return new ResponseEntity<>(revistas, HttpStatus.OK);
    }

    /**
     * GET /api/revistas/formato/{formato}
     * Retorna revistas por formato.
     */
    @GetMapping("/formato/{formato}")
    public ResponseEntity<List<Revista>> getRevistasByFormato(@PathVariable String formato) {
        List<Revista> revistas = revistaService.getRevistasByFormato(formato);
        return new ResponseEntity<>(revistas, HttpStatus.OK);
    }

    /**
     * GET /api/revistas/pedido/{pedidoId}
     * Retorna revistas associadas a um pedido específico.
     */
    /*@GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<Revista>> getRevistasByPedidoId(@PathVariable Long pedidoId) {
        List<Revista> revistas = revistaService.getRevistasByPedidoId(pedidoId);
        return new ResponseEntity<>(revistas, HttpStatus.OK);
    }*/




    //alternativa de endpoint usando RevistaDTO


    /**
     * GET /api/revistas
     * Retorna todas as revistas como DTOs.
     */
    @GetMapping("/obtertodas")
    public ResponseEntity<List<RevistaDTO>> getAllRevistasDTO() {
        List<Revista> revistas = revistaService.getAllRevistas();
        List<RevistaDTO> revistaDTOs = revistas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(revistaDTOs, HttpStatus.OK);
    }

    /**
     * GET /api/revistas/{id}
     * Retorna uma revista pelo ID como DTO.
     */
    @GetMapping("/unidade/{id}")
    public ResponseEntity<RevistaDTO> getRevistaByIds(@PathVariable Long id) {
        Revista revista = revistaService.getRevistaById(id);
        RevistaDTO revistaDTO = convertToDTO(revista);
        return new ResponseEntity<>(revistaDTO, HttpStatus.OK);
    }

    /**
     * POST /api/revistas
     * Cria uma nova revista a partir de um DTO.
     */
    @PostMapping("/novarevista")
    public ResponseEntity<RevistaDTO> createRevista(@Valid @RequestBody RevistaDTO revistaDTO) {
        Revista revista = convertToEntity(revistaDTO);
        Revista novaRevista = revistaService.createRevista(revista);
        RevistaDTO novaRevistaDTO = convertToDTO(novaRevista);
        return new ResponseEntity<>(novaRevistaDTO, HttpStatus.CREATED);
    }

    /**
     * PUT /api/revistas/{id}
     * Atualiza uma revista existente a partir de um DTO.
     */
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<RevistaDTO> updateRevista(@PathVariable Long id, @Valid @RequestBody RevistaDTO revistaDTO) {
        Revista revistaDetails = convertToEntity(revistaDTO);
        Revista revistaAtualizada = revistaService.updateRevista(id, revistaDetails);
        RevistaDTO revistaAtualizadaDTO = convertToDTO(revistaAtualizada);
        return new ResponseEntity<>(revistaAtualizadaDTO, HttpStatus.OK);
    }

    /**
     * DELETE /api/revistas/{id}
     * Deleta uma revista pelo ID.
     */
    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Void> deleteRevistas(@PathVariable Long id) {
        revistaService.deleteRevista(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * GET /api/revistas/tipo/{tipo}
     * Retorna revistas por tipo como DTOs.
     */
    @GetMapping("/tipoderevista/{tipo}")
    public ResponseEntity<List<RevistaDTO>> getRevistasByTipos(@PathVariable String tipo) {
        List<Revista> revistas = revistaService.getRevistasByTipo(tipo);
        List<RevistaDTO> revistaDTOs = revistas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(revistaDTOs, HttpStatus.OK);
    }

    /**
     * GET /api/revistas/nome/{nome}
     * Retorna revistas cujo nome contenha a string especificada como DTOs.
     */
    @GetMapping("/nomedarevista/{nome}")
    public ResponseEntity<List<RevistaDTO>> getRevistasByNomes(@PathVariable String nome) {
        List<Revista> revistas = revistaService.getRevistasByNomeContaining(nome);
        List<RevistaDTO> revistaDTOs = revistas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(revistaDTOs, HttpStatus.OK);
    }

    /**
     * GET /api/revistas/formato/{formato}
     * Retorna revistas por formato como DTOs.
     */
    @GetMapping("/formatodarevista/{formato}")
    public ResponseEntity<List<RevistaDTO>> getRevistasByFormatos(@PathVariable String formato) {
        List<Revista> revistas = revistaService.getRevistasByFormato(formato);
        List<RevistaDTO> revistaDTOs = revistas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(revistaDTOs, HttpStatus.OK);
    }

    /**
     * GET /api/revistas/pedido/{pedidoId}
     * Retorna revistas associadas a um pedido específico como DTOs.
     */
    /*@GetMapping("/pedidoderevistas/{pedidoId}")
    public ResponseEntity<List<RevistaDTO>> getRevistasByPedidoIds(@PathVariable Long pedidoId) {
        List<Revista> revistas = revistaService.getRevistasByPedidoId(pedidoId);
        List<RevistaDTO> revistaDTOs = revistas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(revistaDTOs, HttpStatus.OK);
    }*/

    /**
     * Converte uma entidade Revista para RevistaDTO.
     */
    private RevistaDTO convertToDTO(Revista revista) {
        RevistaDTO dto = new RevistaDTO();
        dto.setId(revista.getId());
        dto.setNome(revista.getNome());
        dto.setFormato(revista.getFormato());
        dto.setTipo(revista.getTipo());
        dto.setPreco(revista.getPreco().compareTo(BigDecimal.ZERO) == 0 ? null : revista.getPreco());
        //dto.setQuantidade(revista.getQuantidade());
        /*if (revista.getPedido() != null) {
            dto.setPedidoId(revista.getPedido().getId());
        }*/
        return dto;
    }

    /**
     * Converte um RevistaDTO para entidade Revista.
     */
    private Revista convertToEntity(RevistaDTO dto) {
        Revista revista = new Revista();
        revista.setNome(dto.getNome());
        revista.setFormato(dto.getFormato());
        revista.setTipo(dto.getTipo());
        revista.setPreco(dto.getPreco());
        dto.setPreco(revista.getPreco().compareTo(BigDecimal.ZERO) == 0 ? null : revista.getPreco());
        //revista.setQuantidade(dto.getQuantidade());
        /*if (dto.getPedidoId() != null) {
            Pedido pedido = pedidoService.getPedidoById(dto.getPedidoId());
            revista.setPedido(pedido);
        }*/
        return revista;
    }


    //personalizados

   /* @PostMapping("/por-igreja")
    public ResponseEntity<List<Revista>> getRevistasPorIgreja(@RequestBody IgrejaRequest request) {
        Long igrejaId = request.getId();
        List<Revista> revistas = revistaService.buscarRevistasPorIgreja(igrejaId);
        if (revistas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(revistas);
    } */


}
