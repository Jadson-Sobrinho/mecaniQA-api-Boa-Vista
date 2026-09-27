package br.com.mecaniQA.api.mapper;

import br.com.mecaniQA.api.dto.ItemPedidoPecaDTO;
import br.com.mecaniQA.api.dto.PedidoPecasDTO;
import br.com.mecaniQA.api.model.ItemPedidoPeca;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.model.PedidoPecas;
import br.com.mecaniQA.api.repository.PecaRepository;

import java.util.ArrayList;
import java.util.List;

public class PedidoPecasMapper {

    public static PedidoPecas toEntity(
            PedidoPecasDTO dto) {

        PedidoPecas pedido =
                new PedidoPecas.Builder()
                        .codigo(dto.getCodigo())
                        .status(dto.getStatus())
                        .build();

        if (dto.getItens() != null) {

            for (ItemPedidoPecaDTO itemDTO :
                    dto.getItens()) {

                Peca peca =
                        PecaRepository
                                .getInstance()
                                .buscarPorCodigo(
                                        itemDTO.getPecaCodigo()
                                );

                if (peca == null) {
                    continue;
                }

                ItemPedidoPeca item =
                        new ItemPedidoPeca.Builder()
                                .codigo(itemDTO.getCodigo())
                                .peca(peca)
                                .quantidade(
                                        itemDTO.getQuantidade()
                                )
                                .build();

                pedido.adicionarItem(item);
            }
        }

        return pedido;
    }

    public static PedidoPecasDTO toDTO(
            PedidoPecas pedido) {

        List<ItemPedidoPecaDTO> itens =
                new ArrayList<>();

        for (ItemPedidoPeca item :
                pedido.getItens()) {

            itens.add(
                    new ItemPedidoPecaDTO(
                            item.getCodigo(),
                            item.getPeca().getCodigo(),
                            item.getQuantidade()
                    )
            );
        }

        return new PedidoPecasDTO(
                pedido.getCodigo(),
                pedido.getStatus(),
                itens
        );
    }
}