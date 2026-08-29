package com.mecaniqa.controller;

import com.mecaniqa.model.Servico;
import com.mecaniqa.repository.ServicoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoRepository repository =
            ServicoRepository.getInstance();

    // US05 - Cadastrar serviço
    @PostMapping
    public ResponseEntity<Servico> cadastrar(
            @RequestBody Servico servico
    ) {

        servico.setDataCriacao(LocalDateTime.now());
        servico.setDataUltimaAtualizacao(LocalDateTime.now());

        Servico novoServico = repository.salvar(servico);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(novoServico);
    }

    // US06 - Listar serviços
    @GetMapping
    public ResponseEntity<List<Servico>> listar() {

        return ResponseEntity.ok(
                repository.listarTodos()
        );
    }

    // Buscar serviço por código
    @GetMapping("/{codigo}")
    public ResponseEntity<Servico> buscar(
            @PathVariable Long codigo
    ) {

        return repository.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // US07 - Atualizar serviço
    @PutMapping("/{codigo}")
    public ResponseEntity<Servico> atualizar(
            @PathVariable Long codigo,
            @RequestBody Servico dados
    ) {

        return repository.buscarPorCodigo(codigo)
                .map(servico -> {

                    servico.setNome(
                            dados.getNome()
                    );

                    servico.setTempoEstimadoMinutos(
                            dados.getTempoEstimadoMinutos()
                    );

                    servico.setCustoTabelado(
                            dados.getCustoTabelado()
                    );

                    servico.setDataUltimaAtualizacao(
                            LocalDateTime.now()
                    );

                    return ResponseEntity.ok(servico);
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // US08 - Excluir serviço
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long codigo
    ) {

        boolean removido = repository.deletar(codigo);

        if (!removido) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}