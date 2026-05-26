package stonetek.com.ebdsoft.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import stonetek.com.ebdsoft.dto.dtosespecificos.*;
import stonetek.com.ebdsoft.dto.mapper.AlunoMapper;
import stonetek.com.ebdsoft.dto.mapper.AulaMapper;
import stonetek.com.ebdsoft.dto.mapper.TurmaMapper;
import stonetek.com.ebdsoft.dto.request.AlunoRequest;
import stonetek.com.ebdsoft.dto.response.AlunoAulaResponse;
import stonetek.com.ebdsoft.dto.response.AlunoResponse;
import stonetek.com.ebdsoft.dto.response.AlunoTurmaResponse;
import stonetek.com.ebdsoft.exception.AlunoNotFoundException;
import stonetek.com.ebdsoft.exception.TurmaNotFoundException;
import stonetek.com.ebdsoft.model.*;
import stonetek.com.ebdsoft.repository.*;
import stonetek.com.ebdsoft.util.Area;
import stonetek.com.ebdsoft.util.FiltroAulas;
import stonetek.com.ebdsoft.util.ResourceUriUtil;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;

    private final AlunoTurmaRepository alunoTurmaRepository;

    private final AlunoAulaRepository alunoAulaRepository;

    private final TurmaRepository turmaRepository;

    private final AulaRepository aulaRepository;

    private final EbdTurmaRepository ebdTurmaRepository;

    private final IgrejaEbdRepository igrejaEbdRepository;

    private final IgrejaRepository igrejaRepository;



    public List<AlunoResponse> listar() {
        List<Aluno> alunos = alunoRepository.findAllByOrderByNomeAsc();
        return AlunoMapper.converter(alunos);
    }

    public List<Aula> buscarAulasPorFiltro(FiltroAulas filtro) {
        List<AlunoAula> alunoAulas = alunoAulaRepository.findByAlunoId(filtro.getAlunoId());
        return alunoAulas.stream()
                .map(AlunoAula::getAula)
                .filter(aula -> filtrarAulas(aula, filtro))
                .collect(Collectors.toList());
    }

    private boolean filtrarAulas(Aula aula, FiltroAulas filtro) {
        LocalDate dataAula = aula.getDia();
        if (filtro.getAno() != null) {
            if (filtro.getMes() != null) {
                return dataAula.getYear() == filtro.getAno() && dataAula.getMonthValue() == filtro.getMes();
            } else if (filtro.getTrimestre() != null) {
                int mesInicio = (filtro.getTrimestre() - 1) * 3 + 1;
                int mesFim = mesInicio + 2;
                return dataAula.getYear() == filtro.getAno() && dataAula.getMonthValue() >= mesInicio && dataAula.getMonthValue() <= mesFim;
            } else {
                return dataAula.getYear() == filtro.getAno();
            }
        }
        return true;
    }

    public AlunoResponse salvar(AlunoRequest request, Long igrejaId) {
        Aluno aluno = AlunoMapper.converter(request);

        if (igrejaId != null) {
            List<IgrejaEbd> igrejaEbdList = igrejaEbdRepository.findByIgrejaId(igrejaId);

            IgrejaEbd igrejaEbd = igrejaEbdList.stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Vínculo de Igreja com EBD não encontrado para esta igreja."));

            Ebd ebd = igrejaEbd.getEbd();

            LocalDate aniversario = request.getAniversario();
            Integer idade = Period.between(aniversario, LocalDate.now()).getYears();

            Optional<Turma> turmaOptional = ebdTurmaRepository.findTurmaByEbdIdAndIdade(ebd.getId(), idade);

            if (!turmaOptional.isPresent()) {
                throw new IllegalArgumentException("Aluno fora da faixa etária para as turmas da EBD desta igreja.");
            }

            Turma turma = turmaOptional.get();

            AlunoTurma alunoTurma = new AlunoTurma();
            alunoTurma.setAluno(aluno);
            alunoTurma.setTurma(turma);

            aluno = alunoRepository.save(aluno);
            alunoTurmaRepository.save(alunoTurma);
        } else {

            aluno = alunoRepository.save(aluno);
        }

        ResourceUriUtil.addUriInResponseHeader(aluno.getId());
        return AlunoMapper.converter(aluno);
    }


    public AlunoResponse buscarPorId(Long idAluno) {
        Optional<Aluno> aluno = alunoRepository.findById(idAluno);
        if (aluno.isEmpty()) {
            throw new AlunoNotFoundException(idAluno);
        }
        return AlunoMapper.converter(aluno.get());
    }

    public AlunoResponse editar(Long idAluno, AlunoRequest request) {
        AlunoResponse alunoEncontrado = buscarPorId(idAluno);
        Aluno aluno = AlunoMapper.converter(alunoEncontrado);
        AlunoMapper.copyToProperties(request, aluno);
        aluno = alunoRepository.save(aluno);
        return AlunoMapper.converter(aluno);
    }

    public void excluir(Long idAluno) {
        try {
            alunoRepository.deleteById(idAluno);
        } catch (EmptyResultDataAccessException ex) {
            throw new AlunoNotFoundException(idAluno);
        }
    }

    //personalizados

    public AlunoDetalhesDTO buscarDetalhesDoAluno(Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));

        List<AlunoTurma> alunoTurmas = alunoTurmaRepository.findByAlunoId(alunoId);
        List<AlunoAula> alunoAulas = alunoAulaRepository.findByAlunoId(alunoId);

        List<TurmaDTO> turmas = alunoTurmas.stream()
                .map(alunoTurma -> TurmaMapper.toDto(alunoTurma.getTurma()))
                .collect(Collectors.toList());

        List<AulaDTO> aulas = alunoAulas.stream()
                .map(alunoAula -> AulaMapper.toDto(alunoAula.getAula()))
                .collect(Collectors.toList());

        Map<Integer, Map<Integer, Map<Integer, List<AulaDTO>>>> aulasPorAnoMesTrimestre = alunoAulas.stream()
                .map(alunoAula -> AulaMapper.toDto(alunoAula.getAula()))
                .collect(Collectors.groupingBy(
                        aula -> aula.getDia().getYear(),
                        Collectors.groupingBy(
                                AulaDTO::getMes,
                                Collectors.groupingBy(
                                        AulaDTO::getTrimestre
                                )
                        )
                ));

        AlunoDetalhesDTO detalhesDTO = new AlunoDetalhesDTO();
        detalhesDTO.setAlunoId(aluno.getId());
        detalhesDTO.setNomeAluno(aluno.getNome());
        detalhesDTO.setTurmas(turmas);
        detalhesDTO.setAulas(aulas);
        detalhesDTO.setAulasPorAnoMesTrimestre(aulasPorAnoMesTrimestre);

        return detalhesDTO;
    }

    public List<AlunoDTO> buscarTodosOsAlunosComDetalhes() {
        List<Aluno> alunos = alunoRepository.findAll();
        List<AlunoDTO> alunoDTOs = new ArrayList<>();

        for (Aluno aluno : alunos) {
            List<AlunoTurma> alunoTurmas = alunoTurmaRepository.findByAlunoId(aluno.getId());
            List<AlunoAula> alunoAulas = alunoAulaRepository.findByAlunoId(aluno.getId());

            List<TurmaDTO> turmas = alunoTurmas.stream()
                    .map(alunoTurma -> TurmaMapper.toDto(alunoTurma.getTurma()))
                    .collect(Collectors.toList());

            List<AulaDTO> aulas = alunoAulas.stream()
                    .map(alunoAula -> AulaMapper.toDto(alunoAula.getAula()))
                    .collect(Collectors.toList());

            AlunoDTO alunoDTO = new AlunoDTO();
            alunoDTO.setId(aluno.getId());
            alunoDTO.setNome(aluno.getNome());
            alunoDTO.setTurmas(turmas);
            alunoDTO.setAulas(aulas);

            alunoDTOs.add(alunoDTO);
        }

        return alunoDTOs;
    }

    public AlunoTurma criarVinculoAlunoTurma(Long idAluno, Long idTurma) {
        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> new EntityNotFoundException("Aluno not found"));
        Turma turma = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new EntityNotFoundException("Turma not found"));
        AlunoTurma alunoTurma = new AlunoTurma();
        alunoTurma.setAluno(aluno);
        alunoTurma.setTurma(turma);
        return alunoTurmaRepository.save(alunoTurma);
    }

    public AlunoTurma atualizarVinculoAlunoTurma(Long id, Long idAluno, Long idTurma) {
        AlunoTurma alunoTurma = alunoTurmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AlunoTurma not found"));

        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> new EntityNotFoundException("Aluno not found"));

        Turma turma = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new EntityNotFoundException("Turma not found"));

        alunoTurma.setAluno(aluno);
        alunoTurma.setTurma(turma);

        return alunoTurmaRepository.save(alunoTurma);
    }

    public AlunoTurmaResponse findAlunoTurmaById(Long id) {
        Optional<AlunoTurma> alunoTurmaOptional = alunoTurmaRepository.findById(id);
        if (alunoTurmaOptional.isPresent()) {
            AlunoTurma alunoTurma = alunoTurmaOptional.get();
            return mapToResponse(alunoTurma);
        } else {
            throw new ResourceNotFoundException("AlunoTurma not found with id: " + id);
        }
    }

    private AlunoTurmaResponse mapToResponse(AlunoTurma alunoTurma) {
        AlunoTurmaResponse response = new AlunoTurmaResponse();
        response.setId(alunoTurma.getId());
        response.setAlunoId(alunoTurma.getAluno().getId());
        response.setTurmaId(alunoTurma.getTurma().getId());
        response.setNomeTurma(alunoTurma.getTurma().getNome());
        response.setNomeAluno(alunoTurma.getAluno().getNome());
        return response;
    }

    public List<AlunoTurmaDTO> listarAlunoETurmas() {
        List<Turma> turmas = turmaRepository.findAll();
        return turmas.stream()
                .flatMap(turma -> turma.getAlunoTurmas().stream()
                        .map(alunoTurma -> new AlunoTurmaDTO(
                                alunoTurma.getId(),
                                turma.getNome(),
                                alunoTurma.getAluno().getNome())))
                .collect(Collectors.toList());
    }

    public List<AlunoTurmaDTO> listarAlunoETurmasPorIgreja(Long igrejaId) {
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
                .flatMap(turma -> turma.getAlunoTurmas().stream()
                        .map(alunoTurma -> new AlunoTurmaDTO(
                                alunoTurma.getId(),
                                turma.getNome(),
                                alunoTurma.getAluno().getNome())))
                .collect(Collectors.toList());
    }


    //aluno e aula

    public List<AlunoTurma> criarVinculoAlunoTurma(List<Long> alunoIds, Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new EntityNotFoundException("Turma not found"));

        List<AlunoTurma> alunoTurmas = new ArrayList<>();

        for (Long alunoId : alunoIds) {
            Aluno aluno = alunoRepository.findById(alunoId)
                    .orElseThrow(() -> new EntityNotFoundException("Aluno not found"));

            boolean jaVinculado = alunoTurmaRepository.existsByAlunoAndTurma(aluno, turma);
            if (!jaVinculado) {
                AlunoTurma alunoTurma = new AlunoTurma();
                alunoTurma.setAluno(aluno);
                alunoTurma.setTurma(turma);

                alunoTurmas.add(alunoTurmaRepository.save(alunoTurma));
            }
        }

        return alunoTurmas; // Retorna a lista de AlunoTurma criados
    }

    public AlunoAula criarVinculoAlunoAula(Long idAluno, Long idAula) {
        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> new EntityNotFoundException("Aluno not found"));
        Aula aula = aulaRepository.findById(idAula)
                .orElseThrow(() -> new EntityNotFoundException("Aula not found"));
        AlunoAula alunoAula = new AlunoAula();
        alunoAula.setAluno(aluno);
        alunoAula.setAula(aula);
        return alunoAulaRepository.save(alunoAula);
    }

    public AlunoAula atualizarVinculoAlunoAula(Long id, Long idAluno, Long idAula) {
        AlunoAula alunoAula = alunoAulaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AlunoAula not found"));

        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> new EntityNotFoundException("Aluno not found"));

        Aula aula = aulaRepository.findById(idAula)
                .orElseThrow(() -> new EntityNotFoundException("Aula not found"));

        alunoAula.setAluno(aluno);
        alunoAula.setAula(aula);

        return alunoAulaRepository.save(alunoAula);
    }

    public AlunoAulaResponse findAlunoAulaById(Long id) {
        Optional<AlunoAula> alunoAulaOptional = alunoAulaRepository.findById(id);
        if (alunoAulaOptional.isPresent()) {
            AlunoAula alunoAula = alunoAulaOptional.get();
            return mapToResponse(alunoAula);
        } else {
            throw new ResourceNotFoundException("AlunoAula not found with id: " + id);
        }
    }

    private AlunoAulaResponse mapToResponse(AlunoAula alunoAula) {
        AlunoAulaResponse response = new AlunoAulaResponse();
        response.setId(alunoAula.getId());
        response.setIdAluno(alunoAula.getAluno().getId());
        response.setIdAula(alunoAula.getAula().getId());
        response.setLicao(alunoAula.getAula().getLicao());
        response.setNomeAluno(alunoAula.getAluno().getNome());
        return response;
    }

    public List<AlunoAulaDTO> listarAlunoEAulas() {
        List<Aula> aulas = aulaRepository.findAll();
        return aulas.stream()
                .flatMap(aula -> aula.getAlunoAulas().stream()
                        .map(alunoAula -> new AlunoAulaDTO(
                                alunoAula.getId(),
                                aula.getLicao(),
                                alunoAula.getAluno().getNome())))
                .collect(Collectors.toList());
    }


    public List<AlunoAulaDTO> listarAlunoEAulasPorIgreja(Long igrejaId) {
        // Busca a igreja e verifica se existe
        Igreja igreja = igrejaRepository.findById(igrejaId)
                .orElseThrow(() -> new EntityNotFoundException("Igreja não encontrada com ID: " + igrejaId));

        // Obter a Ebd vinculada a essa igreja através de IgrejaEbd
        Ebd ebd = igreja.getIgrejaEbds().stream()
                .map(IgrejaEbd::getEbd)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("EBD não encontrada para a igreja com ID: " + igrejaId));

        // Obter as turmas da Ebd
        List<Turma> turmas = ebd.getEbdTurmas().stream()
                .map(EbdTurma::getTurma)
                .collect(Collectors.toList());

        // Obter todas as aulas das turmas e os relacionamentos AlunoAula
        return turmas.stream()
                .flatMap(turma -> turma.getAulaTurmas().stream()
                        .flatMap(aulaTurma -> aulaTurma.getAula().getAlunoAulas().stream()
                                .map(alunoAula -> new AlunoAulaDTO(
                                        alunoAula.getId(),
                                        aulaTurma.getAula().getLicao(),
                                        alunoAula.getAluno().getNome()))))
                .collect(Collectors.toList());
    }


    //aluno turma

    public List<Aluno> findAlunosByTurma(Long idTurma) {
        List<AlunoTurma> alunoTurmas = alunoTurmaRepository.findByTurmaId(idTurma);
        return alunoTurmas.stream()
                .map(AlunoTurma::getAluno)
                .collect(Collectors.toList());
    }

    public Long findAlunoIdByUsuarioId(Long usuarioId) {
        return alunoRepository.findByUsuarioId(usuarioId)
                .map(Aluno::getId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado para o ID de usuário: " + usuarioId));
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public List<AlunoDTO> listarAlunosElegiveisPorTurma(Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada"));

        List<EbdTurma> ebdTurmaList = ebdTurmaRepository.findByTurmaId(turmaId);
        if (ebdTurmaList.isEmpty()) {
            throw new ResourceNotFoundException("EBD não encontrado para o Turma fornecido");
        }

        for (EbdTurma ebdTurma : ebdTurmaList) {
            Ebd ebd = ebdTurma.getEbd();
            Set<IgrejaEbd> igrejaEbds = ebd.getIgrejaEbds();

            if (!igrejaEbds.isEmpty()) {
                IgrejaEbd igrejaEbd = igrejaEbds.iterator().next();
                Igreja igreja = igrejaEbd.getIgreja();

                if (igreja != null) {
                    Area areaIgreja = igreja.getArea();
                    if (areaIgreja != null) {
                        System.out.println("Área da Igreja: " + areaIgreja.getDescricao());
                        return alunoRepository.findAll().stream()
                                .filter(aluno -> {

                                    boolean alunoNaTurmaDaIgreja = aluno.getAlunoTurmas().stream()
                                            .anyMatch(alunoTurma -> {
                                                Turma turmaAluno = alunoTurma.getTurma();
                                                return ebdTurmaRepository.findByTurmaId(turmaAluno.getId()).stream()
                                                        .anyMatch(ebdTurmas -> ebdTurma.getEbd().equals(ebd) && turmaAluno.equals(turma));
                                            });


                                    if (aluno.getArea() == null || !aluno.getArea().equals(areaIgreja)) {
                                        return false;
                                    }
                                    LocalDate aniversarioDate = aluno.getAniversario();
                                    if (aniversarioDate == null) {
                                        return false;
                                    }
                                    LocalDate aniversario = aniversarioDate;

                                    int idade = Period.between(aniversario, LocalDate.now()).getYears();
                                    return idade >= turma.getIdadeMinima() && idade <= turma.getIdadeMaxima();
                                })
                                .map(aluno -> {
                                    if (aluno.getPerfil() == null) {
                                        Perfil perfilAluno = new Perfil();
                                        perfilAluno.setNome("aluno");
                                        aluno.setPerfil(perfilAluno);
                                    }
                                    boolean matriculado = alunoTurmaRepository.existsByAlunoAndTurma(aluno, turma);
                                    AlunoDTO alunoDTO = new AlunoDTO(aluno, matriculado);
                                    alunoDTO.setPerfilNome(aluno.getPerfil().getNome());
                                    alunoDTO.setMatriculado(matriculado);

                                    return alunoDTO;
                                })
                                .collect(Collectors.toList());
                    }
                }
            }
        }

        return Collections.emptyList();
    }


    public Aluno getAlunoById(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno not found with id " + id));
    }

    public List<Aluno> listarAlunosPorIgreja(Long idIgreja) {
        return alunoRepository.findAllByIgrejaId(idIgreja);
    }


}
