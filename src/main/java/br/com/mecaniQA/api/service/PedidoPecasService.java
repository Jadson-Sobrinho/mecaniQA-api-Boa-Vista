package br.com.mecaniQA.api.service;

import java.util.List;
import br.com.mecaniQA.api.config.ConnectionFactory;
import br.com.mecaniQA.api.dto.*;
import br.com.mecaniQA.api.model.*;
import br.com.mecaniQA.api.mapper.PedidoPecasMapper;
import br.com.mecaniQA.api.repository.*;
import br.com.mecaniQA.api.exception.*;

public class PedidoPecasService {
    private final PedidoPecasRepository repository;
    private final PecaRepository pecas;
    private final ConnectionFactory db;
                                                                    
    public PedidoPecasService() {
        this.repository = PedidoPecasRepository.getInstance();
        this.pecas = PecaRepository.getInstance();
        this.db = ConnectionFactory.getInstance();
    }
    public List<PedidoPecasDTO> listar() { return repository.findAll().stream().map(PedidoPecasMapper::toDTO).toList(); }
    public PedidoPecasDTO buscar(Long id) {
        Validacao.id(id);
        return PedidoPecasMapper.toDTO(repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado.")));
    }
                                                                                               
    public PedidoPecasDTO criar(PedidoPecasDTO dto) {
        Validacao.required(dto,"Dados");
        return PedidoPecasMapper.toDTO(repository.save(new PedidoPecas.Builder().build()));
    }
    public PedidoPecasDTO adicionarItem(Long id, ItemPedidoPecaDTO dto) {
        Validacao.id(id); Validacao.required(dto,"Item"); Validacao.id(dto.getPecaCodigo());
        if (dto.getQuantidade() == null || dto.getQuantidade() <= 0) throw new DadosInvalidosException("Quantidade deve ser maior que zero.");
        return db.transaction(() -> {
            PedidoPecas pedido = repository.findByIdForUpdate(id).orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado."));
            if (pedido.getStatus() != StatusPedidoPecas.EM_ORCAMENTO) throw new DadosInvalidosException("Só é possível adicionar peças a pedidos EM_ORCAMENTO.");
            Peca peca = pecas.findById(dto.getPecaCodigo()).orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada."));
            ItemPedidoPeca item = new ItemPedidoPeca.Builder().pedido(pedido).peca(peca).quantidade(dto.getQuantidade()).build();
            repository.insertItem(id,item);
            return PedidoPecasMapper.toDTO(repository.findById(id).orElseThrow());
        });
    }
    public PedidoPecasDTO alterarStatus(Long id, PedidoPecasDTO dto) {
        Validacao.id(id); Validacao.required(dto,"Dados"); Validacao.required(dto.getStatus(),"Status");
        return db.transaction(() -> {
            repository.findByIdForUpdate(id).orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado."));
            repository.updateStatus(id,dto.getStatus());
            return PedidoPecasMapper.toDTO(repository.findById(id).orElseThrow());
        });
    }
}
