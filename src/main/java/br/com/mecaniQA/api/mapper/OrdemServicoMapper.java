package br.com.mecaniQA.api.mapper;

import br.com.mecaniQA.api.dto.OrdemServicoDTO;
import br.com.mecaniQA.api.model.OrdemServico;

public class OrdemServicoMapper {

    public static OrdemServico toEntity(OrdemServicoDTO dto) {

        return new OrdemServico.Builder()
                .codigo(dto.getCodigo())
                .descricao(dto.getDescricao())
                .status(dto.getStatus())
                .build();
    }

    public static OrdemServicoDTO toDTO(OrdemServico ordemServico) {

        return new OrdemServicoDTO(
                ordemServico.getCodigo(),
                ordemServico.getDescricao(),
                ordemServico.getStatus()
        );
    }
}