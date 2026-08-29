package br.com.mecaniQA.api.repository;

import br.com.mecaniQA.api.Peca;

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
}