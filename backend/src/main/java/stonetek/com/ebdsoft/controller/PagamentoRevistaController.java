package stonetek.com.ebdsoft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stonetek.com.ebdsoft.dto.dtosespecificos.PagamentoRevistaDTO;
import stonetek.com.ebdsoft.model.Aluno;
import stonetek.com.ebdsoft.model.PagamentoRevista;
import stonetek.com.ebdsoft.model.Professor;
import stonetek.com.ebdsoft.model.Revista;
import stonetek.com.ebdsoft.service.AlunoService;
import stonetek.com.ebdsoft.service.PagamentoRevistaService;
import stonetek.com.ebdsoft.service.ProfessorService;
import stonetek.com.ebdsoft.service.RevistaService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/pagamentorevista")
public class PagamentoRevistaController {

    private final PagamentoRevistaService pagamentoRevistaService;

    private final RevistaService revistaService;

    private final AlunoService alunoService;

    private final ProfessorService professorService;


    /**
     * GET /api/pagamentos
     * Retorna todos os pagamentos de revista.
     */
    @GetMapping
    public ResponseEntity<List<PagamentoRevista>> getAllPagamentos() {
        List<PagamentoRevista> pagamentos = pagamentoRevistaService.getAllPagamentos();
        return new ResponseEntity<>(pagamentos, HttpStatus.OK);
    }

    /**
     * GET /api/pagamentos/{id}
     * Retorna um pagamento de revista pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoRevista> getPagamentoById(@PathVariable Long id) {
        PagamentoRevista pagamento = pagamentoRevistaService.getPagamentoById(id);
        return new ResponseEntity<>(pagamento, HttpStatus.OK);
    }

    /**
     * POST /api/pagamentos
     * Cria um novo pagamento de revista.
     */
    @PostMapping
    public ResponseEntity<PagamentoRevista> createPagamento(@Valid @RequestBody PagamentoRevista pagamentoRevista) {
        PagamentoRevista novoPagamento = pagamentoRevistaService.createPagamento(pagamentoRevista);
        return new ResponseEntity<>(novoPagamento, HttpStatus.CREATED);
    }

    /**
     * PUT /api/pagamentos/{id}
     * Atualiza um pagamento de revista existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PagamentoRevista> updatePagamento(@PathVariable Long id, @Valid @RequestBody PagamentoRevista pagamentoDetails) {
        PagamentoRevista pagamentoAtualizado = pagamentoRevistaService.updatePagamento(id, pagamentoDetails);
        return new ResponseEntity<>(pagamentoAtualizado, HttpStatus.OK);
    }

    /**
     * DELETE /api/pagamentos/{id}
     * Deleta um pagamento de revista pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePagamento(@PathVariable Long id) {
        pagamentoRevistaService.deletePagamento(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * GET /api/pagamentos/revista/{revistaId}
     * Retorna pagamentos por ID da revista.
     */
    @GetMapping("/revista/{revistaId}")
    public ResponseEntity<List<PagamentoRevista>> getPagamentosByRevistaId(@PathVariable Long revistaId) {
        List<PagamentoRevista> pagamentos = pagamentoRevistaService.getPagamentosByRevistaId(revistaId);
        return new ResponseEntity<>(pagamentos, HttpStatus.OK);
    }

    /**
     * GET /api/pagamentos/aluno/{alunoId}
     * Retorna pagamentos por ID do aluno.
     */
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<PagamentoRevista>> getPagamentosByAlunoId(@PathVariable Long alunoId) {
        List<PagamentoRevista> pagamentos = pagamentoRevistaService.getPagamentosByAlunoId(alunoId);
        return new ResponseEntity<>(pagamentos, HttpStatus.OK);
    }

    /**
     * GET /api/pagamentos/professor/{professorId}
     * Retorna pagamentos por ID do professor.
     */
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<PagamentoRevista>> getPagamentosByProfessorId(@PathVariable Long professorId) {
        List<PagamentoRevista> pagamentos = pagamentoRevistaService.getPagamentosByProfessorId(professorId);
        return new ResponseEntity<>(pagamentos, HttpStatus.OK);
    }

