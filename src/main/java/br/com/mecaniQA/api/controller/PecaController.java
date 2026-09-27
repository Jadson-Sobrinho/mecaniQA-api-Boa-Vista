package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.dto.PecaDTO;
import br.com.mecaniQA.api.mapper.PecaMapper;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.repository.PecaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

    private final PecaRepository repository;

    public PecaController() {
        this.repository = PecaRepository.getInstance();
    }

    @GetMapping
    public ResponseEntity<List<PecaDTO>> listar() {

        List<PecaDTO> pecas = repository.listar().stream().map(PecaMapper::toDTO).toList();

        return ResponseEntity.ok(pecas);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable Long codigo) {

        Peca peca = repository.buscarPorCodigo(codigo);

        if (peca == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Peça não encontrada.");
        }

        return ResponseEntity.ok(PecaMapper.toDTO(peca));
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody PecaDTO dto) {

        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O nome da peça é obrigatório.");
        }

        if (dto.getCategoria() == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A categoria da peça é obrigatória.");
        }

        Peca peca = PecaMapper.toEntity(dto);

        repository.salvar(peca);

        return ResponseEntity.status(HttpStatus.CREATED).body(PecaMapper.toDTO(peca));
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> atualizar(@PathVariable Long codigo, @RequestBody PecaDTO dto) {

        Peca existente = repository.buscarPorCodigo(codigo);

        if (existente == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Peça não encontrada.");
        }

        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O nome da peça é obrigatório.");
        }

        if (dto.getCategoria() == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A categoria da peça é obrigatória.");
        }

        Peca pecaAtualizada = PecaMapper.toEntity(dto);

        boolean atualizada = repository.atualizar(codigo, pecaAtualizada);

        if (!atualizada) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Peça não encontrada.");
        }

        Peca peca = repository.buscarPorCodigo(codigo);

        return ResponseEntity.ok(PecaMapper.toDTO(peca));
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> deletar(@PathVariable Long codigo) {

        boolean removido = repository.deletar(codigo);

        if (!removido) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Peça não encontrada.");
        }

        return ResponseEntity.noContent().build();
    }
}