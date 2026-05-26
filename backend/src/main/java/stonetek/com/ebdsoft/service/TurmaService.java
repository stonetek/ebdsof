package stonetek.com.ebdsoft.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import stonetek.com.ebdsoft.dto.dtosespecificos.AulaTurmaDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.TurmaEbdDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.TurmasEbdsDTO;
import stonetek.com.ebdsoft.dto.mapper.TurmaMapper;
import stonetek.com.ebdsoft.dto.request.TurmaRequest;
import stonetek.com.ebdsoft.dto.response.AlunoResponse;
import stonetek.com.ebdsoft.dto.response.TurmaResponse;
import stonetek.com.ebdsoft.exception.TurmaNotFoundException;
import stonetek.com.ebdsoft.model.*;
import stonetek.com.ebdsoft.repository.*;
import stonetek.com.ebdsoft.util.ResourceUriUtil;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TurmaService {

    private final TurmaRepository turmaRepository;

    private final AlunoRepository alunoRepository;

    private final AlunoTurmaRepository alunoTurmaRepository;

    private final EbdRepository ebdRepository;

    private final EbdTurmaRepository ebdTurmaRepository;

    private final IgrejaEbdRepository igrejaEbdRepository;

    public List<TurmaResponse> listar() {
        List<Turma> turmas = turmaRepository.findAllByOrderByNomeAsc();
        return TurmaMapper.converter(turmas);
    }

    /**
     * Método para salvar uma turma, caso um igrejaId seja identificado
     * imediatamente salva a turma e a vincula a ebd da igrejaId recebida.
     * @param request
     * @param igrejaId
     * @return
     */
    public TurmaResponse salvar(TurmaRequest request, Long igrejaId) {
        System.out.println("Request recebido: " + request);
        Turma turma = TurmaMapper.converter(request);

        if (igrejaId != null) {
            List<IgrejaEbd> igrejaEbdList = igrejaEbdRepository.findByIgrejaId(igrejaId);

            IgrejaEbd igrejaEbd = igrejaEbdList.stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Vínculo de Igreja com EBD não encontrado para esta igreja."));

            Ebd ebd = igrejaEbd.getEbd();
            EbdTurma ebdTurma = new EbdTurma();
            ebdTurma.setEbd(ebd);
            ebdTurma.setTurma(turma);

            turma.getEbdTurmas().add(ebdTurma);
        }

        turma = turmaRepository.save(turma);
        ResourceUriUtil.addUriInResponseHeader(turma.getId());
        return TurmaMapper.converter(turma);
    }


    /*    public TurmaResponse salvar(TurmaRequest request) {
        Turma turma = TurmaMapper.converter(request);
        turma = turmaRepository.save(turma);
        ResourceUriUtil.addUriInResponseHeader(turma.getId()); // Adiciona no header da resquição o recurso que foi
                                                                   // criado
        return TurmaMapper.converter(turma);
    }
*/
    public TurmaResponse buscarPorId(Long idTurma) {
        Optional<Turma> turma = turmaRepository.findById(idTurma);
        if (turma.isEmpty()) {
            throw new TurmaNotFoundException(idTurma);
        }
        return TurmaMapper.converter(turma.get());
    }

    public TurmaResponse editar(Long idTurma, TurmaRequest request) {
        TurmaResponse turmaEncontrado = buscarPorId(idTurma);
        Turma turma = TurmaMapper.converter(turmaEncontrado);
        TurmaMapper.copyToProperties(request, turma);
        turma = turmaRepository.save(turma);
        return TurmaMapper.converter(turma);
    }

    public void excluir(Long idTurma) {
        try {
            turmaRepository.deleteById(idTurma);
        } catch (EmptyResultDataAccessException ex) {
            throw new TurmaNotFoundException(idTurma);
        }
    }

    //personalizados

    public List<Aluno> listarAlunosPorFaixaEtaria(String nomeTurma) {
        Turma turma = turmaRepository.findByNome(nomeTurma);
        if (turma == null) {
            throw new IllegalArgumentException("Turma não encontrada");
        }

        int idadeMinima = turma.getIdadeMinima();
        int idadeMaxima = turma.getIdadeMaxima();

        List<Aluno> allAlunos = alunoRepository.findAll();
        List<Aluno> eligibleAlunos = new ArrayList<>();

        for (Aluno aluno : allAlunos) {
            int idade = calculateAge(aluno.getAniversario());
            if (idade >= idadeMinima && idade <= idadeMaxima) {
                eligibleAlunos.add(aluno);
            }
        }

        return eligibleAlunos;
    }

    private int calculateAge(LocalDate aniversario) {
        LocalDate birthDate = aniversario;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public void matricularAluno(Long alunoId, Long turmaId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        // Verifica se o aluno já está matriculado na mesma turma
        boolean jaMatriculadoNaMesmaTurma = alunoTurmaRepository.existsByAlunoAndTurma(aluno, turma);

        if (jaMatriculadoNaMesmaTurma) {
            throw new RuntimeException("Aluno já está matriculado na turma especificada");
        }

        AlunoTurma alunoTurma = new AlunoTurma();
        alunoTurma.setAluno(aluno);
        alunoTurma.setTurma(turma);

        alunoTurmaRepository.save(alunoTurma);
    }

    public void matricularAlunos(List<Long> alunosIds, Long turmaId) {
        Set<Long> uniqueAlunosIds = new HashSet<>(alunosIds);
        if (uniqueAlunosIds.size() != alunosIds.size()) {
            throw new RuntimeException("IDs de alunos duplicados fornecidos");
        }
        for (Long alunoId : uniqueAlunosIds) {
            matricularAluno(alunoId, turmaId);
        }
    }

    @Transactional
    public void vincularTurmaComEbd(String nomeTurma, String nomeEbd) {
        // Busca a turma pelo nome
        Turma turma = turmaRepository.findByNome(nomeTurma);
        if (turma == null) {
            throw new EntityNotFoundException("Turma não encontrada");
        }

        // Busca a EBD pelo nome
        Optional<Ebd> optionalEbd = ebdRepository.findByNome(nomeEbd);
        if (optionalEbd.isEmpty()) {
            throw new EntityNotFoundException("EBD não encontrada");
        }

        // Obtém o objeto Ebd do Optional
        Ebd ebd = optionalEbd.get();

        // Cria e salva o vínculo entre Turma e EBD
        EbdTurma ebdTurma = new EbdTurma();
        ebdTurma.setTurma(turma);
        ebdTurma.setEbd(ebd);

        ebdTurmaRepository.save(ebdTurma);
    }

    public List<TurmasEbdsDTO> listarEbdETurmas() {
        List<Turma> turmas = turmaRepository.findAll();
        return turmas.stream()
                .flatMap(turma -> turma.getEbdTurmas().stream()
                        .map(ebdTurma -> new TurmasEbdsDTO(
                                ebdTurma.getId(),
                                turma.getNome(),
                                ebdTurma.getEbd().getNome()
                                )))
                .collect(Collectors.toList());
    }

    public List<TurmaEbdDTO> listarEbdETurmasPorEbdId(Long ebdId) {
        List<EbdTurma> ebdTurmas = ebdTurmaRepository.findByEbdId(ebdId);
        return ebdTurmas.stream()
                .map(ebdTurma -> new TurmaEbdDTO(
                        ebdTurma.getId(), // ID da EbdTurma
                        ebdTurma.getTurma().getNome(), // Nome da turma
                        ebdTurma.getEbd().getNome(),
                        ebdTurma.getTurma().getId(),
                        ebdTurma.getEbd().getId()
                ))
                .collect(Collectors.toList());
    }


    @Transactional
    public Turma salvarVinculo(Long turmaId, Long ebdId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada"));

        Ebd ebd = ebdRepository.findById(ebdId)
                .orElseThrow(() -> new IllegalArgumentException("EBD não encontrada"));
        EbdTurma ebdTurma = new EbdTurma();
        ebdTurma.setTurma(turma);
        ebdTurma.setEbd(ebd);

        turma.getEbdTurmas().add(ebdTurma);
        return turmaRepository.save(turma);
    }

    //turma e aluno
    public List<AlunoResponse> getAlunosByTurmaId(Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new TurmaNotFoundException(turmaId));

        List<Aluno> alunos = alunoRepository.findByTurmasContains(turma);

        return alunos.stream()
                .map(aluno -> new AlunoResponse(
                        aluno.getId(),
                        aluno.getNome(),
                        aluno.getAniversario(),
                        aluno.getNovoConvertido(),
                        aluno.getSexo()
                        //aluno.isPresente()
                ))
                .collect(Collectors.toList());
    }

    //turma e aula

    public List<AulaTurmaDTO> buscarAulasPorTurmaId(Long idTurma) {
        Optional<Turma> turmaOptional = turmaRepository.findById(idTurma);
        if (turmaOptional.isPresent()) {
            Turma turma = turmaOptional.get();
            return turma.getAulaTurmas().stream()
                    .map(aulaTurma -> new AulaTurmaDTO(
                            aulaTurma.getId(),
                            aulaTurma.getAula().getLicao(),
                            turma.getNome()
                    ))
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("Turma não encontrada com o ID: " + idTurma);
        }
    }

   /* public List<TurmaResponse> listarTurmasPorIgreja(Long igrejaId) {
        List<IgrejaEbd> igrejaEbds = igrejaEbdRepository.findByIgrejaId(igrejaId);

        // Corrigir o mapeamento para obter os IDs de Ebd
        List<Long> ebdIds = igrejaEbds.stream()
                .map(igrejaEbd -> igrejaEbd.getEbd().getId())
                .collect(Collectors.toList());

        List<EbdTurma> ebdTurmas = ebdTurmaRepository.findByEbdIdIn(ebdIds);
        List<Turma> turmas = ebdTurmas.stream()
                .map(EbdTurma::getTurma)
                .collect(Collectors.toList());

        return turmas.stream()
                .map(turma -> new TurmaResponse(turma.getId(), turma.getNome()))
                .collect(Collectors.toList());
    } */

}