    /**
     * GET /api/pagamentos/pendentes
     * Retorna pagamentos pendentes (pago = false).
     */
    @GetMapping("/pendentes")
    public ResponseEntity<List<PagamentoRevista>> getPagamentosPendentes() {
        List<PagamentoRevista> pagamentos = pagamentoRevistaService.getPagamentosPendentes();
        return new ResponseEntity<>(pagamentos, HttpStatus.OK);
    }

    /**
     * GET /api/pagamentos/vencidos?data=YYYY-MM-DD
     * Retorna pagamentos com data de vencimento antes de uma data específica.
     */
    @GetMapping("/vencidos")
    public ResponseEntity<List<PagamentoRevista>> getPagamentosVencidosAntesDe(@RequestParam("data") String data) {
        LocalDate dataLimit = LocalDate.parse(data);
        List<PagamentoRevista> pagamentos = pagamentoRevistaService.getPagamentosVencidosAntesDe(dataLimit);
        return new ResponseEntity<>(pagamentos, HttpStatus.OK);
    }


    //métodos que usam DTO

    /**
     * POST /api/pagamentos
     * Cria um novo pagamento de revista usando PagamentoRevistaDTO.
     */
    @PostMapping("/pagamentos")
    public ResponseEntity<PagamentoRevista> createPagamentos(@Valid @RequestBody PagamentoRevistaDTO pagamentoDTO) {
        PagamentoRevista pagamentoRevista = new PagamentoRevista();
        pagamentoRevista.setDataPagamento(pagamentoDTO.getDataPagamento());
        pagamentoRevista.setPago(pagamentoDTO.isPago());
        pagamentoRevista.setDataVencimento(pagamentoDTO.getDataVencimento());
        pagamentoRevista.setParcela(pagamentoDTO.getParcela());
        pagamentoRevista.setValorPago(pagamentoDTO.getValorPago());

        // Associar Revista
        Revista revista = revistaService.getRevistaById(pagamentoDTO.getRevistaId());
        pagamentoRevista.setRevista(revista);

        // Associar Aluno ou Professor
        if (pagamentoDTO.getAlunoId() != null) {
            Aluno aluno = alunoService.getAlunoById(pagamentoDTO.getAlunoId());
            pagamentoRevista.setAluno(aluno);
        } else if (pagamentoDTO.getProfessorId() != null) {
            Professor professor = professorService.getProfessorById(pagamentoDTO.getProfessorId());
            pagamentoRevista.setProfessor(professor);
        }

        PagamentoRevista novoPagamento = pagamentoRevistaService.createPagamento(pagamentoRevista);
        return new ResponseEntity<>(novoPagamento, HttpStatus.CREATED);
    }

    /**
     * PUT /api/pagamentos/{id}
     * Atualiza um pagamento de revista existente usando PagamentoRevistaDTO.
     */
    @PutMapping("pagamentos/{id}")
    public ResponseEntity<PagamentoRevista> updatePagamentos(@PathVariable Long id, @Valid @RequestBody PagamentoRevistaDTO pagamentoDTO) {
        PagamentoRevista pagamentoDetails = new PagamentoRevista();
        pagamentoDetails.setDataPagamento(pagamentoDTO.getDataPagamento());
        pagamentoDetails.setPago(pagamentoDTO.isPago());
        pagamentoDetails.setDataVencimento(pagamentoDTO.getDataVencimento());
        pagamentoDetails.setParcela(pagamentoDTO.getParcela());
        pagamentoDetails.setValorPago(pagamentoDTO.getValorPago());

        // Associar Revista
        Revista revista = revistaService.getRevistaById(pagamentoDTO.getRevistaId());
        pagamentoDetails.setRevista(revista);

        // Associar Aluno ou Professor
        if (pagamentoDTO.getAlunoId() != null) {
            Aluno aluno = alunoService.getAlunoById(pagamentoDTO.getAlunoId());
            pagamentoDetails.setAluno(aluno);
        } else if (pagamentoDTO.getProfessorId() != null) {
            Professor professor = professorService.getProfessorById(pagamentoDTO.getProfessorId());
            pagamentoDetails.setProfessor(professor);
        }

        PagamentoRevista pagamentoAtualizado = pagamentoRevistaService.updatePagamento(id, pagamentoDetails);
        return new ResponseEntity<>(pagamentoAtualizado, HttpStatus.OK);
    }

}
