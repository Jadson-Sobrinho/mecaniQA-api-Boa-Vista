package br.com.mecaniQA.api.mapper;

import br.com.mecaniQA.api.dto.ItemPedidoPecaDTO;
import br.com.mecaniQA.api.dto.PedidoPecasDTO;
import br.com.mecaniQA.api.model.ItemPedidoPeca;

import br.com.mecaniQA.api.model.PedidoPecas;


import java.util.ArrayList;
import java.util.List;

public class PedidoPecasMapper {

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