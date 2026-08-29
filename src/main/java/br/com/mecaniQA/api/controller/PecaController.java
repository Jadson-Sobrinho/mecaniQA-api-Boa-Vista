package com.mecaniqa.controller;

import com.mecaniqa.model.Peca;
import com.mecaniqa.repository.PecaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

    private final PecaRepository repository =
            PecaRepository.getInstance();

    // US01 - Cadastrar peça
    @PostMapping
    public ResponseEntity<Peca> cadastrar(@RequestBody Peca peca) {

        peca.setDataCadastro(LocalDateTime.now());
        peca.setDataUltimaAtualizacao(LocalDateTime.now());

        Peca novaPeca = repository.salvar(peca);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(novaPeca);
    }

    // US02 - Listar peças
    @GetMapping
    public ResponseEntity<List<Peca>> listar() {

        return ResponseEntity.ok(
                repository.listarTodos()
        );
    }

    // US02 - Buscar peça por código
    @GetMapping("/{codigo}")
    public ResponseEntity<Peca> buscar(
            @PathVariable Long codigo
    ) {

        return repository.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // US03 - Atualizar peça
    @PutMapping("/{codigo}")
    public ResponseEntity<Peca> atualizar(
            @PathVariable Long codigo,
            @RequestBody Peca dados
    ) {

        return repository.buscarPorCodigo(codigo)
                .map(peca -> {

                    peca.setCodigoBarras(
                            dados.getCodigoBarras()
                    );

                    peca.setFornecedorMarca(
                            dados.getFornecedorMarca()
                    );

                    peca.setQuantidadeEstoque(
                            dados.getQuantidadeEstoque()
                    );

                    peca.setPrecoCusto(
                            dados.getPrecoCusto()
                    );

                    peca.setPrecoVenda(
                            dados.getPrecoVenda()
                    );

                    peca.setTamanho(
                            dados.getTamanho()
                    );

                    peca.setCor(
                            dados.getCor()
                    );

                    peca.setCategoria(
                            dados.getCategoria()
                    );

                    peca.setDataUltimaAtualizacao(
                            LocalDateTime.now()
                    );

                    return ResponseEntity.ok(peca);
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // US04 - Excluir peça
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