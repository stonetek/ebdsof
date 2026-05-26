package stonetek.com.ebdsoft.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stonetek.com.ebdsoft.dto.request.ParcelaRequest;
import stonetek.com.ebdsoft.dto.response.ParcelaResponse;
import stonetek.com.ebdsoft.service.ParcelaService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/parcelas")
public class ParcelaController {

    private final ParcelaService parcelaService;

    @PostMapping
    public ResponseEntity<ParcelaResponse> criarParcela(@RequestBody @Valid ParcelaRequest request) {
        ParcelaResponse response = parcelaService.criarParcela(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParcelaResponse> editarParcela(
            @PathVariable Long id,
            @RequestBody @Valid ParcelaRequest request) {
        ParcelaResponse response = parcelaService.editarParcela(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ParcelaResponse>> buscarTodas() {
        List<ParcelaResponse> response = parcelaService.buscarTodas();
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirParcela(@PathVariable Long id) {
        parcelaService.excluirParcela(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/pagamento/{pagamentoId}")
    public ResponseEntity<List<ParcelaResponse>> buscarPorPagamento(@PathVariable Long pagamentoId) {
        List<ParcelaResponse> response = parcelaService.buscarPorPagamento(pagamentoId);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/pagar/{parcelaId}")
    public ResponseEntity<String> pagarParcela(@PathVariable Long parcelaId) {
        parcelaService.pagarParcela(parcelaId);
        return ResponseEntity.ok("Parcela paga com sucesso! ID da parcela: " + parcelaId);
    }

    @PostMapping("/por-igreja")
    public ResponseEntity<List<ParcelaResponse>> buscarParcelasPorIgreja(@RequestBody Map<String, Long> request) {
        Long igrejaId = request.get("igrejaId");
        if (igrejaId == null) {
            return ResponseEntity.badRequest().build();
        }
        List<ParcelaResponse> parcelas = parcelaService.buscarParcelasPorIgreja(igrejaId);
        return ResponseEntity.ok(parcelas);
    }


}
