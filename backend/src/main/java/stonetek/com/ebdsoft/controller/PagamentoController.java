package stonetek.com.ebdsoft.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stonetek.com.ebdsoft.dto.request.PagamentoRequest;
import stonetek.com.ebdsoft.dto.request.QRCodeRequest;
import stonetek.com.ebdsoft.dto.response.PagamentoResponse;
import stonetek.com.ebdsoft.exception.EntityNotFoundException;
import stonetek.com.ebdsoft.service.PagamentoService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;


    @GetMapping
    public ResponseEntity<List<PagamentoResponse>> buscarTodos() {
        List<PagamentoResponse> pagamentos = pagamentoService.buscarTodos();
        return ResponseEntity.ok(pagamentos);
    }


    @PostMapping
    public ResponseEntity<PagamentoResponse> criarPagamento(@RequestBody PagamentoRequest pagamentoRequest) {
        PagamentoResponse pagamentoResponse = pagamentoService.criarPagamento(pagamentoRequest);
        return ResponseEntity.ok(pagamentoResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoResponse> editarPagamento(@PathVariable Long id, @RequestBody PagamentoRequest pagamentoRequest) {
        PagamentoResponse pagamentoResponse = pagamentoService.editarPagamento(id, pagamentoRequest);
        return ResponseEntity.ok(pagamentoResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPagamento(@PathVariable Long id) {
        pagamentoService.excluirPagamento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponse> buscarPagamentoPorId(@PathVariable Long id) {
        PagamentoResponse pagamentoResponse = pagamentoService.buscarPagamentoPorId(id);
        return ResponseEntity.ok(pagamentoResponse);
    }

    @GetMapping("/igreja/{igrejaId}")
    public ResponseEntity<List<PagamentoResponse>> buscarPagamentosPorIgreja(@PathVariable Long igrejaId) {
        List<PagamentoResponse> pagamentos = pagamentoService.buscarPagamentosPorIgreja(igrejaId);
        return ResponseEntity.ok(pagamentos);
    }

    @PostMapping("/{pagamentoId}/parcelas/qrcodes")
    public ResponseEntity<String> gerarQRCodesParaPagamento(@PathVariable Long pagamentoId) {
        pagamentoService.gerarQRCodesParaPagamento(pagamentoId);
        return ResponseEntity.status(HttpStatus.OK).body("QR Codes gerados com sucesso para o pagamento ID: " + pagamentoId);
    }

    @PostMapping("/obterDadosParcela")
    public ResponseEntity<Map<String, String>> obterDadosParcela(@RequestBody QRCodeRequest request) {
        Map<String, String> dadosParcela = pagamentoService.obterDadosParcela(request.getPagamentoId(), request.getNumeroParcela());
        return ResponseEntity.ok(dadosParcela);
    }

    @PostMapping("/gerarQRCodeParcela")
    public ResponseEntity<byte[]> gerarQRCodeParaParcela(@RequestBody @Valid QRCodeRequest request) {
        return pagamentoService.gerarQRCodeParaParcela(request.getPagamentoId(), request.getNumeroParcela());
    }


    @GetMapping("/{id}/parcelas")
    public ResponseEntity<PagamentoResponse> buscarParcelasPorPagamentoId(@PathVariable Long id) {
        try {
            // Chamar o método do service
            PagamentoResponse pagamentoResponse = pagamentoService.buscarParcelasPorPagamentoId(id);
            return ResponseEntity.ok(pagamentoResponse); // Retornar status 200 com o corpo da resposta
        } catch (EntityNotFoundException e) {
            // Caso o pagamento não seja encontrado, retornar 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            // Capturar outros erros inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @PostMapping("/gerarQRCode")
    public ResponseEntity<byte[]> gerarQRCode(@RequestBody QRCodeRequest request) {
        try {
            byte[] qrCode = pagamentoService.generateQRCode(request);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrCode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/por-igreja/{igrejaId}")
    public ResponseEntity<List<PagamentoResponse>> porIgreja(@PathVariable Long igrejaId) {
        List<PagamentoResponse> pagamentos = pagamentoService.porIgreja(igrejaId);
        return ResponseEntity.ok(pagamentos);
    }



}
