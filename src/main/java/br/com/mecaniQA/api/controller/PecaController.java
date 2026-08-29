package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.model.CategoriaPeca;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.repository.PecaRepository;
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
    public List<Peca> listar() {
        return repository.listar();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Peca> buscarPorCodigo(
            @PathVariable Long codigo) {

        Peca peca = repository.buscarPorCodigo(codigo);

        if (peca == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(peca);
    }

    @PostMapping
    public Peca salvar(@RequestBody PecaRequest request) {

        Peca peca = new Peca(
                request.codigo(),
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

        return peca;
    }

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