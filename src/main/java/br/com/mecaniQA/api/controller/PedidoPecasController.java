package br.com.mecaniQA.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.mecaniQA.api.dto.*;
import br.com.mecaniQA.api.service.PedidoPecasService;
import br.com.mecaniQA.api.exception.*;

@RestController
@RequestMapping("/api/pedidos-pecas")
public class PedidoPecasController {
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(PedidoPecasController.class.getName());
    private final PedidoPecasService service;
                                                                                                   
    public PedidoPecasController() { this.service = new PedidoPecasService(); }

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
    public ResponseEntity<?> criar(@RequestBody PedidoPecasDTO dto) {
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

    @PostMapping("/{codigo}/itens")
    public ResponseEntity<?> adicionarItem(@PathVariable Long codigo, @RequestBody ItemPedidoPecaDTO dto) {
        try { return ResponseEntity.status(201).body(service.adicionarItem(codigo,dto)); } catch (DadosInvalidosException e) {
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

    @PatchMapping("/{codigo}/status")
    public ResponseEntity<?> alterarStatus(@PathVariable Long codigo, @RequestBody PedidoPecasDTO dto) {
        try { return ResponseEntity.ok(service.alterarStatus(codigo,dto)); } catch (DadosInvalidosException e) {
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
