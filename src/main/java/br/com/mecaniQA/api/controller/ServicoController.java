package br.com.mecaniQA.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.mecaniQA.api.dto.*;
import br.com.mecaniQA.api.service.ServicoService;
import br.com.mecaniQA.api.exception.*;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(ServicoController.class.getName());
    private final ServicoService service;
                                                                                                   
    public ServicoController() { this.service = new ServicoService(); }

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
    public ResponseEntity<?> criar(@RequestBody ServicoDTO dto) {
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
    public ResponseEntity<?> atualizar(@PathVariable Long codigo, @RequestBody ServicoDTO dto) {
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
