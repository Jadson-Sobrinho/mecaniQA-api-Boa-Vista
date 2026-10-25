package br.com.mecaniQA.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.mecaniQA.api.dto.*;
import br.com.mecaniQA.api.service.OrdemServicoService;
import br.com.mecaniQA.api.exception.*;

@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(OrdemServicoController.class.getName());
    private final OrdemServicoService service;
                                                                                                   
    public OrdemServicoController() { this.service = new OrdemServicoService(); }

    @GetMapping
    public ResponseEntity<?> listar() {
        try { return ResponseEntity.ok(service.listar()); } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().body(new ErroDTO(e.getMessage()));
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.status(404).body(new ErroDTO(e.getMessage()));
        } catch (ConflitoDadosException e) {
            LOG.log(java.util.logging.Level.WARNING,"Conflito de persistência",e);
            return ResponseEntity.status(409).body(new ErroDTO(e.getMessage()));
        } catch (PersistenciaException e) {
            LOG.log(java.util.logging.Level.SEVERE,"Falha de persistência",e);
            return ResponseEntity.status(500).body(new ErroDTO("Não foi possível concluir a operação."));
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscar(@PathVariable Long codigo) {
        try { return ResponseEntity.ok(service.buscar(codigo)); } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().body(new ErroDTO(e.getMessage()));
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.status(404).body(new ErroDTO(e.getMessage()));
        } catch (ConflitoDadosException e) {
            LOG.log(java.util.logging.Level.WARNING,"Conflito de persistência",e);
            return ResponseEntity.status(409).body(new ErroDTO(e.getMessage()));
        } catch (PersistenciaException e) {
            LOG.log(java.util.logging.Level.SEVERE,"Falha de persistência",e);
            return ResponseEntity.status(500).body(new ErroDTO("Não foi possível concluir a operação."));
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody OrdemServicoDTO dto) {
        try { return ResponseEntity.status(201).body(service.criar(dto)); } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().body(new ErroDTO(e.getMessage()));
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.status(404).body(new ErroDTO(e.getMessage()));
        } catch (ConflitoDadosException e) {
            LOG.log(java.util.logging.Level.WARNING,"Conflito de persistência",e);
            return ResponseEntity.status(409).body(new ErroDTO(e.getMessage()));
        } catch (PersistenciaException e) {
            LOG.log(java.util.logging.Level.SEVERE,"Falha de persistência",e);
            return ResponseEntity.status(500).body(new ErroDTO("Não foi possível concluir a operação."));
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> atualizar(@PathVariable Long codigo, @RequestBody OrdemServicoDTO dto) {
        try { return ResponseEntity.ok(service.atualizar(codigo,dto)); } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().body(new ErroDTO(e.getMessage()));
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.status(404).body(new ErroDTO(e.getMessage()));
        } catch (ConflitoDadosException e) {
            LOG.log(java.util.logging.Level.WARNING,"Conflito de persistência",e);
            return ResponseEntity.status(409).body(new ErroDTO(e.getMessage()));
        } catch (PersistenciaException e) {
            LOG.log(java.util.logging.Level.SEVERE,"Falha de persistência",e);
            return ResponseEntity.status(500).body(new ErroDTO("Não foi possível concluir a operação."));
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> excluir(@PathVariable Long codigo) {
        try { service.excluir(codigo); return ResponseEntity.noContent().build(); } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().body(new ErroDTO(e.getMessage()));
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.status(404).body(new ErroDTO(e.getMessage()));
        } catch (ConflitoDadosException e) {
            LOG.log(java.util.logging.Level.WARNING,"Conflito de persistência",e);
            return ResponseEntity.status(409).body(new ErroDTO(e.getMessage()));
        } catch (PersistenciaException e) {
            LOG.log(java.util.logging.Level.SEVERE,"Falha de persistência",e);
            return ResponseEntity.status(500).body(new ErroDTO("Não foi possível concluir a operação."));
        }
    }
}
