package br.com.mecaniQA.api.service;

import java.util.List;
import br.com.mecaniQA.api.dto.ServicoDTO;
import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.mapper.ServicoMapper;
import br.com.mecaniQA.api.repository.ServicoRepository;
import br.com.mecaniQA.api.exception.*;

public class ServicoService {
    private final ServicoRepository repository;
                                                                    
    public ServicoService() { this.repository = ServicoRepository.getInstance(); }
    public List<ServicoDTO> listar() { return repository.findAll().stream().map(ServicoMapper::toDTO).toList(); }
    public ServicoDTO buscar(Long id) { return ServicoMapper.toDTO(require(id)); }
    private Servico require(Long id) {
        Validacao.id(id);
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Servico não encontrado(a)."));
    }
    public ServicoDTO criar(ServicoDTO dto) {
        validar(dto);
        return ServicoMapper.toDTO(repository.save(ServicoMapper.toEntity(dto)));
    }
    public ServicoDTO atualizar(Long id, ServicoDTO dto) {
        require(id); validar(dto);
        Servico e = ServicoMapper.toEntity(dto); e.setCodigo(id);
        return ServicoMapper.toDTO(repository.update(e));
    }
    public void excluir(Long id) { Validacao.id(id); repository.deleteById(id); }
    private void validar(ServicoDTO dto) {
        Validacao.required(dto,"Dados");
        Validacao.text(dto.getNome(),"Nome",150);
        if (dto.getTempoEstimado() == null || dto.getTempoEstimado() <= 0)
            throw new DadosInvalidosException("Tempo estimado deve ser informado em minutos e ser maior que zero.");
        Validacao.money(dto.getCustoTabelado(),"Custo tabelado");
    }
}
