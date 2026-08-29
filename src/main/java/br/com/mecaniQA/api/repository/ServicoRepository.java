package br.com.mecaniQA.api.repository;

import br.com.mecaniQA.api.Servico;

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
}