package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.dto.ServicoDTO;
import br.com.mecaniQA.api.mapper.ServicoMapper;
import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.repository.ServicoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoRepository repository;

    public ServicoController() {
        this.repository = ServicoRepository.getInstance();
    }

    @GetMapping
    public ResponseEntity<List<ServicoDTO>> listar() {

        List<ServicoDTO> servicos = repository.listar().stream().map(ServicoMapper::toDTO).toList();

        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable Long codigo) {

        Servico servico = repository.buscarPorCodigo(codigo);

        if (servico == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço não encontrado.");
        }

        return ResponseEntity.ok(ServicoMapper.toDTO(servico));
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody ServicoDTO dto) {

        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O nome do serviço é obrigatório.");
        }

        if (dto.getTempoEstimado() == null || dto.getTempoEstimado().trim().isEmpty()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O tempo estimado é obrigatório.");
        }

        Servico servico = ServicoMapper.toEntity(dto);

        repository.salvar(servico);

        return ResponseEntity.status(HttpStatus.CREATED).body(ServicoMapper.toDTO(servico));
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> atualizar(@PathVariable Long codigo, @RequestBody ServicoDTO dto) {

        Servico existente = repository.buscarPorCodigo(codigo);

        if (existente == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço não encontrado.");
        }

        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O nome do serviço é obrigatório.");
        }

        if (dto.getTempoEstimado() == null || dto.getTempoEstimado().trim().isEmpty()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O tempo estimado é obrigatório.");
        }

        Servico servicoAtualizado = ServicoMapper.toEntity(dto);

        boolean atualizado = repository.atualizar(codigo, servicoAtualizado);

        if (!atualizado) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço não encontrado.");
        }

        Servico servico = repository.buscarPorCodigo(codigo);

        return ResponseEntity.ok(ServicoMapper.toDTO(servico));
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> deletar(@PathVariable Long codigo) {

        boolean removido = repository.deletar(codigo);

        if (!removido) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço não encontrado.");
        }

        return ResponseEntity.noContent().build();
    }
}