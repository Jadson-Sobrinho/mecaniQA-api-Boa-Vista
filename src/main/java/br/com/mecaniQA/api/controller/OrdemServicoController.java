package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.dto.OrdemServicoDTO;
import br.com.mecaniQA.api.mapper.OrdemServicoMapper;
import br.com.mecaniQA.api.model.OrdemServico;
import br.com.mecaniQA.api.repository.OrdemServicoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {

    private final OrdemServicoRepository repository;

    public OrdemServicoController() {
        this.repository = OrdemServicoRepository.getInstance();
    }

    @PostMapping
    public ResponseEntity<?> criar(
            @RequestBody OrdemServicoDTO dto) {

        if (dto.getDescricao() == null ||
                dto.getDescricao().trim().isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("A descrição da ordem de serviço é obrigatória.");
        }

        if (dto.getStatus() == null) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("O status da ordem de serviço é obrigatório.");
        }

        OrdemServico ordemServico =
                OrdemServicoMapper.toEntity(dto);

        OrdemServico salva =
                repository.salvar(ordemServico);

        OrdemServicoDTO resposta =
                OrdemServicoMapper.toDTO(salva);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<OrdemServicoDTO>> listar() {

        List<OrdemServicoDTO> ordens =
                repository.listar()
                        .stream()
                        .map(OrdemServicoMapper::toDTO)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(ordens);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(
            @PathVariable Long codigo) {

        OrdemServico ordem =
                repository.buscarPorCodigo(codigo);

        if (ordem == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Ordem de serviço não encontrada.");
        }

        return ResponseEntity.ok(
                OrdemServicoMapper.toDTO(ordem)
        );
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long codigo,
            @RequestBody OrdemServicoDTO dto) {

        if (dto.getDescricao() == null ||
                dto.getDescricao().trim().isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("A descrição da ordem de serviço é obrigatória.");
        }

        if (dto.getStatus() == null) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("O status da ordem de serviço é obrigatório.");
        }

        OrdemServico existente =
                repository.buscarPorCodigo(codigo);

        if (existente == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Ordem de serviço não encontrada.");
        }

        OrdemServico ordemAtualizada =
                OrdemServicoMapper.toEntity(dto);

        ordemAtualizada.setCodigo(codigo);

        OrdemServico atualizada =
                repository.atualizar(codigo, ordemAtualizada);

        OrdemServicoDTO resposta =
                OrdemServicoMapper.toDTO(atualizada);

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> deletar(
            @PathVariable Long codigo) {

        boolean removido =
                repository.deletar(codigo);

        if (!removido) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Ordem de serviço não encontrada.");
        }

        return ResponseEntity.noContent().build();
    }
}