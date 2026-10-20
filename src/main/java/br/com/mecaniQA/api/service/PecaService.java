package br.com.mecaniQA.api.service;

import java.util.List;
import br.com.mecaniQA.api.dto.PecaDTO;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.mapper.PecaMapper;
import br.com.mecaniQA.api.repository.PecaRepository;
import br.com.mecaniQA.api.exception.*;

public class PecaService {
    private final PecaRepository repository;
                                                                    
    public PecaService() { this.repository = PecaRepository.getInstance(); }
    public List<PecaDTO> listar() { return repository.findAll().stream().map(PecaMapper::toDTO).toList(); }
    public PecaDTO buscar(Long id) { return PecaMapper.toDTO(require(id)); }
    private Peca require(Long id) {
        Validacao.id(id);
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Peca não encontrado(a)."));
    }
    public PecaDTO criar(PecaDTO dto) {
        validar(dto);
        return PecaMapper.toDTO(repository.save(PecaMapper.toEntity(dto)));
    }
    public PecaDTO atualizar(Long id, PecaDTO dto) {
        require(id); validar(dto);
        Peca e = PecaMapper.toEntity(dto); e.setCodigo(id);
        return PecaMapper.toDTO(repository.update(e));
    }
    public void excluir(Long id) { Validacao.id(id); repository.deleteById(id); }
    private void validar(PecaDTO dto) {
        Validacao.required(dto,"Dados");
        Validacao.text(dto.getNome(),"Nome",150);
        Validacao.text(dto.getCodigoBarras(),"Código de barras",100);
        Validacao.text(dto.getFornecedorMarca(),"Fornecedor/marca",150);
        Validacao.required(dto.getCategoria(),"Categoria");
        if (dto.getQuantidadeEstoque() == null || dto.getQuantidadeEstoque() < 0)
            throw new DadosInvalidosException("Quantidade em estoque deve ser informada e não negativa.");
        Validacao.money(dto.getPrecoCusto(),"Preço de custo");
        Validacao.money(dto.getPrecoVenda(),"Preço de venda");
        Validacao.optional(dto.getTamanho(),"Tamanho",50);
        Validacao.optional(dto.getCor(),"Cor",50);
    }
}
