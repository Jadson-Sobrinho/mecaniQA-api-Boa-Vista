package br.com.mecaniQA.api.repository;

import br.com.mecaniQA.api.model.OrdemServico;

import java.util.ArrayList;
import java.util.List;

public class OrdemServicoRepository {

    private static OrdemServicoRepository instance;

    private final List<OrdemServico> ordens = new ArrayList<>();

    private Long proximoCodigo = 1L;

    private OrdemServicoRepository() {
    }

    public static OrdemServicoRepository getInstance() {

        if (instance == null) {
            instance = new OrdemServicoRepository();
        }

        return instance;
    }

    public OrdemServico salvar(OrdemServico ordemServico) {

        ordemServico.setCodigo(proximoCodigo++);

        ordens.add(ordemServico);

        return ordemServico;
    }

    public List<OrdemServico> listar() {

        return new ArrayList<>(ordens);
    }

    public OrdemServico buscarPorCodigo(Long codigo) {

        for (OrdemServico ordem : ordens) {

            if (ordem.getCodigo().equals(codigo)) {
                return ordem;
            }
        }

        return null;
    }

    public OrdemServico atualizar(Long codigo, OrdemServico ordemAtualizada) {

        OrdemServico ordemExistente = buscarPorCodigo(codigo);

        if (ordemExistente == null) {
            return null;
        }

        ordemExistente.setDescricao(ordemAtualizada.getDescricao());
        ordemExistente.setStatus(ordemAtualizada.getStatus());
        ordemExistente.setPecas(ordemAtualizada.getPecas());
        ordemExistente.setServicos(ordemAtualizada.getServicos());

        return ordemExistente;
    }

    public boolean deletar(Long codigo) {

        OrdemServico ordem = buscarPorCodigo(codigo);

        if (ordem == null) {
            return false;
        }

        ordens.remove(ordem);

        return true;
    }
}