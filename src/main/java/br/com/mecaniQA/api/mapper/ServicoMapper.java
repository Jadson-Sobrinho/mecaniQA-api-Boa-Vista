package br.com.mecaniQA.api.mapper;

import br.com.mecaniQA.api.dto.ServicoDTO;
import br.com.mecaniQA.api.model.Servico;

public class ServicoMapper {

    public static Servico toEntity(ServicoDTO dto) {

        return new Servico(
                dto.getNome(),
                dto.getTempoEstimado(),
                dto.getCustoTabelado()
        );
    }

    public static ServicoDTO toDTO(Servico servico) {

        return new ServicoDTO(
                servico.getCodigo(),
                servico.getNome(),
                servico.getTempoEstimado(),
                servico.getCustoTabelado()
        );
    }
}