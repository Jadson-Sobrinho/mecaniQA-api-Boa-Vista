package br.com.mecaniQA.api.repository;

import br.com.mecaniQA.api.model.Peca;

import java.util.ArrayList;
import java.util.List;

public class PecaRepository {

    private static PecaRepository instance;

    private final List<Peca> pecas;

    private PecaRepository() {
        pecas = new ArrayList<>();
    }

    public static PecaRepository getInstance() {

        if (instance == null) {
            instance = new PecaRepository();
        }

        return instance;
    }

    public List<Peca> listar() {
        return pecas;
    }

    public void salvar(Peca peca) {
        pecas.add(peca);
    }

    public Peca buscarPorCodigo(Long codigo) {
        for (Peca peca : pecas) {
            if (peca.getCodigo().equals(codigo)) {
                return peca;
            }
        }

        return null;
    }

    public boolean atualizar(Long codigo, Peca pecaAtualizada) {
        Peca peca = buscarPorCodigo(codigo);

        if (peca == null) {
            return false;
        }

        peca.setNome(pecaAtualizada.getNome());
        peca.setCodigoBarras(pecaAtualizada.getCodigoBarras());
        peca.setFornecedorMarca(pecaAtualizada.getFornecedorMarca());
        peca.setQuantidadeEstoque(pecaAtualizada.getQuantidadeEstoque());
        peca.setPrecoCusto(pecaAtualizada.getPrecoCusto());
        peca.setPrecoVenda(pecaAtualizada.getPrecoVenda());
        peca.setCategoria(pecaAtualizada.getCategoria());
        peca.setTamanho(pecaAtualizada.getTamanho());
        peca.setCor(pecaAtualizada.getCor());

        return true;
    }

    public boolean deletar(Long codigo) {
        return pecas.removeIf(peca ->
                peca.getCodigo().equals(codigo)
        );
    }
}

