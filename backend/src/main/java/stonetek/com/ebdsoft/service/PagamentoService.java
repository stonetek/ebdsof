package stonetek.com.ebdsoft.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import stonetek.com.ebdsoft.dto.mapper.PagamentoMapper;
import stonetek.com.ebdsoft.dto.mapper.ParcelaMapper;
import stonetek.com.ebdsoft.dto.request.PagamentoRequest;
import stonetek.com.ebdsoft.dto.request.QRCodeRequest;
import stonetek.com.ebdsoft.dto.response.PagamentoResponse;
import stonetek.com.ebdsoft.dto.response.ParcelaResponse;
import stonetek.com.ebdsoft.exception.EntityNotFoundException;
import stonetek.com.ebdsoft.model.Pagamento;
import stonetek.com.ebdsoft.model.PagamentoParcela;
import stonetek.com.ebdsoft.model.Parcela;
import stonetek.com.ebdsoft.model.PedidoPagamento;
import stonetek.com.ebdsoft.repository.PagamentoParcelaRepository;
import stonetek.com.ebdsoft.repository.PagamentoRepository;
import stonetek.com.ebdsoft.repository.ParcelaRepository;
import stonetek.com.ebdsoft.repository.PedidoPagamentoRepository;
import stonetek.com.ebdsoft.util.QRCodeGenerator;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    private final PedidoPagamentoRepository pedidoPagamentoRepository;

    private final ParcelaRepository parcelaRepository;

    private final PagamentoParcelaRepository pagamentoParcelaRepository;


    public List<PagamentoResponse> buscarTodos() {
        List<Pagamento> pagamentos = pagamentoRepository.findAll();

        pagamentos.forEach(pagamento -> {
            List<PagamentoParcela> parcelas = pagamentoParcelaRepository.findByPagamentoId(pagamento.getId());
            parcelas.forEach(parcela -> {

                ParcelaResponse response = mapToParcelaResponse(parcela.getParcela());

            });
        });

        return pagamentos.stream()
                .map(pagamento -> {
                    // Obter as parcelas relacionadas ao pagamento diretamente do pagamentoParcelaRepository
                    Set<ParcelaResponse> parcelaResponses = pagamentoParcelaRepository.findByPagamentoId(pagamento.getId())
                            .stream()
                            .map(pagamentoParcela -> mapToParcelaResponse(pagamentoParcela.getParcela()))
                            .collect(Collectors.toSet());

                    // Converter o pagamento para PagamentoResponse
                    PagamentoResponse pagamentoResponse = PagamentoMapper.converter(pagamento);
                    pagamentoResponse.setParcelasSet(parcelaResponses); // Atribuir ao campo correto

                    return pagamentoResponse;
                })
                .collect(Collectors.toList());
    }


    private ParcelaResponse mapToParcelaResponse(Parcela parcela) {
        if (parcela == null) {
            System.out.println("Parcela inválida ou ausente");
            return null;
        }

        ParcelaResponse response = new ParcelaResponse();
        response.setId(parcela.getId());
        response.setNumero(parcela.getNumero());
        response.setValor(parcela.getValor());
        response.setDataVencimento(parcela.getDataVencimento());
        response.setAtraso(parcela.getAtraso());
        response.setStatus(parcela.getStatus());
        response.setDataPagamento(parcela.getDataPagamento());
        return response;
    }



    @Transactional
    public PagamentoResponse criarPagamento(PagamentoRequest pagamentoRequest) {
        Pagamento pagamento = PagamentoMapper.converter(pagamentoRequest);
        pagamento = pagamentoRepository.save(pagamento);
        return PagamentoMapper.converter(pagamento);
    }

    // Método para editar um pagamento
    @Transactional
    public PagamentoResponse editarPagamento(Long id, PagamentoRequest pagamentoRequest) {
        Optional<Pagamento> pagamentoExistente = pagamentoRepository.findById(id);
        if (!pagamentoExistente.isPresent()) {
            throw new EntityNotFoundException("Pagamento não encontrado com id " + id);
        }
        Pagamento pagamento = pagamentoExistente.get();
        PagamentoMapper.copyToProperties(pagamentoRequest, pagamento);
        pagamento = pagamentoRepository.save(pagamento);
        return PagamentoMapper.converter(pagamento);
    }

    // Método para excluir um pagamento
    @Transactional
    public void excluirPagamento(Long id) {
        Optional<Pagamento> pagamentoExistente = pagamentoRepository.findById(id);
        if (!pagamentoExistente.isPresent()) {
            throw new EntityNotFoundException("Pagamento não encontrado com id " + id);
        }
        pagamentoRepository.deleteById(id);
    }

    // Método para buscar um pagamento por ID
    public PagamentoResponse buscarPagamentoPorId(Long id) {
        Optional<Pagamento> pagamento = pagamentoRepository.findById(id);
        if (!pagamento.isPresent()) {
            throw new EntityNotFoundException("Pagamento não encontrado com id " + id);
        }
        return PagamentoMapper.converter(pagamento.get());
    }

    /**
     *Método para obter as parcelas vinculadas a um pagamento
     *
     */

    public PagamentoResponse buscarParcelasPorPagamentoId(Long id) {
        Optional<Pagamento> pagamentoOpt = pagamentoRepository.findById(id);
        if (!pagamentoOpt.isPresent()) {
            throw new EntityNotFoundException("Pagamento não encontrado com id " + id);
        }
        Pagamento pagamento = pagamentoOpt.get();
        List<PagamentoParcela> pagamentoParcelas = pagamentoParcelaRepository.findByPagamentoId(id);
        return PagamentoMapper.converter(pagamento, pagamentoParcelas);
    }



    // Método para buscar pagamentos por Igreja
    @Transactional
    public List<PagamentoResponse> buscarPagamentosPorIgreja(Long igrejaId) {
        List<PedidoPagamento> pedidoPagamentos = pedidoPagamentoRepository.findByIgrejaId(igrejaId);

        return pedidoPagamentos.stream()
                .map(pedidoPagamento -> {
                    Pagamento pagamento = pedidoPagamento.getPagamento();

                    // FORÇANDO A INICIALIZAÇÃO DA LISTA Lazy
                    List<PagamentoParcela> pagamentoParcelas = new ArrayList<>(pagamento.getPagamentoParcelas());

                    return PagamentoMapper.converter(pagamento, pagamentoParcelas);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void gerarQRCodesParaPagamento(Long pagamentoId) {
        // Buscar o pagamento pelo ID
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado com ID: " + pagamentoId));

        // Iterar pelas parcelas do pagamento
        for (PagamentoParcela pagamentoParcela : pagamento.getPagamentoParcelas()) {
            Parcela parcela = pagamentoParcela.getParcela();

            // Gerar o payload Pix para o QR Code
            String chavePix = "mebellyyh@gmail.com"; // Exemplo de chave PIX
            String descricao = "Pagamento Parcela " + parcela.getNumero();
            String pixPayload = QRCodeGenerator.generatePixPayload(chavePix, parcela.getValor(), descricao);

            try {
                // Gerar a imagem do QR Code
                byte[] qrCodeImage = QRCodeGenerator.generateQRCodeImage(pixPayload, 200, 200);

                // Atualizar a parcela com a URL do QR Code
                parcelaRepository.save(parcela);
            } catch (WriterException | IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Erro ao gerar QR Code para a parcela " + parcela.getNumero(), e);
            }
        }
    }


    @Transactional
    public Map<String, String> obterDadosParcela(Long pagamentoId, Integer numeroParcela) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado com ID: " + pagamentoId));

        PagamentoParcela pagamentoParcela = pagamento.getPagamentoParcelas().stream()
                .filter(pp -> pp.getParcela().getNumero().equals(numeroParcela))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Parcela número " + numeroParcela + " não encontrada."));

        Parcela parcela = pagamentoParcela.getParcela();

        // URL fixa da imagem do QR Code já testada e válida
        String qrCodeUrl = "/qrcodes/qrcode.png";

        // Retornar os dados da parcela e a URL do QR Code estático
        Map<String, String> response = new HashMap<>();
        response.put("qrCodeImage", qrCodeUrl); // URL da imagem fixa
        response.put("valorParcela", parcela.getValor().toString());
        response.put("numeroParcela", parcela.getNumero().toString());
        response.put("chavePix", "91985781904"); // Chave PIX fixa
        response.put("descricao", "Pagamento Parcela " + parcela.getNumero());

        return response;
    }


    @Transactional
    public ResponseEntity<byte[]> gerarQRCodeParaParcela(Long pagamentoId, Integer numeroParcela) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado com ID: " + pagamentoId));

        PagamentoParcela pagamentoParcela = pagamentoParcelaRepository.findByPagamentoIdAndNumeroParcela(pagamentoId, numeroParcela)
                .orElseThrow(() -> new EntityNotFoundException("Parcela número " + numeroParcela + " não encontrada para o pagamento " + pagamentoId));

        Parcela parcela = pagamentoParcela.getParcela();

        String chavePix = "91985781904"; // Chave Pix fixa ou buscar do banco
        String descricao = "Pagamento Parcela " + parcela.getNumero();

        // Gerar código Pix com os dados da parcela
        String pixPayload = QRCodeGenerator.generatePixPayload(chavePix, parcela.getValor(), descricao);

        try {
            byte[] qrCodeImage = QRCodeGenerator.generateQRCodeImage(pixPayload, 200, 200);

            // Retornar a imagem como resposta
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrCodeImage);

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Erro ao gerar QR Code para a parcela número: " + numeroParcela, e);
        }
    }


    public byte[] generateQRCode(QRCodeRequest request) throws Exception {
            String brCode = generateBrCode(request);
            System.out.println("BR Code: " + brCode);
            int width = 300, height = 300;
            BitMatrix bitMatrix = new MultiFormatWriter().encode(brCode, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(image, "png", baos);

            byte[] qrCodeBytes = baos.toByteArray();

            return baos.toByteArray();
    }

    private String generateBrCode(QRCodeRequest request) {
        String valorFormatado = String.format(Locale.US, "%.2f", request.getValor()).replace(",", ".");
        String chavePix = request.getChavePix();
        String nomeRecebedor = request.getNomeRecebedor();
        String cidade = request.getCidade();

        String brCode = "000201"
                + "26580014br.gov.bcb.pix"
                + "01" + String.format("%02d", request.getChavePix().length()) + request.getChavePix()
                + "52040000"
                + "5303986"
                + "5404" + valorFormatado
                + "5802BR"
                + "59" + String.format("%02d", nomeRecebedor.length()) + nomeRecebedor
                + "60" + String.format("%02d", cidade.length()) + cidade
                + "62130509ABC123456"
                + "6304"; // Campo CRC

        // Calcular o CRC corretamente
        String crc = calculateCRC(brCode);

        // Retornar BR Code com o CRC correto no final
        return brCode + crc;
    }



    private String calculateCRC(String brCode) {
        int polynomial = 0x1021; // Polinômio CRC-16-CCITT (XMODEM)
        int crc = 0xFFFF; // Valor inicial

        byte[] bytes = brCode.getBytes(StandardCharsets.US_ASCII);

        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8; // XOR com o byte atual
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ polynomial;
                } else {
                    crc <<= 1;
                }
            }
        }

        crc &= 0xFFFF; // Garantir que o CRC tenha 16 bits
        return String.format("%04X", crc); // Retornar como hexadecimal maiúsculo (4 dígitos)
    }


    public List<PagamentoResponse> porIgreja(Long igrejaId) {
        List<Pagamento> pagamentos = pagamentoRepository.findByPedidoIgrejaId(igrejaId);

        return pagamentos.stream()
                .map(pagamento -> {
                    Set<ParcelaResponse> parcelaResponses = pagamentoParcelaRepository.findByPagamentoId(pagamento.getId())
                            .stream()
                            .map(pagamentoParcela -> mapToParcelaResponse(pagamentoParcela.getParcela()))
                            .collect(Collectors.toSet());

                    PagamentoResponse pagamentoResponse = PagamentoMapper.converter(pagamento);
                    pagamentoResponse.setParcelasSet(parcelaResponses);

                    return pagamentoResponse;
                })
                .collect(Collectors.toList());
    }



}
