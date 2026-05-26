package stonetek.com.ebdsoft.controller;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import stonetek.com.ebdsoft.dto.dtosespecificos.*;
import stonetek.com.ebdsoft.dto.request.AlunoAulaRequest;
import stonetek.com.ebdsoft.dto.request.AlunoRequest;
import stonetek.com.ebdsoft.dto.request.AlunoTurmaRequest;
import stonetek.com.ebdsoft.dto.request.EbdTurmaRequest;
import stonetek.com.ebdsoft.dto.response.AlunoAulaResponse;
import stonetek.com.ebdsoft.dto.response.AlunoResponse;
import stonetek.com.ebdsoft.dto.response.AlunoTurmaResponse;
import stonetek.com.ebdsoft.dto.response.EbdTurmaResponse;
import stonetek.com.ebdsoft.model.*;
import stonetek.com.ebdsoft.repository.AlunoRepository;
import stonetek.com.ebdsoft.repository.AlunoTurmaRepository;
import stonetek.com.ebdsoft.repository.TurmaRepository;
import stonetek.com.ebdsoft.service.AlunoService;
import stonetek.com.ebdsoft.util.FiltroAulas;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    private final AlunoRepository alunoRepository;

    private final TurmaRepository turmaRepository;

    private final AlunoTurmaRepository alunoTurmaRepository;

    @GetMapping
    public ResponseEntity<List<AlunoResponse>> listar() {
        return ResponseEntity.ok().body(alunoService.listar());
    }

    @PostMapping("/aulas")
    public ResponseEntity<List<Aula>> getAulasPorFiltro(@RequestBody FiltroAulas filtro) {
        List<Aula> aulas = alunoService.buscarAulasPorFiltro(filtro);
        return ResponseEntity.ok(aulas);
    }
    @GetMapping("/{idAluno}")
    public ResponseEntity<AlunoResponse> buscarPorId(@PathVariable Long idAluno) {
        AlunoResponse aluno = alunoService.buscarPorId(idAluno);
        return ResponseEntity.ok().body(aluno);
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> salvar(@Valid @RequestBody AlunoRequest request) {
        AlunoResponse aluno = alunoService.salvar(request, request.getIgrejaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(aluno);
    }


    @PutMapping("/{idAluno}")
    public ResponseEntity<AlunoResponse> editar(@PathVariable Long idAluno,
            @Valid @RequestBody AlunoRequest request) {
        AlunoResponse aluno = alunoService.editar(idAluno, request);
        return ResponseEntity.ok().body(aluno);
    }

    @DeleteMapping("/{idAluno}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long idAluno) {
        alunoService.excluir(idAluno);
    }

    //personalizados

    @GetMapping("/{alunoId}/detalhes")
    public ResponseEntity<AlunoDetalhesDTO> buscarDetalhesDoAluno(@PathVariable Long alunoId) {
        AlunoDetalhesDTO detalhes = alunoService.buscarDetalhesDoAluno(alunoId);
        return ResponseEntity.ok(detalhes);
    }

    @GetMapping("/detalhes")
    public ResponseEntity<List<AlunoDTO>> buscarTodosOsAlunosComDetalhes() {
        List<AlunoDTO> alunos = alunoService.buscarTodosOsAlunosComDetalhes();
        return ResponseEntity.ok(alunos);
    }

    // Aluno Turma

    @PostMapping("/aluno-turma-vinculo")
    public List<AlunoTurma> criarVinculoAlunoTurma(@RequestBody AlunoTurmaRequest request) {
        return alunoService.criarVinculoAlunoTurma(request.getAlunoIds(), request.getTurmaId());
    }

    @PutMapping("/aluno-turma-vinculo/{id}")
    public ResponseEntity<AlunoTurma> atualizarVinculoAlunoTurma(@PathVariable Long id, @RequestBody AlunoTurmaRequest request) {
        AlunoTurma updatedAlunoTurma = alunoService.atualizarVinculoAlunoTurma(id, request.getAlunoId(), request.getTurmaId());
        System.out.println("turmaId: " + request.getTurmaId());
        System.out.println("alunoIds: " + request.getAlunoIds());

        return ResponseEntity.ok(updatedAlunoTurma);
    }


    @GetMapping("/aluno-turma-vinculo/{id}")
    public ResponseEntity<AlunoTurmaResponse> getAlunoTurmaById(@PathVariable Long id) {
        AlunoTurmaResponse alunoTurmaResponse = alunoService.findAlunoTurmaById(id);
        return ResponseEntity.ok(alunoTurmaResponse);
    }

    @GetMapping("/aluno-turmas")
    public ResponseEntity<List<AlunoTurmaDTO>> listarAlunoETurmas() {
        List<AlunoTurmaDTO> alunoETurmas = alunoService.listarAlunoETurmas();
        return ResponseEntity.ok(alunoETurmas);
    }


    @GetMapping("/aluno-turmas/{igrejaId}")
    public ResponseEntity<List<AlunoTurmaDTO>> listarAlunoETurmasPorIgreja(@PathVariable Long igrejaId) {
        List<AlunoTurmaDTO> alunoETurmas = alunoService.listarAlunoETurmasPorIgreja(igrejaId);
        return ResponseEntity.ok(alunoETurmas);
    }

    @GetMapping("/turma/{idTurma}")
    public ResponseEntity<List<Aluno>> getAlunosByTurma(@PathVariable Long idTurma) {
        List<Aluno> alunos = alunoService.findAlunosByTurma(idTurma);
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/turmas/{idTurma}/alunosElegiveis")
    public ResponseEntity<List<AlunoDTO>> listarAlunosElegiveis(@PathVariable Long idTurma) {
        List<AlunoDTO> alunosElegiveis = alunoService.listarAlunosElegiveisPorTurma(idTurma);
        return ResponseEntity.ok(alunosElegiveis);
    }



    //Aluno Aula

    @PostMapping("/aluno-aula-vinculo")
    public AlunoAula criarVinculoAula(@RequestBody AlunoAulaRequest request) {
        return alunoService.criarVinculoAlunoAula(request.getIdAluno(), request.getIdAula());
    }

    @PutMapping("/aluno-aula-vinculo/{id}")
    public ResponseEntity<AlunoAula> atualizarVinculoAlunoAula(@PathVariable Long id, @RequestBody AlunoAulaRequest request) {
        AlunoAula updatedAlunoAula = alunoService.atualizarVinculoAlunoAula(id, request.getIdAula(), request.getIdAluno());
        return ResponseEntity.ok(updatedAlunoAula);
    }

    @GetMapping("/aluno-aula-vinculo/{id}")
    public ResponseEntity<AlunoAulaResponse> getAlunoAulaById(@PathVariable Long id) {
        AlunoAulaResponse alunoAulaResponse = alunoService.findAlunoAulaById(id);
        return ResponseEntity.ok(alunoAulaResponse);
    }

    @GetMapping("/aluno-aulas")
    public ResponseEntity<List<AlunoAulaDTO>> listarAlunoEAulas() {
        List<AlunoAulaDTO> alunoEAulas = alunoService.listarAlunoEAulas();
        return ResponseEntity.ok(alunoEAulas);
    }

    @GetMapping("/aluno-aulas/{igrejaId}")
    public ResponseEntity<List<AlunoAulaDTO>> listarAlunoEAulasPorIgreja(@PathVariable Long igrejaId) {
        List<AlunoAulaDTO> alunoEAulas = alunoService.listarAlunoEAulasPorIgreja(igrejaId);
        return ResponseEntity.ok(alunoEAulas);
    }




    // Aluno Igreja

    @GetMapping("/igreja/{idIgreja}")
    public ResponseEntity<List<Aluno>> listarAlunosPorIgreja(@PathVariable Long idIgreja) {
        List<Aluno> alunos = alunoService.listarAlunosPorIgreja(idIgreja);
        return ResponseEntity.ok(alunos);
    }


    //métodos rejeitados, mas que podem vir a ser usados


    /*@PostMapping("/aluno-turma-vinculo")
    public AlunoTurma criarVinculoAlunoTurma(@RequestBody AlunoTurmaRequest request) {
        return alunoService.criarVinculoAlunoTurma(request.getAlunoId(), request.getTurmaId());
    } */


  /*  @GetMapping("/turma/{turmaId}/area")
    public ResponseEntity<List<Aluno>> getAlunosByTurmaAndArea(@PathVariable Long turmaId) {
        List<Aluno> alunos = alunoService.findAlunosByTurmaAndArea(turmaId);
        return ResponseEntity.ok(alunos);
    }*/


}
