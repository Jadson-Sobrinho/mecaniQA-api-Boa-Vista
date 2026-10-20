package br.com.mecaniQA.api.service;

import java.util.List;
import br.com.mecaniQA.api.config.ConnectionFactory;
import br.com.mecaniQA.api.dto.OrdemServicoDTO;
import br.com.mecaniQA.api.model.OrdemServico;
import br.com.mecaniQA.api.mapper.OrdemServicoMapper;
import br.com.mecaniQA.api.repository.OrdemServicoRepository;
import br.com.mecaniQA.api.exception.*;

public class OrdemServicoService {
    private final OrdemServicoRepository repository;
    private final ConnectionFactory db;
                                                                    
    public OrdemServicoService() {
        this.repository = OrdemServicoRepository.getInstance();
        this.db = ConnectionFactory.getInstance();
    }
    public List<OrdemServicoDTO> listar() { return repository.findAll().stream().map(OrdemServicoMapper::toDTO).toList(); }
    public OrdemServicoDTO buscar(Long id) {
        Validacao.id(id);
        return OrdemServicoMapper.toDTO(repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Ordem não encontrada.")));
    }
    public OrdemServicoDTO criar(OrdemServicoDTO dto) {
        validar(dto);
        OrdemServico ordem = OrdemServicoMapper.toEntity(dto); ordem.setCodigo(null);
        return OrdemServicoMapper.toDTO(repository.save(ordem));
    }
    public OrdemServicoDTO atualizar(Long id, OrdemServicoDTO dto) {
        Validacao.id(id); validar(dto);
        return db.transaction(() -> {
            OrdemServico existente = repository.findByIdForUpdate(id).orElseThrow(() -> new RecursoNaoEncontradoException("Ordem não encontrada."));
            existente.setDescricao(dto.getDescricao()); existente.setStatus(dto.getStatus());
            return OrdemServicoMapper.toDTO(repository.update(existente));
        });
    }
    public void excluir(Long id) { Validacao.id(id); repository.deleteById(id); }
    private void validar(OrdemServicoDTO dto) {
        Validacao.required(dto,"Dados"); Validacao.text(dto.getDescricao(),"Descrição",2000); Validacao.required(dto.getStatus(),"Status");
    }
}
