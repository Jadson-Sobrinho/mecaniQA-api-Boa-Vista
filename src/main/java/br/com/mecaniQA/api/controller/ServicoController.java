package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.repository.ServicoRepository;
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
    public List<Servico> listar() {
        return repository.listar();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Servico> buscarPorCodigo(
            @PathVariable Long codigo) {

        Servico servico = repository.buscarPorCodigo(codigo);

        if (servico == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(servico);
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

    @PutMapping("/{codigo}")
    public ResponseEntity<Servico> atualizar(
            @PathVariable Long codigo,
            @RequestBody ServicoRequest request) {

        Servico servico = repository.buscarPorCodigo(codigo);

        if (servico == null) {
            return ResponseEntity.notFound().build();
        }

        servico.setNome(request.nome());
        servico.setTempoEstimado(request.tempoEstimado());
        servico.setCustoTabelado(request.custoTabelado());

        return ResponseEntity.ok(servico);
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

    public record ServicoRequest(
            String nome,
            String tempoEstimado,
            double custoTabelado
    ) {}
}