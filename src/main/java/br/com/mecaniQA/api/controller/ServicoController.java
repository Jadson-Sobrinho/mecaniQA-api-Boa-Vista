package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.repository.ServicoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoRepository repository;

    public ServicoController() {
        this.repository = ServicoRepository.getInstance();
    }

    @GetMapping
    public List<Servico> listar() {
        return repository.listar();
    }

    @PostMapping
    public Servico salvar(@RequestBody ServicoRequest request) {

        Servico servico = new Servico(
                request.nome(),
                request.tempoEstimado(),
                request.custoTabelado()
        );

        repository.salvar(servico);

        return servico;
    }

    public record ServicoRequest(
            String nome,
            String tempoEstimado,
            double custoTabelado
    ) {}
}