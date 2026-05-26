package stonetek.com.ebdsoft.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stonetek.com.ebdsoft.dto.dtosespecificos.PerfilAssignmentRequestDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.UsuarioAssignmentRequestDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.UsuarioNomeDTO;
import stonetek.com.ebdsoft.dto.request.UsuarioRequest;
import stonetek.com.ebdsoft.dto.response.UsuarioResponse;
import stonetek.com.ebdsoft.model.Aluno;
import stonetek.com.ebdsoft.model.Perfil;
import stonetek.com.ebdsoft.model.Professor;
import stonetek.com.ebdsoft.model.Usuario;
import stonetek.com.ebdsoft.repository.AlunoRepository;
import stonetek.com.ebdsoft.repository.ProfessorRepository;
import stonetek.com.ebdsoft.repository.UsuarioRepository;
import stonetek.com.ebdsoft.service.UsuarioService;
import stonetek.com.ebdsoft.util.Usuarioable;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final UsuarioRepository usuarioRepository;

    private final ProfessorRepository professorRepository;

    private final AlunoRepository alunoRepository;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok().body(usuarioService.listar());
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long idUsuario) {
        UsuarioResponse usuario = usuarioService.buscarPorId(idUsuario);
        return ResponseEntity.ok().body(usuario);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> salvar(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse usuario = usuarioService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> editar(@PathVariable Long idUsuario,
                                                @Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse usuario = usuarioService.editar(idUsuario, request);
        return ResponseEntity.ok().body(usuario);
    }

    @DeleteMapping("/{idUsuario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long idUsuario) {
        usuarioService.excluir(idUsuario);
    }


    @PostMapping("/vincular")
    public ResponseEntity<String> vincularUsuario(@RequestBody UsuarioAssignmentRequestDTO request) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(request.getUsuarioId());
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        Usuario usuario = usuarioOptional.get();
        if ("aluno".equalsIgnoreCase(request.getTipoEntidade())) {
            Optional<Aluno> alunoOptional = alunoRepository.findById(request.getEntidadeId());
            if (alunoOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluno não encontrado");
            }
            Aluno aluno = alunoOptional.get();
            usuarioService.vincularUsuario(aluno, usuario);
        } else if ("professor".equalsIgnoreCase(request.getTipoEntidade())) {
            Optional<Professor> professorOptional = professorRepository.findById(request.getEntidadeId());
            if (professorOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Professor não encontrado");
            }
            Professor professor = professorOptional.get();
            usuarioService.vincularUsuario(professor, usuario);
        } else {
            return ResponseEntity.badRequest().body("Tipo de entidade inválido. Use 'aluno' ou 'professor'.");
        }
        return ResponseEntity.ok("Perfil vinculado com sucesso!");
    }

    @PostMapping("/pornome")
    public List<Usuario> getUsuariosByNome(@RequestBody UsuarioNomeDTO usuarioNomeDTO) {
        return usuarioService.findUsuariosByNome(usuarioNomeDTO.getNome());
    }

    @GetMapping("/igreja/{igrejaId}")
    public ResponseEntity<List<Usuario>> listarPorIgreja(@PathVariable Long igrejaId) {
        List<Usuario> usuarios = usuarioService.buscarUsuariosPorIgreja(igrejaId);
        return ResponseEntity.ok(usuarios);
    }

}
