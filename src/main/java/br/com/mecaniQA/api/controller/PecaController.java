package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.model.CategoriaPeca;
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

    // GET /api/pecas
    // 200 OK
    @GetMapping
    public ResponseEntity<List<Peca>> listar() {

        return ResponseEntity.ok(repository.listar());
    }

    // GET /api/pecas/{codigo}
    // 200 OK ou 404 Not Found
    @GetMapping("/{codigo}")
    public ResponseEntity<Peca> buscarPorCodigo(
            @PathVariable Long codigo) {

        Peca peca = repository.buscarPorCodigo(codigo);

        if (peca == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(peca);
    }

    // POST /api/pecas
    // 201 Created
    @PostMapping
    public ResponseEntity<Peca> salvar(
            @RequestBody PecaRequest request) {

        Peca peca = new Peca(
                request.nome(),
                request.codigoBarras(),
                request.fornecedorMarca(),
                request.quantidadeEstoque(),
                request.precoCusto(),
                request.precoVenda(),
                request.categoria()
        );

        peca.setTamanho(request.tamanho());
        peca.setCor(request.cor());

        repository.salvar(peca);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(peca);
    }

    // PUT /api/pecas/{codigo}
    // 200 OK ou 404 Not Found
    @PutMapping("/{codigo}")
    public ResponseEntity<Peca> atualizar(
            @PathVariable Long codigo,
            @RequestBody PecaRequest request) {

        Peca peca = repository.buscarPorCodigo(codigo);

        if (peca == null) {
            return ResponseEntity.notFound().build();
        }

        peca.setNome(request.nome());
        peca.setCodigoBarras(request.codigoBarras());
        peca.setFornecedorMarca(request.fornecedorMarca());
        peca.setQuantidadeEstoque(request.quantidadeEstoque());
        peca.setPrecoCusto(request.precoCusto());
        peca.setPrecoVenda(request.precoVenda());
        peca.setCategoria(request.categoria());
        peca.setTamanho(request.tamanho());
        peca.setCor(request.cor());

        return ResponseEntity.ok(peca);
    }

    // DELETE /api/pecas/{codigo}
    // 204 No Content ou 404 Not Found
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long codigo) {

        boolean removido = repository.deletar(codigo);

        if (!removido) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    public record PecaRequest(
            Long codigo,
            String nome,
            String codigoBarras,
            String fornecedorMarca,
            int quantidadeEstoque,
            double precoCusto,
            double precoVenda,
            CategoriaPeca categoria,
            String tamanho,
            String cor
    ) {}
}