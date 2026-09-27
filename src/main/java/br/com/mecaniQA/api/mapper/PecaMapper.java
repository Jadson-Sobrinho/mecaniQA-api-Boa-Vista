package br.com.mecaniQA.api.mapper;

import br.com.mecaniQA.api.dto.PecaDTO;
import br.com.mecaniQA.api.model.Peca;

public class PecaMapper {

    public static Peca toEntity(PecaDTO dto) {

        Peca peca = new Peca(
                dto.getNome(),
                dto.getCodigoBarras(),
                dto.getFornecedorMarca(),
                dto.getQuantidadeEstoque(),
                dto.getPrecoCusto(),
                dto.getPrecoVenda(),
                dto.getCategoria()
        );

        peca.setTamanho(dto.getTamanho());
        peca.setCor(dto.getCor());

        return peca;
    }

    public static PecaDTO toDTO(Peca peca) {

        return new PecaDTO(
                peca.getCodigo(),
                peca.getNome(),
                peca.getCodigoBarras(),
                peca.getFornecedorMarca(),
                peca.getQuantidadeEstoque(),
                peca.getPrecoCusto(),
                peca.getPrecoVenda(),
                peca.getCategoria(),
                peca.getTamanho(),
                peca.getCor()
        );
    }
}