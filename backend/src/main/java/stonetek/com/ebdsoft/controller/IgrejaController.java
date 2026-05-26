package stonetek.com.ebdsoft.controller;

import stonetek.com.ebdsoft.dto.dtosespecificos.IgrejaEbdDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.IgrejaEbdTurmasDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.IgrejasEbdsDTO;
import stonetek.com.ebdsoft.dto.dtosespecificos.TurmaEbdDTO;
import stonetek.com.ebdsoft.dto.request.EbdTurmaRequest;
import stonetek.com.ebdsoft.dto.request.IgrejaEbdRequest;
import stonetek.com.ebdsoft.dto.request.IgrejaRequest;
import stonetek.com.ebdsoft.dto.response.EbdTurmaResponse;
import stonetek.com.ebdsoft.dto.response.IgrejaEbdResponse;
import stonetek.com.ebdsoft.dto.response.IgrejaPublicaResponse;
import stonetek.com.ebdsoft.dto.response.IgrejaResponse;
import stonetek.com.ebdsoft.model.EbdTurma;
import stonetek.com.ebdsoft.model.IgrejaEbd;
import stonetek.com.ebdsoft.service.IgrejaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/igrejas")
public class IgrejaController {
    
    private final IgrejaService igrejaService;


@GetMapping
public ResponseEntity<List<IgrejaResponse>> listar() {
    return ResponseEntity.ok().body(igrejaService.listar());
}

@GetMapping("/publica")
public ResponseEntity<List<IgrejaPublicaResponse>> listarPublica() {
        return ResponseEntity.ok().body(igrejaService.listarPublica());}

@GetMapping("/{idIgreja}")
public ResponseEntity<IgrejaResponse> buscarPorId(@PathVariable Long idIgreja) {
    IgrejaResponse igreja = igrejaService.buscarPorId(idIgreja);
    return ResponseEntity.ok().body(igreja);
}

@PostMapping
public ResponseEntity<IgrejaResponse> salvar(@Valid @RequestBody IgrejaRequest request) {
    IgrejaResponse igreja = igrejaService.salvar(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(igreja);
}

@PutMapping("/{idIgreja}")
public ResponseEntity<IgrejaResponse> editar(@PathVariable Long idIgreja,
        @Valid @RequestBody IgrejaRequest request) {
    IgrejaResponse igreja = igrejaService.editar(idIgreja, request);
    return ResponseEntity.ok().body(igreja);
}

@DeleteMapping("/{idIgreja}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void excluir(@PathVariable Long idIgreja) {
    igrejaService.excluir(idIgreja);
}

// personalizadas

    @PostMapping("/igreja-ebd-vinculo")
    public IgrejaEbd criarVinculoIgrejaEbd(@RequestBody IgrejaEbdRequest request) {
        return igrejaService.criarVinculoIgrejaEbd(request.getIdIgreja(), request.getIdEbd());
    }

    @PutMapping("/igreja-ebd-vinculo/{id}")
    public ResponseEntity<IgrejaEbd> atualizarVinculoIgrejaEbd(@PathVariable Long id, @RequestBody IgrejaEbdRequest request) {
        IgrejaEbd updatedIgrejaEbd = igrejaService.atualizarVinculoIgrejaEbd(id, request.getIdIgreja(), request.getIdEbd());
        return ResponseEntity.ok(updatedIgrejaEbd);
    }

    @GetMapping("/igreja-ebd-vinculo/{id}")
    public ResponseEntity<IgrejaEbdResponse> getEbdTurmaById(@PathVariable Long id) {
        IgrejaEbdResponse igrejaEbdResponse = igrejaService.findIgrejaEbdById(id);
        return ResponseEntity.ok(igrejaEbdResponse);
    }

    @GetMapping("/igreja-ebds")
    public ResponseEntity<List<IgrejaEbdDTO>> listarIgrejasEEbds() {
        List<IgrejaEbdDTO> igrejaEbd = igrejaService.listarIgrejasEEbds();
        return ResponseEntity.ok(igrejaEbd);
    }

    @GetMapping("/{igrejaId}/ebds")
    public ResponseEntity<List<IgrejasEbdsDTO>> igrejasEEbds(@PathVariable Long igrejaId) {
        List<IgrejasEbdsDTO> igrejaEbdDTOs = igrejaService.igrejasEEbds(igrejaId);
        return ResponseEntity.ok(igrejaEbdDTOs);
    }


    //igreja com ebd e turma

    @GetMapping("/{igrejaId}/ebd-turmas")
    public ResponseEntity<IgrejaEbdTurmasDTO> getEbdAndTurmasByIgreja(@PathVariable Long igrejaId) {
        IgrejaEbdTurmasDTO dto = igrejaService.getEbdAndTurmasByIgreja(igrejaId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{igrejaId}/ebd-turmas-new")
    public ResponseEntity<List<IgrejaEbdTurmasDTO>> getEbdsAndTurmasByIgrejas(@PathVariable Long igrejaId) {
        List<IgrejaEbdTurmasDTO> dtos = igrejaService.getEbdAndTurmasByIgrejas(igrejaId);
        return ResponseEntity.ok(dtos);
    }


}
