package stonetek.com.ebdsoft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import stonetek.com.ebdsoft.dto.dtosespecificos.AlunoAulaDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.AulaDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.ProfessorAulaDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.ProfessorTurmaDTO;
import stonetek.com.ebdsoft.dto.request.AulaRequest;
import stonetek.com.ebdsoft.dto.request.ProfessorAulaRequest;
import stonetek.com.ebdsoft.dto.request.ProfessorRequest;
import stonetek.com.ebdsoft.dto.request.ProfessorTurmaRequest;
import stonetek.com.ebdsoft.dto.response.AulaResponse;
import stonetek.com.ebdsoft.dto.response.ProfessorAulaResponse;
import stonetek.com.ebdsoft.dto.response.ProfessorResponse;
import stonetek.com.ebdsoft.dto.response.ProfessorTurmaResponse;
import stonetek.com.ebdsoft.model.*;
import stonetek.com.ebdsoft.service.ProfessorService;
import stonetek.com.ebdsoft.util.ResourceUriUtil;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/professores")
public class ProfessorController {

    private final ProfessorService professorService;

     @GetMapping
    public ResponseEntity<List<ProfessorResponse>> listar() {
    return ResponseEntity.ok().body(professorService.listar());
    }

    @GetMapping("/{idProfessor}")
    public ResponseEntity<ProfessorResponse> buscarPorId(@PathVariable Long idProfessor) {
        ProfessorResponse professor = professorService.buscarPorId(idProfessor);
        return ResponseEntity.ok().body(professor);
    }

    @PostMapping
    public ResponseEntity<ProfessorResponse> salvar(@RequestBody ProfessorRequest request) {
        ProfessorResponse professor = professorService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(professor);
    }

    @PutMapping("/{idProfessor}")
    public ResponseEntity<ProfessorResponse> editar(@PathVariable Long idProfessor,
            @Valid @RequestBody ProfessorRequest request) {
        ProfessorResponse professor = professorService.editar(idProfessor, request);
        return ResponseEntity.ok().body(professor);
    }

    @DeleteMapping("/{idProfessor}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long idProfessor) {
    professorService.excluir(idProfessor);
}

    //personalizados**********

    //Professor e Turmas


    @PostMapping("/professor-turma-vinculo")
    public ProfessorTurma criarVinculoProfessorTurma(@RequestBody ProfessorTurmaRequest request) {
        return professorService.criarVinculoProfessorTurma(request.getIdProfessor(), request.getIdTurma());
    }

    @PutMapping("/professor-turma-vinculo/{id}")
    public ResponseEntity<ProfessorTurma> atualizarVinculoProfessorTurma(@PathVariable Long id, @RequestBody ProfessorTurmaRequest request) {
        ProfessorTurma updatedProfessorTurma = professorService.atualizarVinculoProfessorTurma(id, request.getIdProfessor(), request.getIdTurma());
        return ResponseEntity.ok(updatedProfessorTurma);
    }

    @GetMapping("/professor-turma-vinculo/{id}")
    public ResponseEntity<ProfessorTurmaResponse> getProfessorTurmaById(@PathVariable Long id) {
        ProfessorTurmaResponse professorTurmaResponse = professorService.findProfessorTurmaById(id);
        return ResponseEntity.ok(professorTurmaResponse);
    }

    @GetMapping("/professor-turmas")
    public ResponseEntity<List<ProfessorTurmaDTO>> listarprofessoresETurmas() {
        List<ProfessorTurmaDTO> professorTurma = professorService.listarprofessoresETurmas();
        return ResponseEntity.ok(professorTurma);
    }


    @GetMapping("/turma/{idTurma}")
    public ResponseEntity<List<Professor>> getProfessoresByTurmaId(@PathVariable Long idTurma) {
        List<Professor> professores = professorService.findProfessoresByTurmaId(idTurma);
        return ResponseEntity.ok(professores);
    }

