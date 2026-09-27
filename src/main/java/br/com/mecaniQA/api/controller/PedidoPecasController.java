package br.com.mecaniQA.api.controller;

import br.com.mecaniQA.api.dto.PedidoPecasDTO;
import br.com.mecaniQA.api.mapper.PedidoPecasMapper;
import br.com.mecaniQA.api.model.ItemPedidoPeca;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.model.PedidoPecas;
import br.com.mecaniQA.api.model.StatusPedidoPecas;
import br.com.mecaniQA.api.repository.PecaRepository;
import br.com.mecaniQA.api.repository.PedidoPecasRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos-pecas")
public class PedidoPecasController {

    private final PedidoPecasRepository repository;
    private final PecaRepository pecaRepository;

    public PedidoPecasController() {

        this.repository =
                PedidoPecasRepository.getInstance();

        this.pecaRepository =
                PecaRepository.getInstance();
    }

    // =========================
    // CRIAR PEDIDO
    // =========================

    @PostMapping
    public ResponseEntity<?> criar(
            @RequestBody PedidoPecasDTO dto) {

        PedidoPecas pedido =
                new PedidoPecas.Builder()
                        .status(StatusPedidoPecas.EM_ORCAMENTO)
                        .build();

        PedidoPecas salvo =
                repository.salvar(pedido);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        PedidoPecasMapper.toDTO(salvo)
                );
    }

    // =========================
    // LISTAR
    // =========================

    @GetMapping
    public ResponseEntity<List<PedidoPecasDTO>> listar() {

        List<PedidoPecasDTO> pedidos =
                repository.listar()
                        .stream()
                        .map(PedidoPecasMapper::toDTO)
                        .toList();

        return ResponseEntity.ok(pedidos);
    }

    // =========================
    // BUSCAR
    // =========================

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscar(
            @PathVariable Long codigo) {

        PedidoPecas pedido =
                repository.buscarPorCodigo(codigo);

        if (pedido == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Pedido de peças não encontrado.");
        }

        return ResponseEntity.ok(
                PedidoPecasMapper.toDTO(pedido)
        );
    }

    // =========================
    // ADICIONAR PEÇA
    // =========================

    @PostMapping("/{codigo}/itens")
    public ResponseEntity<?> adicionarItem(
            @PathVariable Long codigo,
            @RequestBody br.com.mecaniQA.api.dto.ItemPedidoPecaDTO dto) {

        PedidoPecas pedido =
                repository.buscarPorCodigo(codigo);

        if (pedido == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Pedido de peças não encontrado.");
        }

        if (pedido.getStatus()
                != StatusPedidoPecas.EM_ORCAMENTO) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Só é possível adicionar peças a pedidos no status EM_ORCAMENTO."
                    );
        }

        if (dto.getPecaCodigo() == null) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("O código da peça é obrigatório.");
        }

        if (dto.getQuantidade() == null ||
                dto.getQuantidade() <= 0) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "A quantidade deve ser maior que zero."
                    );
        }

        Peca peca =
                pecaRepository.buscarPorCodigo(
                        dto.getPecaCodigo()
                );

        if (peca == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Peça não encontrada.");
        }

        ItemPedidoPeca item =
                new ItemPedidoPeca.Builder()
                        .peca(peca)
                        .quantidade(dto.getQuantidade())
                        .build();

        pedido.adicionarItem(item);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        PedidoPecasMapper.toDTO(pedido)
                );
    }

    // =========================
    // ALTERAR STATUS
    // =========================

    @PatchMapping("/{codigo}/status")
    public ResponseEntity<?> alterarStatus(
            @PathVariable Long codigo,
            @RequestBody PedidoPecasDTO dto) {

        PedidoPecas pedido =
                repository.buscarPorCodigo(codigo);

        if (pedido == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Pedido de peças não encontrado.");
        }

        if (dto.getStatus() == null) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("O status é obrigatório.");
        }

        pedido.setStatus(dto.getStatus());

        return ResponseEntity.ok(
                PedidoPecasMapper.toDTO(pedido)
        );
    }
}