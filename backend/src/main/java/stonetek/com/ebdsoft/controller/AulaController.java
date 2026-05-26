package stonetek.com.ebdsoft.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import stonetek.com.ebdsoft.dto.dtosespecificos.*;
import stonetek.com.ebdsoft.dto.mapper.AulaMapper;
import stonetek.com.ebdsoft.dto.request.AulaRequest;
import stonetek.com.ebdsoft.dto.request.AulaTurmaRequest;
import stonetek.com.ebdsoft.dto.request.ProfessorAulaRequest;
import stonetek.com.ebdsoft.dto.response.AulaResponse;
import stonetek.com.ebdsoft.dto.response.AulaTurmaResponse;
import stonetek.com.ebdsoft.dto.response.ProfessorAulaResponse;
import stonetek.com.ebdsoft.model.Aula;
import stonetek.com.ebdsoft.model.AulaTurma;
import stonetek.com.ebdsoft.model.ProfessorAula;
import stonetek.com.ebdsoft.model.Turma;
import stonetek.com.ebdsoft.repository.AulaRepository;
import stonetek.com.ebdsoft.repository.TurmaRepository;
import stonetek.com.ebdsoft.service.AulaService;
import stonetek.com.ebdsoft.service.TurmaService;
import stonetek.com.ebdsoft.util.TrimestreUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/aulas")
public class AulaController {

    private final AulaService aulaService;

    private final AulaRepository aulaRepository;

    private final TurmaRepository turmaRepository;


@GetMapping
public ResponseEntity<List<AulaResponse>> listar() {
    return ResponseEntity.ok().body(aulaService.listar());
}

@GetMapping("/{idAula}")
public ResponseEntity<AulaResponse> buscarPorId(@PathVariable Long idAula) {
    AulaResponse aula = aulaService.buscarPorId(idAula);
    return ResponseEntity.ok().body(aula);
}

@PostMapping
public ResponseEntity<AulaResponse> salvar(@Valid @RequestBody AulaRequest request) {
    AulaResponse aula = aulaService.salvar(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(aula);
}

    @PutMapping("/{idAula}")
    public ResponseEntity<AulaResponse> editar(@PathVariable Long idAula,
            @Valid @RequestBody AulaRequest request) {
        AulaResponse aula = aulaService.editar(idAula, request);
        return ResponseEntity.ok().body(aula);
    }

    @DeleteMapping("/{idAula}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long idAula) {
    aulaService.excluir(idAula);
}


//personalizados

    @GetMapping("/trimestre")
    public ResponseEntity<List<Aula>> getAulasByTrimestre(@RequestParam String trimestre) {
        List<Aula> aulas = aulaRepository.findByTrimestre(trimestre);
        return ResponseEntity.ok().body(aulas);
    }
    @PostMapping("/buscar-por-trimestre")
    public ResponseEntity<List<Aula>> getAulasByTrimestre(@RequestBody TrimestreRequest request) {
        List<Aula> aulas = aulaRepository.findByTrimestreAndAno(request.getTrimestre(), request.getAno());
        return ResponseEntity.ok().body(aulas);
    }

    @GetMapping("/aulascomturmas")
    public List<AulaResponse> getAllAulas() {
        return aulaService.getAllAulas();
    }

    @PostMapping("/ofertas")
    public ResponseEntity<List<OfertaResponse>> somaOfertasPorTurmaEMesETrimestre(@RequestBody OfertaRequest ofertaRequest) {
        Long turmaId = ofertaRequest.getTurmaId();
        Integer mes = ofertaRequest.getMes();
        Integer trimestre = ofertaRequest.getTrimestre();
        Integer ano = ofertaRequest.getAno();

        if ((mes == null && trimestre == null) || ano == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<OfertaResponse> ofertas = aulaService.somaOfertasPorTurmaEMesETrimestre(turmaId, mes, trimestre, ano);
        return ResponseEntity.ok(ofertas);
    }

    //Aula professor

    @PostMapping("/current-trimestre")
    public ResponseEntity<List<Aula>> getAulasByCurrentTrimestre(@RequestBody TrimestreRequest currentTrimestre) {
        List<Aula> aulas = aulaService.findByTrimestreAndAno(currentTrimestre.getTrimestre(), currentTrimestre.getAno());
        return ResponseEntity.ok(aulas);
    }

    //aula e turma

    @PostMapping("/aula-turma-vinculo")
    public AulaTurma criarVinculoAulaTurma(@RequestBody AulaTurmaRequest request) {
        return aulaService.criarVinculoAulaTurma(request.getIdAula(), request.getIdTurma());
    }

    @PutMapping("/aula-turma-vinculo/{id}")
    public ResponseEntity<AulaTurma> atualizarVinculoAulaTurma(@PathVariable Long id, @RequestBody AulaTurmaRequest request) {
        AulaTurma updatedAulaTurma = aulaService.atualizarVinculoAulaTurma(id, request.getIdAula(), request.getIdTurma());
        return ResponseEntity.ok(updatedAulaTurma);
    }

    @GetMapping("/aula-turma-vinculo/{id}")
    public ResponseEntity<AulaTurmaResponse> getAulaTurmaById(@PathVariable Long id) {
        AulaTurmaResponse aulaTurmaResponse = aulaService.findAulaTurmaById(id);
        return ResponseEntity.ok(aulaTurmaResponse);
    }

    @GetMapping("/aula-turmas")
    public ResponseEntity<List<AulaTurmaDTO>> listarAulaETurmas() {
        List<AulaTurmaDTO> aulaTurma = aulaService.listarAulaETurmas();
        return ResponseEntity.ok(aulaTurma);
    }


    //Aula Igreja

    @GetMapping("/igreja/{idIgreja}")
    public ResponseEntity<List<AulaResponse>> listarAulasPorIgreja(@PathVariable Long idIgreja) {
        List<Aula> aulas = aulaService.listarAulasPorIgreja(idIgreja);
        if (aulas.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<AulaResponse> aulaResponses = aulas.stream()
                    .map(AulaMapper::converte)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(aulaResponses);
        }
    }

    @GetMapping("/aulaeturmas/{igrejaId}")
    public ResponseEntity<List<AulaTurmaDTO>> listarAulaETurmasPorIgrejaId(@PathVariable Long igrejaId) {
        List<AulaTurmaDTO> result = aulaService.listarAulaETurmasPorIgrejaId(igrejaId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/igreja-turmas/{igrejaId}")
    public ResponseEntity<List<Turma>> obterTurmasPorIgrejaId(@PathVariable Long igrejaId) {
        List<Turma> turmas = aulaService.obterTurmasPorIgrejaId(igrejaId);
        if (turmas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(turmas);
    }


}
