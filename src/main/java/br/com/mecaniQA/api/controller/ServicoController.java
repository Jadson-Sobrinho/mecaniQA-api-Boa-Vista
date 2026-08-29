package br.com.mecaniQA.api.controller;

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

    // GET /api/servicos
    // 200 OK
    @GetMapping
    public ResponseEntity<List<Servico>> listar() {

        return ResponseEntity.ok(repository.listar());
    }

    // GET /api/servicos/{codigo}
    // 200 OK ou 404 Not Found
    @GetMapping("/{codigo}")
    public ResponseEntity<Servico> buscarPorCodigo(
            @PathVariable Long codigo) {

        Servico servico = repository.buscarPorCodigo(codigo);

        if (servico == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(servico);
    }

    // POST /api/servicos
    // 201 Created
    @PostMapping
    public ResponseEntity<Servico> salvar(
            @RequestBody ServicoRequest request) {

        Servico servico = new Servico(
                request.nome(),
                request.tempoEstimado(),
                request.custoTabelado()
        );

        repository.salvar(servico);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servico);
    }

    // PUT /api/servicos/{codigo}
    // 200 OK ou 404 Not Found
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

    // DELETE /api/servicos/{codigo}
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

    public record ServicoRequest(
            String nome,
            String tempoEstimado,
            double custoTabelado
    ) {}
}