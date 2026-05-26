package stonetek.com.ebdsoft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stonetek.com.ebdsoft.dto.mapper.PedidoMapper;
import stonetek.com.ebdsoft.dto.request.IgrejaRequest;
import stonetek.com.ebdsoft.dto.request.PedidoRequest;
import stonetek.com.ebdsoft.dto.response.PedidoResponse;
import stonetek.com.ebdsoft.model.Pedido;
import stonetek.com.ebdsoft.model.Revista;
import stonetek.com.ebdsoft.repository.PedidoRepository;
import stonetek.com.ebdsoft.repository.RevistaRepository;
import stonetek.com.ebdsoft.service.PedidoService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    private final PedidoRepository pedidoRepository;

    private final RevistaRepository revistaRepository;

    /**
     * GET /api/pedidos
     * Retorna todos os pedidos.
     */

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> obterTodosPedidos() {
        List<PedidoResponse> pedidos = pedidoService.obterTodosPedidosComRevistas();
        return ResponseEntity.ok(pedidos);
    }



    /**
     * GET /api/pedidos/{id}
     * Retorna um pedido pelo ID.
     */

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obterPedidoPorId(@PathVariable Long id) {
        PedidoResponse pedido = pedidoService.obterPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }


    /**
     * POST /api/pedidos
     * Cria um novo pedido.
     */
    @PostMapping
    public ResponseEntity<PedidoResponse> createPedido(@RequestBody @Valid PedidoRequest pedidoRequest) {
        PedidoResponse createdPedido = pedidoService.createPedido(pedidoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPedido);
    }

    /**
     * PUT /api/pedidos/{id}
     * Atualiza um pedido existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable Long id, @RequestBody Pedido pedidoDetails) {
        Pedido pedidoAtualizado = pedidoService.updatePedido(id, pedidoDetails);
        return new ResponseEntity<>(pedidoAtualizado, HttpStatus.OK);
    }

    /**
     * DELETE /api/pedidos/{id}
     * Deleta um pedido pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        pedidoService.deletePedido(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * GET /api/pedidos/trimestre/{trimestre}
     * Retorna pedidos por trimestre.
     */
    @GetMapping("/trimestre/{trimestre}")
    public ResponseEntity<List<Pedido>> getPedidosByTrimestre(@PathVariable short trimestre) {
        List<Pedido> pedidos = pedidoRepository.findByTrimestre(trimestre);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    /**
     * GET /api/pedidos/status/{status}
     * Retorna pedidos por status.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Pedido>> getPedidosByStatus(@PathVariable String status) {
        List<Pedido> pedidos = pedidoRepository.findByStatus(status);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    //personalizados

    @PostMapping("/por-igreja")
    public ResponseEntity<List<Pedido>> getPedidosPorIgreja(@RequestBody IgrejaRequest request) {
        Long igrejaId = request.getId();
        List<Pedido> pedidos = pedidoService.buscarPedidosPorIgreja(igrejaId);
        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pedidos);
    }


    @PostMapping("/{pedidoId}/revistas")
    public ResponseEntity<Pedido> addRevista(@PathVariable Long pedidoId, @RequestBody Revista revista) {
        Pedido pedidoAtualizado = pedidoService.addRevistaToPedido(pedidoId, revista);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @PutMapping("/pedido-revista/{id}")
    public ResponseEntity<PedidoResponse> atualizarPedido(@PathVariable Long id, @RequestBody PedidoRequest pedidoRequest) {
        PedidoResponse pedidoAtualizado = pedidoService.atualizarPedido(id, pedidoRequest);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @GetMapping("/por-igreja/{igrejaId}")
    public ResponseEntity<List<PedidoResponse>> getPedidosByIgrejaId(@PathVariable Long igrejaId) {
        List<PedidoResponse> pedidos = pedidoService.findPedidosByIgrejaId(igrejaId);
        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pedidos);
    }

}
