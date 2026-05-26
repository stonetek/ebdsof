package stonetek.com.ebdsoft.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import stonetek.com.ebdsoft.dto.dtosespecificos.*;
import stonetek.com.ebdsoft.dto.mapper.ProfessorMapper;
import stonetek.com.ebdsoft.dto.request.ProfessorRequest;
import stonetek.com.ebdsoft.dto.response.*;
import stonetek.com.ebdsoft.exception.IgrejaAlreadyEnrolledException;
import stonetek.com.ebdsoft.exception.ProfessorNotFoundException;
import stonetek.com.ebdsoft.model.*;
import stonetek.com.ebdsoft.repository.*;
import stonetek.com.ebdsoft.util.ResourceUriUtil;
import stonetek.com.ebdsoft.util.TrimestreUtils;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    private final ProfessorAulaRepository professorAulaRepository;

    private final ProfessorTurmaRepository professorTurmaRepository;

    private final AlunoAulaRepository alunoAulaRepository;

    private final AulaRepository aulaRepository;

    private final TurmaRepository turmaRepository;

    private final AulaTurmaRepository aulaTurmaRepository;

    private final IgrejaEbdRepository igrejaEbdRepository;

    private final EbdTurmaRepository ebdTurmaRepository;

    private final IgrejaRepository igrejaRepository;

    private final AlunoService alunoService;

    private final AlunoTurmaRepository alunoTurmaRepository;


    public List<ProfessorResponse> listar() {
        List<Professor> professors = professorRepository.findAllByOrderByNomeAsc();
        return ProfessorMapper.converter(professors);
    }

    public ProfessorResponse salvar(ProfessorRequest request) {
        Professor professor = ProfessorMapper.converter(request);

        if (request.getIgrejaId() != null) {
            List<IgrejaEbd> igrejaEbdList = igrejaEbdRepository.findByIgrejaId(request.getIgrejaId());

            IgrejaEbd igrejaEbd = igrejaEbdList.stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Vínculo de Igreja com EBD não encontrado para esta igreja."));

            Ebd ebd = igrejaEbd.getEbd();
            EbdTurma ebdTurma = ebdTurmaRepository.findOptionalByTurmaId(request.getTurmaId())
                    .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada."));
            if (!ebdTurma.getEbd().getId().equals(ebd.getId())) {
                throw new IllegalArgumentException("Turma não associada à EBD da igreja informada.");
            }

            Turma turmaSelecionada = ebdTurma.getTurma();

            ProfessorTurma professorTurma = new ProfessorTurma();
            professorTurma.setProfessor(professor);
            professorTurma.setTurma(turmaSelecionada);

            professor = professorRepository.save(professor);
            professorTurmaRepository.save(professorTurma);
        } else {
            professor = professorRepository.save(professor);
        }

        ResourceUriUtil.addUriInResponseHeader(professor.getId());
        return ProfessorMapper.converter(professor);
    }

    /* public ProfessorResponse salvar(ProfessorRequest request) {
        Professor professor = ProfessorMapper.converter(request);
        professor = professorRepository.save(professor);
        ResourceUriUtil.addUriInResponseHeader(professor.getId()); // Adiciona no header da resquição o recurso que foi
                                                                   // criado
        return ProfessorMapper.converter(professor);
    } */

    public ProfessorResponse buscarPorId(Long idProfessor) {
        Optional<Professor> professor = professorRepository.findById(idProfessor);
        if (professor.isEmpty()) {
            throw new ProfessorNotFoundException(idProfessor);
        }
        return ProfessorMapper.converter(professor.get());
    }

    public ProfessorResponse editar(Long idProfessor, ProfessorRequest request) {
        ProfessorResponse professorEncontrado = buscarPorId(idProfessor);
        Professor professor = ProfessorMapper.converter(professorEncontrado);
        ProfessorMapper.copyToProperties(request, professor);
        professor = professorRepository.save(professor);
        return ProfessorMapper.converter(professor);
    }

    public void excluir(Long idProfessor) {
        try {
            professorRepository.deleteById(idProfessor);
        } catch (EmptyResultDataAccessException ex) {
            throw new ProfessorNotFoundException(idProfessor);
        }
    }





    //Professor e Turma******************************

    public ProfessorTurma criarVinculoProfessorTurma(Long idProfessor, Long idTurma) {
        Professor professor = professorRepository.findById(idProfessor)
                .orElseThrow(() -> new EntityNotFoundException("Professor not found"));
        Turma turma = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new EntityNotFoundException("Turma not found"));


        boolean exists = professorTurmaRepository.existsByProfessorAndTurma(professor, turma);
        if (exists) {
            throw new IgrejaAlreadyEnrolledException("A turma com id: " + idTurma + " já está vinculada com o professor de id: " + idProfessor);
        }

        ProfessorTurma professorTurma = new ProfessorTurma();
        professorTurma.setProfessor(professor);
        professorTurma.setTurma(turma);


        return professorTurmaRepository.save(professorTurma);
    }


    public ProfessorTurma atualizarVinculoProfessorTurma(Long id, Long idProfessor, Long idTurma) {
        ProfessorTurma professorTurma = professorTurmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProfessorTurma not found"));

        Professor professor = professorRepository.findById(idProfessor)
                .orElseThrow(() -> new EntityNotFoundException("Professor not found"));

        Turma turma = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new EntityNotFoundException("Turma not found"));

        professorTurma.setProfessor(professor);
        professorTurma.setTurma(turma);

        return professorTurmaRepository.save(professorTurma);
    }

    public ProfessorTurmaResponse findProfessorTurmaById(Long id) {
        Optional<ProfessorTurma> professorTurmaOptional = professorTurmaRepository.findById(id);
        if (professorTurmaOptional.isPresent()) {
            ProfessorTurma professorTurma = professorTurmaOptional.get();
            return mapToResponse(professorTurma);
        } else {
            throw new ResourceNotFoundException("ProfessorTurma not found with id: " + id);
        }
    }

    private ProfessorTurmaResponse mapToResponse(ProfessorTurma professorTurma) {
        ProfessorTurmaResponse response = new ProfessorTurmaResponse();
        response.setId(professorTurma.getId());
        response.setIdProfessor(professorTurma.getProfessor().getId());
        response.setIdTurma(professorTurma.getTurma().getId());
        response.setNomeProfessor(professorTurma.getProfessor().getNome()); // Assumindo que a entidade Ebd tem um campo nome
        response.setNomeTurma(professorTurma.getTurma().getNome()); // Assumindo que a entidade Igreja tem um campo nome
        return response;
    }

    public List<ProfessorTurmaDTO> listarprofessoresETurmas() {
        List<Professor> professores = professorRepository.findAll();

        // Mapear para DTOs que contenham id, nome da Turma e nome da Ebd
        return professores.stream()
                .flatMap(professor -> professor.getProfessorTurmas().stream()
                        .map(professorTurma -> new ProfessorTurmaDTO(
                                professorTurma.getId(),
                                professor.getNome(),
                                professorTurma.getTurma().getNome())))
                .collect(Collectors.toList());
    }


    public List<Professor> findProfessoresByTurmaId(Long idTurma) {
        Turma turma = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada"));

        return turma.getProfessorTurmas().stream()
                .map(ProfessorTurma::getProfessor)
                .collect(Collectors.toList());
    }

    public List<ProfessorTurmaDTO> listarPorfessorETurmaPorIgreja (Long igrejaId) {
        Optional<Igreja> igrejaOptional = igrejaRepository.findById(igrejaId);
        if (igrejaOptional.isEmpty()) {
            throw new EntityNotFoundException("Igreja não encontrada para o ID: " + igrejaId);
        }

        Igreja igreja = igrejaOptional.get();
        Optional<IgrejaEbd> igrejaEbdOptional = igreja.getIgrejaEbds().stream()
                .findFirst();

        if (igrejaEbdOptional.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma EBD associada à igreja com ID: " + igrejaId);
        }
        Ebd ebd = igrejaEbdOptional.get().getEbd();
        List<Turma> turmas = ebd.getEbdTurmas().stream()
                .map(EbdTurma::getTurma)
                .collect(Collectors.toList());
        return turmas.stream()
                .flatMap(turma ->
                        turma.getProfessorTurmas().stream()
                                .map(professorTurma -> new ProfessorTurmaDTO(
                                        professorTurma.getId(),
                                        turma.getNome(),
                                        professorTurma.getProfessor().getNome())))
                .collect(Collectors.toList());

    }

    public Set<Turma> getTurmasByProfessorId(Long professorId) {
        return professorTurmaRepository.findTurmasByProfessorId(professorId);
    }


    //professor e aulas*******************************

    public ProfessorAula criarVinculoProfessorAula(Long idProfessor, Long idAula) {
        Professor professor = professorRepository.findById(idProfessor)
                .orElseThrow(() -> new EntityNotFoundException("Professor not found"));
        Aula aula = aulaRepository.findById(idAula)
                .orElseThrow(() -> new EntityNotFoundException("Aula not found"));


        boolean exists = professorAulaRepository.existsByProfessorAndAula(professor, aula);
        if (exists) {
            throw new IgrejaAlreadyEnrolledException("A aula com id: " + idAula + " já está vinculada com o professor de id: " + idProfessor);
        }

        ProfessorAula professorAula = new ProfessorAula();
        professorAula.setProfessor(professor);
        professorAula.setAula(aula);


        return professorAulaRepository.save(professorAula);
    }


    public ProfessorAula atualizarVinculoProfessorAula(Long id, Long idProfessor, Long idAula) {
        ProfessorAula professorAula = professorAulaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProfessorAula not found"));

        Professor professor = professorRepository.findById(idProfessor)
                .orElseThrow(() -> new EntityNotFoundException("Professor not found"));

        TrimestreRequest currentTrimestre = TrimestreUtils.getCurrentTrimestre();
        List<Aula> aulasInTrimestre = aulaRepository.findByTrimestreAndAno(currentTrimestre.getTrimestre(), currentTrimestre.getAno());

        Aula aula = aulasInTrimestre.stream()
                .filter(a -> a.getId().equals(idAula))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Aula not found in the current trimester"));

        professorAula.setProfessor(professor);
        professorAula.setAula(aula);

        return professorAulaRepository.save(professorAula);
    }

    public List<ProfessorAulaDTO> listarProfessorEAulasPorIgreja(Long igrejaId) {
        Igreja igreja = igrejaRepository.findById(igrejaId)
                .orElseThrow(() -> new EntityNotFoundException("Igreja não encontrada com ID: " + igrejaId));

        Ebd ebd = igreja.getIgrejaEbds().stream()
                .map(IgrejaEbd::getEbd)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("EBD não encontrada para a igreja com ID: " + igrejaId));

        List<Turma> turmas = ebd.getEbdTurmas().stream()
                .map(EbdTurma::getTurma)
                .collect(Collectors.toList());

        return turmas.stream()
                .flatMap(turma -> turma.getAulaTurmas().stream()
                        .flatMap(aulaTurma -> aulaTurma.getAula().getProfessorAulas().stream()
                                .map(professorAula -> new ProfessorAulaDTO(
                                        professorAula.getId(),
                                        professorAula.getAula().getLicao(),
                                        professorAula.getProfessor().getNome()))))
                .collect(Collectors.toList());
    }


    public ProfessorAulaResponse findProfessorAulaById(Long id) {
        Optional<ProfessorAula> professorAulaOptional = professorAulaRepository.findById(id);
        if (professorAulaOptional.isPresent()) {
            ProfessorAula professorAula = professorAulaOptional.get();
            return mapToResponse(professorAula);
        } else {
            throw new ResourceNotFoundException("ProfessorTurma not found with id: " + id);
        }
    }

    private ProfessorAulaResponse mapToResponse(ProfessorAula professorAula) {
        ProfessorAulaResponse response = new ProfessorAulaResponse();
        response.setId(professorAula.getId());
        response.setIdProfessor(professorAula.getProfessor().getId());
        response.setIdAula(professorAula.getAula().getId());
        response.setNomeProfessor(professorAula.getProfessor().getNome()); // Assumindo que a entidade Ebd tem um campo nome
        response.setLicao(professorAula.getAula().getLicao()); // Assumindo que a entidade Igreja tem um campo nome
        return response;
    }

    public List<ProfessorAulaDTO> listarprofessoresEAulas() {
        List<Professor> professores = professorRepository.findAll();
        return professores.stream()
                .flatMap(professor -> professor.getProfessorAulas().stream()
                        .map(professorAula -> new ProfessorAulaDTO(
                                professorAula.getId(),
                                professor.getNome(),
                                professorAula.getAula().getLicao())))
                .collect(Collectors.toList());
    }


    public List<AulaDTO> listarAulasComDetalhes(String userProfile, Usuario usuario, Long igrejaId) {
        List<Aula> aulas;

        if ("PROFESSOR".equalsIgnoreCase(userProfile)) {
            List<Turma> turmas = professorTurmaRepository.findByProfessorId(usuario.getId())
                    .stream()
                    .map(ProfessorTurma::getTurma)
                    .collect(Collectors.toList());

            if (!turmas.isEmpty()) {
                List<AulaTurma> aulaTurmas = aulaTurmaRepository.findByTurmas(turmas);

                aulas = aulaTurmas.stream()
                        .map(AulaTurma::getAula)
                        .distinct()
                        .collect(Collectors.toList());
            } else {
                aulas = Collections.emptyList();
            }
        } else {
            aulas = aulaRepository.findAll();
        }

        if (igrejaId != null) {
            aulas = aulas.stream()
                    .filter(aula -> aula.getAulaTurmas().stream()
                            .anyMatch(aulaTurma -> aulaTurma.getTurma().getEbdTurmas().stream()
                                    .anyMatch(ebdTurma -> ebdTurma.getEbd().getIgrejaEbds().stream()
                                            .anyMatch(igrejaEbd -> igrejaEbd.getIgreja().getId().equals(igrejaId)))))
                    .collect(Collectors.toList());
        }

        return aulas.stream().map(aula -> {
            List<Aluno> alunos = alunoAulaRepository.findByAulaId(aula.getId())
                    .stream()
                    .map(AlunoAula::getAluno)
                    .collect(Collectors.toList());

            List<Professor> professores = professorAulaRepository.findByAulaId(aula.getId())
                    .stream()
                    .map(ProfessorAula::getProfessor)
                    .collect(Collectors.toList());

            List<Turma> turmas = professores.stream()
                    .flatMap(professor -> professorTurmaRepository.findByProfessorId(professor.getId()).stream())
                    .map(ProfessorTurma::getTurma)
                    .distinct()
                    .collect(Collectors.toList());

            return new AulaDTO(aula, alunos, professores, turmas);
        }).collect(Collectors.toList());
    }



    public Long findProfessorIdByUsuarioId(Long usuarioId) {
        return professorRepository.findByUsuarioId(usuarioId)
                .map(Professor::getId)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado para o ID de usuário: " + usuarioId));
    }

    public Professor getProfessorById(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found with id " + id));
    }

    public List<AulaResponse> listarAulasPorProfessor(Long professorId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado: " + professorId));

        Set<ProfessorAula> professorAulas = professor.getProfessorAulas();
        List<Aula> aulas = professorAulas.stream()
                .map(ProfessorAula::getAula)
                .collect(Collectors.toList());

        // Converta as aulas para AulaResponse
        return aulas.stream()
                .map(aula -> new AulaResponse(aula, getAlunosPorTurma(aula)))
                .collect(Collectors.toList());
    }

    private List<AlunoResponse> getAlunosPorTurma(Aula aula) {
        List<AlunoResponse> alunosPorTurma = new ArrayList<>();
        for (AulaTurma aulaTurma : aula.getAulaTurmas()) {
            List<Aluno> alunos = alunoService.findAlunosByTurma(aulaTurma.getTurma().getId());
            alunosPorTurma.addAll(alunos.stream()
                    .map(AlunoResponse::new)
                    .collect(Collectors.toList()));
        }
        return alunosPorTurma;
    }

    //Professor Igreja*********************************************

    public List<Professor> ListarProfessorPorIgreja(Long idIgreja) {
        return professorRepository.findAllByProfessorIgrejaId(idIgreja);
    }

    // Professor Aluno********************************************

    public Set<Aluno> buscarAlunosPorProfessor(Long professorId) {
        return alunoTurmaRepository.findAlunosByProfessorId(professorId);
    }


}