    @GetMapping("/professor-turmas/{igrejaId}")
    public ResponseEntity<List<ProfessorTurmaDTO>> listarProfessorETurmasPorIgreja(@PathVariable Long igrejaId){
         List<ProfessorTurmaDTO> professorETurmas = professorService.listarPorfessorETurmaPorIgreja(igrejaId);
         return ResponseEntity.ok(professorETurmas);
    }

    @PostMapping("/buscar-por-professor")
    public ResponseEntity<Set<Turma>> buscarTurmasPorProfessor(@RequestBody Map<String, Long> request) {
        Long professorId = request.get("professorId");
        if (professorId == null) {
            return ResponseEntity.badRequest().build();
        }
        Set<Turma> turmas = professorService.getTurmasByProfessorId(professorId);
        return ResponseEntity.ok(turmas);
    }

    //Professor Aulas***********************
    @PostMapping("/professor-aula-vinculo")
    public ProfessorAula criarVinculoProfessorAula(@RequestBody ProfessorAulaRequest request) {
        return professorService.criarVinculoProfessorAula(request.getIdProfessor(), request.getIdAula());
    }

    @PutMapping("/professor-aula-vinculo/{id}")
    public ResponseEntity<ProfessorAula> atualizarVinculoProfessorAula(@PathVariable Long id, @RequestBody ProfessorAulaRequest request) {
        ProfessorAula updatedProfessorAula = professorService.atualizarVinculoProfessorAula(id, request.getIdProfessor(), request.getIdAula());
        return ResponseEntity.ok(updatedProfessorAula);
    }

    @GetMapping("/professor-aula-vinculo/{id}")
    public ResponseEntity<ProfessorAulaResponse> getProfessorAulaById(@PathVariable Long id) {
        ProfessorAulaResponse professorAulaResponse = professorService.findProfessorAulaById(id);
        return ResponseEntity.ok(professorAulaResponse);
    }

    @GetMapping("/professor-aulas")
    public ResponseEntity<List<ProfessorAulaDTO>> listarprofessoresEAulas() {
        List<ProfessorAulaDTO> professorAula = professorService.listarprofessoresEAulas();
        return ResponseEntity.ok(professorAula);
    }

    @GetMapping("/aulas")
    public List<AulaDTO> listarAulasComDetalhes(@RequestParam String userProfile, @AuthenticationPrincipal Usuario usuario, @RequestParam(required = false) Long igrejaId) {
        return professorService.listarAulasComDetalhes(userProfile, usuario, igrejaId);
    }

    @PostMapping("/aulasPorProfessor")
    public ResponseEntity<List<AulaResponse>> listarAulasPorProfessor(@RequestBody AulaRequest aulaRequest) {
        List<AulaResponse> aulas = professorService.listarAulasPorProfessor(aulaRequest.getId());
        return ResponseEntity.ok(aulas);
    }


    @GetMapping("/professor-aulas/{igrejaId}")
    public ResponseEntity<List<ProfessorAulaDTO>> listarAlunoEAulasPorIgreja(@PathVariable Long igrejaId) {
        List<ProfessorAulaDTO> professorAulas = professorService.listarProfessorEAulasPorIgreja(igrejaId);
        return ResponseEntity.ok(professorAulas);
    }

    //Professor Igreja

    @GetMapping("/igreja/{idIgreja}")
    public ResponseEntity<List<Professor>> listarProfessorPorIgreja(@PathVariable Long idIgreja) {
     List<Professor> professores = professorService.ListarProfessorPorIgreja(idIgreja);
     return ResponseEntity.ok(professores);
    }

    //Professor Aluno

    @PostMapping("/aluno-por-professor")
    public ResponseEntity<Set<Aluno>> buscarAlunosPorProfessor(@RequestBody Map<String, Long> request) {
        Long professorId = request.get("professorId");
        if (professorId == null) {
            return ResponseEntity.badRequest().build();
        }
        Set<Aluno> alunos = professorService.buscarAlunosPorProfessor(professorId);
        return ResponseEntity.ok(alunos);
    }

}
