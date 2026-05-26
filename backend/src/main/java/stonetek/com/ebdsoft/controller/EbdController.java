package stonetek.com.ebdsoft.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import stonetek.com.ebdsoft.dto.dtosespecificos.EbdTurmaDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.RelatorioDTO;
import stonetek.com.ebdsoft.dto.request.EbdRequest;
import stonetek.com.ebdsoft.dto.request.EbdTurmaRequest;
import stonetek.com.ebdsoft.dto.request.RelatorioRequestDTO;
import stonetek.com.ebdsoft.dto.response.EbdResponse;
import stonetek.com.ebdsoft.dto.response.EbdTurmaResponse;
import stonetek.com.ebdsoft.model.Ebd;
import stonetek.com.ebdsoft.model.EbdTurma;
import stonetek.com.ebdsoft.service.EbdService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/escolabiblica")
public class EbdController {

    private final EbdService ebdService;

    @GetMapping
    public ResponseEntity<List<EbdResponse>> listar() {
        return ResponseEntity.ok().body(ebdService.listar());
    }
    
    @GetMapping("/{idEbd}")
    public ResponseEntity<EbdResponse> buscarPorId(@PathVariable Long idEbd) {
        EbdResponse ebd = ebdService.buscarPorId(idEbd);
        return ResponseEntity.ok().body(ebd);
    }
    
    @PostMapping
    public ResponseEntity<EbdResponse> salvar(@Valid @RequestBody EbdRequest request) {
        EbdResponse ebd = ebdService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ebd);
    }
    
    @PutMapping("/{idEbd}")
    public ResponseEntity<EbdResponse> editar(@PathVariable Long idEbd,
            @Valid @RequestBody EbdRequest request) {
        EbdResponse ebd = ebdService.editar(idEbd, request);
        return ResponseEntity.ok().body(ebd);
    }
    
    @DeleteMapping("/{idEbd}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long idEbd) {
        ebdService.excluir(idEbd);
    }

    //Métodos personalizados

    @GetMapping("/{ebdId}/turmas")
    public List<Long> getTurmaIdsByEbdId(@PathVariable Long ebdId) {
        return ebdService.getTurmaIdsByEbdId(ebdId);
    }

    @GetMapping("/{nome}/turmas")
    public List<Long> getTurmaIdsByEbdNome(@PathVariable String nome) {
        return ebdService.getTurmaIdsByEbdNome(nome);
    }

    @PostMapping("/ebd-turma-vinculo")
    public EbdTurma criarVinculoEbdTurma(@RequestBody EbdTurmaRequest request) {
        return ebdService.criarVinculoEbdTurma(request.getEbdId(), request.getTurmaId());
    }

    @PutMapping("/ebd-turma-vinculo/{id}")
    public ResponseEntity<EbdTurma> atualizarVinculoEbdTurma(@PathVariable Long id, @RequestBody EbdTurmaRequest request) {
        EbdTurma updatedEbdTurma = ebdService.atualizarVinculoEbdTurma(id, request.getEbdId(), request.getTurmaId());
        return ResponseEntity.ok(updatedEbdTurma);
    }

    @GetMapping("/ebd-turma-vinculo/{id}")
    public ResponseEntity<EbdTurmaResponse> getEbdTurmaById(@PathVariable Long id) {
        EbdTurmaResponse ebdTurmaResponse = ebdService.findEbdTurmaById(id);
        return ResponseEntity.ok(ebdTurmaResponse);
    }


    @GetMapping("/ebd-turma")
    public List<EbdTurma> obterTodasAsRelacoesEbdTurma() {
        return ebdService.obterTodasAsRelacoesEbdTurma();
    }
    
    @GetMapping("/ebd-turma/all")
    public List<EbdTurmaDTO> getAllEbdTurmaDTOsWithCompleteData() {
        return ebdService.obterTodasAsRelacoesEbdTurmaComDadosCompletos();
    }

    //métodos com outras classes

    @GetMapping("/aniversariantes/mes")
    public List<Object> getAniversariantesDoMes() {
        return ebdService.getAniversariantesDoMes();
    }

    @GetMapping("/aniversariantes/trimestre")
    public List<Object> getAniversariantesDoTrimestre() {
        return ebdService.getAniversariantesDoTrimestre();
    }

    @GetMapping("/aniversariantes/mes/{igrejaId}")
    public List<Object> getAniversariantesDoMesPorIgreja(@PathVariable Long igrejaId) {
        return ebdService.getAniversariantesDoMesPorIgreja(igrejaId);
    }

    @GetMapping("/aniversariantes/trimestre/{igrejaId}")
    public List<Object> getAniversariantesDoTrimestrePorIgreja(@PathVariable Long igrejaId) {
        return ebdService.getAniversariantesDoTrimestrePorIgreja(igrejaId);
    }


    @PostMapping("/vincular-igreja")
    public void vincularIgrejaAEbd(@RequestBody VincularRequest request) {
        ebdService.vincularIgrejaAEbd(request.getEbdNome(), request.getIgrejaNome());
    }

    @GetMapping("/igreja/{igrejaId}")
    public ResponseEntity<Ebd> obterEbdPorIgrejaId(@PathVariable Long igrejaId) {
        Ebd ebd = ebdService.obterEbdPorIgrejaId(igrejaId);
        return ResponseEntity.ok(ebd);
    }

    public static class VincularRequest {
        private String ebdNome;
        private String igrejaNome;

        public String getEbdNome() {
            return ebdNome;
        }

        public void setEbdNome(String ebdNome) {
            this.ebdNome = ebdNome;
        }

        public String getIgrejaNome() {
            return igrejaNome;
        }

        public void setIgrejaNome(String igrejaNome) {
            this.igrejaNome = igrejaNome;
        }
    }

    //relatorio de secretaria

    @PostMapping("/data")
    public ResponseEntity<RelatorioDTO> gerarRelatorio(@RequestBody RelatorioRequestDTO request) {
        RelatorioDTO relatorio;

        if (request.getData() != null) {
            // Se a data for fornecida
            relatorio = ebdService.gerarRelatorio(request.getIdEbd(), request.getData(), null, null, request.getAno());
        } else if (request.getMes() != null) {
            // Se o mês for fornecido
            relatorio = ebdService.gerarRelatorio(request.getIdEbd(), null, request.getMes(), null, request.getAno());
        } else if (request.getTrimestre() != null) {
            // Se o trimestre for fornecido
            relatorio = ebdService.gerarRelatorio(request.getIdEbd(), null, null, request.getTrimestre(), request.getAno());
        } else {
            throw new IllegalArgumentException("Data, mês ou trimestre devem ser fornecidos");
        }

        return ResponseEntity.ok(relatorio);
    }


}
