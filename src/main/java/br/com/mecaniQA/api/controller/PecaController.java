package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.model.CategoriaPeca;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.repository.PecaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pecas")
public class PecaController {

    private final PecaRepository repository;

    public PecaController() {
        this.repository = PecaRepository.getInstance();
    }

    @GetMapping
    public List<Peca> listar() {
        return repository.listar();
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