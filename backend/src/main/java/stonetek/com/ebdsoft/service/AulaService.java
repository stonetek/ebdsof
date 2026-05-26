package stonetek.com.ebdsoft.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import stonetek.com.ebdsoft.dto.dtosespecificos.AulaTurmaDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.OfertaResponse;
import stonetek.com.ebdsoft.dto.dtosespecificos.ProfessorTurmaDTO;
import stonetek.com.ebdsoft.dto.mapper.AulaMapper;
import stonetek.com.ebdsoft.dto.request.AlunoAulaRequest;
import stonetek.com.ebdsoft.dto.request.AulaRequest;
import stonetek.com.ebdsoft.dto.request.AulaTurmaRequest;
import stonetek.com.ebdsoft.dto.request.ProfessorAulaRequest;
import stonetek.com.ebdsoft.dto.response.*;
import stonetek.com.ebdsoft.exception.AulaNotFoundException;
import stonetek.com.ebdsoft.exception.IgrejaAlreadyEnrolledException;
import stonetek.com.ebdsoft.model.*;
import stonetek.com.ebdsoft.repository.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AulaService {

    private final AulaRepository aulaRepository;

    private final AulaTurmaRepository aulaTurmaRepository;

    private final AlunoRepository alunoRepository;

    private final AlunoAulaRepository alunoAulaRepository;

    private final AlunoTurmaRepository alunoTurmaRepository;

    private final ProfessorRepository professorRepository;

    private final ProfessorAulaRepository professorAulaRepository;

    private final TurmaRepository turmaRepository;

    private final EbdTurmaRepository ebdTurmaRepository;

    private final IgrejaEbdRepository igrejaEbdRepository;






    /**
     * Busca de Aulas: Recupera todas as aulas do repositório (aulaRepository) ordenadas pelo campo licao em ordem ascendente.
     * Conversão de Entidades: Converte cada entidade Aula para um objeto AulaResponse usando o método converte do AulaMapper.
     * Retorno da Lista: Retorna a lista de objetos AulaResponse.
     * @return
     */

    public List<AulaResponse> listar() {
        List<Aula> aulas = aulaRepository.findAllByOrderByLicaoAsc();
        return aulas.stream().map(AulaMapper::converte).collect(Collectors.toList());
    }

    /**
     * Busca de Aula por ID: Tenta encontrar uma aula no repositório (aulaRepository) pelo ID fornecido (idAula).
     * Verificação de Existência: Verifica se a aula foi encontrada. Se não for encontrada, lança uma exceção AulaNotFoundException.
     * Conversão de Entidade: Converte a entidade Aula encontrada para um objeto AulaResponse usando o método converter do AulaMapper.
     * Retorno do Objeto: Retorna o objeto AulaResponse.
     * @param idAula
     * @return
     */


    public AulaResponse buscarPorId(Long idAula) {
        Optional<Aula> aula = aulaRepository.findById(idAula);
        if (aula.isEmpty()) {
            throw new AulaNotFoundException(idAula);
        }
        return AulaMapper.converter(aula.get());
    }

    public void excluir(Long idAula) {
        try {
            aulaRepository.deleteById(idAula);
        } catch (EmptyResultDataAccessException ex) {
            throw new AulaNotFoundException(idAula);
        }
    }

    // personalizados

    @Transactional
    public AulaResponse salvar(AulaRequest request) {
        Aula aula = new Aula();
        aula.setLicao(request.getLicao());
        aula.setDia(request.getDia());
        aula.setTrimestre(request.getTrimestre());
        aula.setAlunosMatriculados(request.getAlunosMatriculados());
        aula.setTrimestre(request.getTrimestre());
        aula.setAusentes(request.getAusentes());
        aula.setPresentes(request.getPresentes());
        aula.setVisitantes(request.getVisitantes());
        aula.setTotalAssistencia(request.getTotalAssistencia());
        aula.setBiblias(request.getBiblias());
        aula.setRevistas(request.getRevistas());
        aula.setOferta(request.getOferta());

        Aula savedAula = aulaRepository.save(aula);

        Set<ProfessorAula> professorAulas = new HashSet<>();
        if (request.getProfessorAulas() != null && !request.getProfessorAulas().isEmpty()) {
            for (ProfessorAulaRequest professorAulaRequest : request.getProfessorAulas()) {

                Professor professor = professorRepository.findById(professorAulaRequest.getIdProfessor())
                        .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado"));

                boolean professorExists = professorAulaRepository.existsByProfessorAndAula(professor, savedAula);
                if (!professorExists) {
                    ProfessorAula professorAula = new ProfessorAula();
                    professorAula.setAula(savedAula);
                    professorAula.setProfessor(professor);
                    professorAulaRepository.save(professorAula);
                    professorAulas.add(professorAula);
                }
            }
        }

        Set<AulaTurma> aulaTurmas = new HashSet<>();

        if (request.getAulaTurmas() != null && !request.getAulaTurmas().isEmpty()) {

            AulaTurmaRequest aulaTurmaRequest = request.getAulaTurmas().iterator().next();
            Turma turma = turmaRepository.findById(aulaTurmaRequest.getIdTurma())
                    .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada"));

            boolean turmaExists = aulaTurmaRepository.existsByTurmaAndAula(turma, savedAula);
            if (!turmaExists) {
                AulaTurma aulaTurma = new AulaTurma();
                aulaTurma.setAula(savedAula);
                aulaTurma.setTurma(turma);
                aulaTurmaRepository.save(aulaTurma);
                aulaTurmas.add(aulaTurma);
            }
        }

        Set<AlunoAula> alunoAulas = new HashSet<>();
        if (request.getAlunoAulas() != null && !request.getAlunoAulas().isEmpty()) {
            for (AlunoAulaRequest alunoAulaRequest : request.getAlunoAulas()) {
                Aluno aluno = alunoRepository.findById(alunoAulaRequest.getIdAluno())
                        .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

                boolean alunoExists = alunoAulaRepository.existsByAlunoAndAula(aluno, savedAula);
                if (!alunoExists) {
                    AlunoAula alunoAula = new AlunoAula();
                    alunoAula.setAula(savedAula);
                    alunoAula.setAluno(aluno);
                    alunoAula.setPresente(alunoAulaRequest.isPresente());
                    alunoAulaRepository.save(alunoAula);
                    alunoAulas.add(alunoAula);
                }
            }
        }

        savedAula.setProfessorAulas(professorAulas);
        savedAula.setAulaTurmas(aulaTurmas);
        savedAula.setAlunoAulas(alunoAulas);

        AulaResponse response = new AulaResponse(savedAula,
                new ArrayList<>(aulaTurmas.stream()
                        .map(AulaTurma::getTurma)
                        .collect(Collectors.toList())),
                new ArrayList<>());

        return response;
    }

    @Transactional
    public AulaResponse editar(Long idAula, AulaRequest request) {
        Aula aula = aulaRepository.findById(idAula)
                .orElseThrow(() -> new IllegalArgumentException("Aula não encontrada"));

        aula.setLicao(request.getLicao());
        aula.setDia(request.getDia());
        aula.setTrimestre(request.getTrimestre());
        aula.setAlunosMatriculados(request.getAlunosMatriculados());
        aula.setAusentes(request.getAusentes());
        aula.setPresentes(request.getPresentes());
        aula.setVisitantes(request.getVisitantes());
        aula.setTotalAssistencia(request.getTotalAssistencia());
        aula.setBiblias(request.getBiblias());
        aula.setRevistas(request.getRevistas());
        aula.setOferta(request.getOferta());

        aula.getProfessorAulas().clear();
        if (request.getProfessorAulas() != null && !request.getProfessorAulas().isEmpty()) {
            ProfessorAulaRequest professorAulaRequest = request.getProfessorAulas().iterator().next();
            Long professorId = professorAulaRequest.getIdProfessor();
            System.out.println("Professor ID recebido: " + professorId);

            Professor professor = professorRepository.findById(professorId)
                    .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado"));

            boolean professorExists = professorAulaRepository.existsByProfessorAndAula(professor, aula);
            if (!professorExists) {
                ProfessorAula professorAula = new ProfessorAula();
                professorAula.setAula(aula);
                professorAula.setProfessor(professor);
                professorAulaRepository.save(professorAula);
                aula.getProfessorAulas().add(professorAula);
            }
        }

        aula.getAulaTurmas().clear();
        if (request.getAulaTurmas() != null && !request.getAulaTurmas().isEmpty()) {
            AulaTurmaRequest aulaTurmaRequest = request.getAulaTurmas().iterator().next();
            Turma turma = turmaRepository.findById(aulaTurmaRequest.getIdTurma())
                    .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada"));

            boolean turmaExists = aulaTurmaRepository.existsByTurmaAndAula(turma, aula);
            if (!turmaExists) {
                AulaTurma aulaTurma = new AulaTurma();
                aulaTurma.setAula(aula);
                aulaTurma.setTurma(turma);
                aulaTurmaRepository.save(aulaTurma);
                aula.getAulaTurmas().add(aulaTurma);
            }
        }

        aula.getAlunoAulas().clear();
        if (request.getAlunoAulas() != null && !request.getAlunoAulas().isEmpty()) {
            for (AlunoAulaRequest alunoAulaRequest : request.getAlunoAulas()) {
                Aluno aluno = alunoRepository.findById(alunoAulaRequest.getIdAluno())
                        .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

                // Verificação se já existe a associação do aluno com a aula
                boolean alunoExists = alunoAulaRepository.existsByAlunoAndAula(aluno, aula);
                if (!alunoExists) {
                    AlunoAula alunoAula = new AlunoAula();
                    alunoAula.setAula(aula);
                    alunoAula.setAluno(aluno);
                    alunoAula.setPresente(alunoAulaRequest.isPresente());
                    alunoAulaRepository.save(alunoAula);
                    aula.getAlunoAulas().add(alunoAula);
                }
            }
        }

        Aula updatedAula = aulaRepository.save(aula);

        AulaResponse response = new AulaResponse(updatedAula,
                new ArrayList<>(updatedAula.getAulaTurmas().stream()
                        .map(AulaTurma::getTurma)
                        .collect(Collectors.toList())),
                new ArrayList<>());

        return response;
    }

    public List<AulaResponse> getAllAulas() {
        List<Aula> aulas = aulaRepository.findAll();
        List<AulaResponse> aulaResponses = new ArrayList<>();

        for (Aula aula : aulas) {
            List<AulaTurma> aulaTurmas = aulaTurmaRepository.findByAulaId(aula.getId());

            List<Turma> turmas = aulaTurmas.stream()
                    .map(AulaTurma::getTurma)
                    .collect(Collectors.toList());

            List<AlunoResponse> alunosPorTurma = new ArrayList<>();
            for (AulaTurma aulaTurma : aulaTurmas) {
                List<AlunoTurma> alunosTurma = alunoTurmaRepository.findByTurma(aulaTurma.getTurma());
                for (AlunoTurma alunoTurma : alunosTurma) {
                    AlunoResponse alunoResponse = new AlunoResponse();
                    alunoResponse.setId(alunoTurma.getAluno().getId());
                    alunoResponse.setNome(alunoTurma.getAluno().getNome());
                    alunosPorTurma.add(alunoResponse);
                }
            }

            AulaResponse response = new AulaResponse(aula, turmas, alunosPorTurma);
            aulaResponses.add(response);
        }

        return aulaResponses;
    }



    public List<OfertaResponse> somaOfertasPorTurmaEMesETrimestre(Long turmaId, Integer mes, Integer trimestre, Integer ano) {
        List<OfertaResponse> ofertas = new ArrayList<>();

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma not found"));

        Map<Integer, BigDecimal> somaOfertasPorMes = new HashMap<>(); // Mapeia mês para soma de ofertas

        for (AulaTurma aulaTurma : turma.getAulaTurmas()) {
            Aula aula = aulaTurma.getAula();
            int aulaMes = getMonth(aula.getDia());
            int aulaTrimestre = getTrimestre(aula.getDia());

            boolean matches = (mes != null && aulaMes == mes) ||
                    (trimestre != null && aulaTrimestre == trimestre);

            if (matches && getYear(aula.getDia()) == ano) {
                BigDecimal oferta = BigDecimal.valueOf(aula.getOferta());
                somaOfertasPorMes.merge(aulaMes, oferta, BigDecimal::add); // Soma a oferta para o mês
            }
        }

        for (Map.Entry<Integer, BigDecimal> entry : somaOfertasPorMes.entrySet()) {
            OfertaResponse response = new OfertaResponse();
            response.setAno(ano);
            response.setMes(entry.getKey());
            response.setTrimestre(trimestre);
            response.setTotalOfertas(entry.getValue());
            ofertas.add(response);
        }

        return ofertas;
    }



    // Método auxiliar para obter o mês a partir de uma data
    private int getMonth(LocalDate date) {
        return date.getMonthValue(); // getMonthValue() retorna o mês como um int de 1 a 12
    }

    /** Método auxiliar para obter o trimestre a partir de uma data */
    private int getTrimestre(LocalDate date) {
        int mes = date.getMonthValue(); // getMonthValue() retorna o mês como um int de 1 a 12
        return (mes - 1) / 3 + 1;
    }

    /** Método auxiliar para obter o ano a partir de uma data */
    private int getYear(LocalDate date) {
        return date.getYear(); // getYear() retorna o ano como um int
    }


    public List<Aula> findByTrimestreAndAno(String trimestre, Integer ano) {
        return aulaRepository.findByTrimestreAndAno(trimestre, ano);
    }

    // aula e turmas

    public AulaTurma criarVinculoAulaTurma(Long idAula, Long idTurma) {
        Aula aula = aulaRepository.findById(idAula)
                .orElseThrow(() -> new EntityNotFoundException("Aula not found"));
        Turma turma = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new EntityNotFoundException("Turma not found"));


        boolean exists = aulaTurmaRepository.existsByAulaAndTurma(aula, turma);
        if (exists) {
            throw new IgrejaAlreadyEnrolledException("A aula com id: " + idAula + " já está vinculada com a turma de id: " + idTurma);
        }

        AulaTurma aulaTurma = new AulaTurma();
        aulaTurma.setAula(aula);
        aulaTurma.setTurma(turma);


        return aulaTurmaRepository.save(aulaTurma);
    }


    public AulaTurma atualizarVinculoAulaTurma(Long id, Long idAula, Long idTurma) {
        AulaTurma aulaTurma = aulaTurmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AulaTurma not found"));

        Aula aula = aulaRepository.findById(idAula)
                .orElseThrow(() -> new EntityNotFoundException("Aula not found"));

        Turma turma = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new EntityNotFoundException("Turma not found"));

        aulaTurma.setAula(aula);
        aulaTurma.setTurma(turma);

        return aulaTurmaRepository.save(aulaTurma);
    }

    public AulaTurmaResponse findAulaTurmaById(Long id) {
        Optional<AulaTurma> aulaTurmaOptional = aulaTurmaRepository.findById(id);
        if (aulaTurmaOptional.isPresent()) {
            AulaTurma aulaTurma = aulaTurmaOptional.get();
            return mapToResponse(aulaTurma);
        } else {
            throw new ResourceNotFoundException("AulaTurma not found with id: " + id);
        }
    }

    private AulaTurmaResponse mapToResponse(AulaTurma aulaTurma) {
        AulaTurmaResponse response = new AulaTurmaResponse();
        response.setId(aulaTurma.getId());
        response.setIdAula(aulaTurma.getAula().getId());
        response.setIdTurma(aulaTurma.getTurma().getId());
        response.setLicao(aulaTurma.getAula().getLicao());
        response.setNomeTurma(aulaTurma.getTurma().getNome());
        return response;
    }

    public List<AulaTurmaDTO> listarAulaETurmas() {
        List<Aula> aulas = aulaRepository.findAll();

        // Mapear para DTOs que contenham id, nome da Turma e nome da Ebd
        return aulas.stream()
                .flatMap(aula -> aula.getAulaTurmas().stream()
                        .map(aulaTurma -> new AulaTurmaDTO(
                                aulaTurma.getId(),
                                aula.getLicao(),
                                aulaTurma.getTurma().getNome())))
                .collect(Collectors.toList());
    }

    //Aula Igreja

    public List<Aula> listarAulasPorIgreja(Long idIgreja) {
        return aulaRepository.findAulasByIgrejaId(idIgreja);
    }


    public List<AulaTurmaDTO> listarAulaETurmasPorIgrejaId(Long igrejaId) {
        List<Aula> aulas = aulaRepository.findAll();
        return aulas.stream()
                .flatMap(aula -> aula.getAulaTurmas().stream()
                        .filter(aulaTurma -> {
                            Turma turma = aulaTurma.getTurma();
                            return turma.getEbdTurmas().stream()
                                    .anyMatch(ebdTurma -> ebdTurma.getEbd().getIgrejaEbds().stream()
                                            .anyMatch(igrejaEbd -> igrejaEbd.getIgreja().getId().equals(igrejaId)));
                        })
                        .map(aulaTurma -> new AulaTurmaDTO(
                                aulaTurma.getId(),
                                aula.getLicao(),
                                aulaTurma.getTurma().getNome())))
                .collect(Collectors.toList());
    }


    public List<Turma> obterTurmasPorIgrejaId(Long igrejaId) {
        List<Ebd> ebds = igrejaEbdRepository.findEbdsByIgrejaId(igrejaId);
        List<Turma> turmas = ebdTurmaRepository.findTurmasByEbds(ebds);
        return turmas;
    }






    //Métodos sem uso
    /*
    public List<OfertaResponse> somaOfertasPorTurmaEMesETrimestre(Long turmaId, Integer mes, Integer trimestre, Integer ano) {
        List<OfertaResponse> ofertas = new ArrayList<>();
        BigDecimal somaOfertas = BigDecimal.ZERO;

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma not found"));

        for (AulaTurma aulaTurma : turma.getAulaTurmas()) {
            Aula aula = aulaTurma.getAula();
            boolean matches = (mes != null && getMonth(aula.getDia()) == mes) ||
                    (trimestre != null && getTrimestre(aula.getDia()) == trimestre);

            if (matches && getYear(aula.getDia()) == ano) {
                somaOfertas = somaOfertas.add(BigDecimal.valueOf(aula.getOferta()));
            }
        }

        // Adicionar resposta
        OfertaResponse response = new OfertaResponse();
        response.setAno(ano);
        response.setMes(mes);
        response.setTrimestre(trimestre);
        response.setTotalOfertas(somaOfertas);
        ofertas.add(response);

        return ofertas;
    } */
}
