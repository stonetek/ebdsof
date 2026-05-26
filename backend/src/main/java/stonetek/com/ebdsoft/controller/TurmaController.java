package stonetek.com.ebdsoft.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import stonetek.com.ebdsoft.dto.dtosespecificos.*;
import stonetek.com.ebdsoft.dto.request.ProfessorAulaRequest;
import stonetek.com.ebdsoft.dto.request.TurmaRequest;
import stonetek.com.ebdsoft.dto.response.AlunoResponse;
import stonetek.com.ebdsoft.dto.response.ProfessorAulaResponse;
import stonetek.com.ebdsoft.dto.response.TurmaResponse;
import stonetek.com.ebdsoft.model.Aluno;
import stonetek.com.ebdsoft.model.ProfessorAula;
import stonetek.com.ebdsoft.model.Turma;
import stonetek.com.ebdsoft.service.TurmaService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/turmas")
public class TurmaController {

    private final TurmaService turmaService;

    @GetMapping
    public ResponseEntity<List<TurmaResponse>> listar() {
    return ResponseEntity.ok().body(turmaService.listar());
}

    @GetMapping("/{idTurma}")
    public ResponseEntity<TurmaResponse> buscarPorId(@PathVariable Long idTurma) {
        TurmaResponse turma = turmaService.buscarPorId(idTurma);
        return ResponseEntity.ok().body(turma);
    }

    @PostMapping
    public ResponseEntity<TurmaResponse> salvar(@Valid @RequestBody TurmaRequest request) {
        TurmaResponse turma = turmaService.salvar(request, request.getIgrejaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(turma);
    }


    @PutMapping("/{idTurma}")
    public ResponseEntity<TurmaResponse> editar(@PathVariable Long idTurma,
            @Valid @RequestBody TurmaRequest request) {
        TurmaResponse turma = turmaService.editar(idTurma, request);
        return ResponseEntity.ok().body(turma);
    }

    @DeleteMapping("/{idTurma}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long idTurma) {
        turmaService.excluir(idTurma);
    }






    //personalizados

    //Turma e Aluno
    @PostMapping("/alunos-por-faixa-etaria")
    public ResponseEntity<List<Aluno>> listarAlunosPorFaixaEtaria(@RequestBody AlunoFaixaEtariaDTO faixaEtariaDTO) {
        List<Aluno> alunos = turmaService.listarAlunosPorFaixaEtaria(faixaEtariaDTO.getNomeTurma());
        return ResponseEntity.ok(alunos);
    }

    @PostMapping("/matricular-aluno")
    public ResponseEntity<String> matricularAluno(@RequestBody MatriculaDTO matriculaDTO) {
        try {
            if (matriculaDTO.getAlunoId() != null) {
                turmaService.matricularAluno(matriculaDTO.getAlunoId(), matriculaDTO.getTurmaId());
            } else if (matriculaDTO.getAlunosIds() != null && !matriculaDTO.getAlunosIds().isEmpty()) {
                turmaService.matricularAlunos(matriculaDTO.getAlunosIds(), matriculaDTO.getTurmaId());
            }
            return ResponseEntity.ok("Aluno(s) matriculado(s) com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{turmaId}/alunos")
    public ResponseEntity<List<AlunoResponse>> getAlunosByTurmaId(@PathVariable Long turmaId) {
        List<AlunoResponse> alunos = turmaService.getAlunosByTurmaId(turmaId);
        return ResponseEntity.ok(alunos);
    }






    //Turma e EBD

    @PostMapping("/vincular")
    public ResponseEntity<Void> vincularTurmaComEbd(@RequestBody TurmaEbdDTO turmaEbdDTO) {
        turmaService.vincularTurmaComEbd(turmaEbdDTO.getNomeTurma(), turmaEbdDTO.getNomeEbd());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ebd/{ebdId}")
    public ResponseEntity<List<TurmaEbdDTO>> listarTurmasPorEbdId(@PathVariable Long ebdId) {
        List<TurmaEbdDTO> turmas = turmaService.listarEbdETurmasPorEbdId(ebdId);
        return ResponseEntity.ok(turmas);
    }

    @GetMapping("/ebd-turmas")
    public ResponseEntity<List<TurmasEbdsDTO>> listarEbdETurmas() {
        List<TurmasEbdsDTO> ebdETurmas = turmaService.listarEbdETurmas();
        return ResponseEntity.ok(ebdETurmas);
    }

    @PostMapping("/{turmaId}/vincular/{ebdId}")
    public ResponseEntity<Turma> vincularTurmaAEbd(@PathVariable Long turmaId, @PathVariable Long ebdId) {
        Turma turma = turmaService.salvarVinculo(turmaId, ebdId);
        return ResponseEntity.ok(turma);
    }






    // Turma e Aula
    @GetMapping("/{idTurma}/aulas")
    public ResponseEntity<List<AulaTurmaDTO>> buscarAulasPorTurmaId(@PathVariable Long idTurma) {
        List<AulaTurmaDTO> aulas = turmaService.buscarAulasPorTurmaId(idTurma);
        return ResponseEntity.ok(aulas);
    }


    // Turma e Igreja


}
