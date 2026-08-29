package br.com.mecaniQA.api.repository;

import br.com.mecaniQA.api.model.Servico;

import java.util.ArrayList;
import java.util.List;

public class ServicoRepository {

    private static ServicoRepository instance;

    private final List<Servico> servicos;

    private ServicoRepository() {
        servicos = new ArrayList<>();
    }

    public static ServicoRepository getInstance() {

        if (instance == null) {
            instance = new ServicoRepository();
        }

        return instance;
    }

    public List<Servico> listar() {
        return servicos;
    }

    public void salvar(Servico servico) {
        servicos.add(servico);
    }

    public Servico buscarPorCodigo(Long codigo) {
        for (Servico servico : servicos) {
            if (servico.getCodigo().equals(codigo)) {
                return servico;
            }
        }

        return null;
    }

    public boolean atualizar(Long codigo, Servico servicoAtualizado) {
        Servico servico = buscarPorCodigo(codigo);

        if (servico == null) {
            return false;
        }

        servico.setNome(servicoAtualizado.getNome());
        servico.setTempoEstimado(servicoAtualizado.getTempoEstimado());
        servico.setCustoTabelado(servicoAtualizado.getCustoTabelado());

        return true;
    }

    public boolean deletar(Long codigo) {
        return servicos.removeIf(servico ->
                servico.getCodigo().equals(codigo)
        );
    }
}


