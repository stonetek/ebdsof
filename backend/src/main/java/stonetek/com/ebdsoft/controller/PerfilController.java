package stonetek.com.ebdsoft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stonetek.com.ebdsoft.dto.dtosespecificos.PerfilAssignmentRequestDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.PerfilNomeDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.UsuarioNomeDTO;
import stonetek.com.ebdsoft.dto.request.PerfilRequest;
import stonetek.com.ebdsoft.dto.response.PerfilResponse;
import stonetek.com.ebdsoft.model.Aluno;
import stonetek.com.ebdsoft.model.Perfil;
import stonetek.com.ebdsoft.model.Professor;
import stonetek.com.ebdsoft.model.Usuario;
import stonetek.com.ebdsoft.repository.AlunoRepository;
import stonetek.com.ebdsoft.repository.PerfilRepository;
import stonetek.com.ebdsoft.repository.ProfessorRepository;
import stonetek.com.ebdsoft.service.PerfilService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/perfil")
public class PerfilController {

    private final PerfilService perfilService;

    private final PerfilRepository perfilRepository;

    private final ProfessorRepository professorRepository;

    private final AlunoRepository alunoRepository;

    @GetMapping
    public ResponseEntity<List<PerfilResponse>> listar() {
        return ResponseEntity.ok().body(perfilService.listar());
    }

    @GetMapping("/{idPerfil}")
    public ResponseEntity<PerfilResponse> buscarPorId(@PathVariable Long idPerfil) {
        PerfilResponse perfil = perfilService.buscarPorId(idPerfil);
        return ResponseEntity.ok().body(perfil);
    }

    @PostMapping
    public ResponseEntity<PerfilResponse> salvar(@Valid @RequestBody PerfilRequest request) {
        PerfilResponse perfil = perfilService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(perfil);
    }

    @PutMapping("/{idPerfil}")
    public ResponseEntity<PerfilResponse> editar(@PathVariable Long idPerfil,
                                                  @Valid @RequestBody PerfilRequest request) {
        PerfilResponse perfil = perfilService.editar(idPerfil, request);
        return ResponseEntity.ok().body(perfil);
    }

    @DeleteMapping("/{idPerfil}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long idPerfil) {
        perfilService.excluir(idPerfil);
    }

    @PostMapping("/vincular")
    public ResponseEntity<String> vincularPerfil(@RequestBody PerfilAssignmentRequestDTO request) {
        Optional<Perfil> perfilOptional = perfilRepository.findById(request.getPerfilId());
        if (perfilOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Perfil não encontrado");
        }
        Perfil perfil = perfilOptional.get();
        if ("aluno".equalsIgnoreCase(request.getTipoEntidade())) {
            Optional<Aluno> alunoOptional = alunoRepository.findById(request.getEntidadeId());
            if (alunoOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluno não encontrado");
            }
            Aluno aluno = alunoOptional.get();
            perfilService.vincularPerfil(aluno, perfil);
        } else if ("professor".equalsIgnoreCase(request.getTipoEntidade())) {
            Optional<Professor> professorOptional = professorRepository.findById(request.getEntidadeId());
            if (professorOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Professor não encontrado");
            }
            Professor professor = professorOptional.get();
            perfilService.vincularPerfil(professor, perfil);
        } else {
            return ResponseEntity.badRequest().body("Tipo de entidade inválido. Use 'aluno' ou 'professor'.");
        }
        return ResponseEntity.ok("Perfil vinculado com sucesso!");
    }

    @PostMapping("/pornome")
    public List<Perfil> getPerfilByNome(@RequestBody PerfilNomeDTO perfilNomeDTO) {
        return perfilService.findPerfilByNome(perfilNomeDTO.getNome());
    }

    @GetMapping("/{perfilId}/nome")
    public ResponseEntity<String> getPerfilNome(@PathVariable Long perfilId) {
        String nomePerfil = perfilService.findPerfilNomeById(perfilId);
        return ResponseEntity.ok(nomePerfil);
    }
}
