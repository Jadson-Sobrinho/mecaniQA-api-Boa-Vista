# MecâniQA API

API desenvolvida para o sistema **MecâniQA**, responsável pelo gerenciamento de peças, serviços, ordens de serviço e pedidos de peças de uma oficina mecânica.

O projeto utiliza **Spring Boot**, arquitetura baseada em Controllers, DTOs, Mappers, Models e Repositories, com persistência **em memória utilizando Singleton**, conforme os requisitos da atividade.

---

# 1. Tecnologias utilizadas

* Java 21
* Spring Boot
* Gradle
* Spring Web MVC
* REST API
* Singleton
* Builder
* DTO (Data Transfer Object)
* Mapper
* Postman para testes
* Persistência em memória

A biblioteca **Lombok não é utilizada**. Construtores, getters, setters e demais métodos são implementados manualmente.

---

# 2. Estrutura do projeto

```text
src/
└── main/
    └── java/
        └── br/
            └── com/
                └── mecaniQA/
                    └── api/
                        ├── controller/
                        │   ├── PecaController.java
                        │   ├── ServicoController.java
                        │   ├── OrdemServicoController.java
                        │   └── PedidoPecasController.java
                        │
                        ├── dto/
                        │   ├── PecaDTO.java
                        │   ├── ServicoDTO.java
                        │   ├── OrdemServicoDTO.java
                        │   ├── PedidoPecasDTO.java
                        │   └── ItemPedidoPecaDTO.java
                        │
                        ├── mapper/
                        │   ├── PecaMapper.java
                        │   ├── ServicoMapper.java
                        │   ├── OrdemServicoMapper.java
                        │   ├── PedidoPecasMapper.java
                        │   └── ItemPedidoPecaMapper.java
                        │
                        ├── model/
                        │   ├── Peca.java
                        │   ├── Servico.java
                        │   ├── OrdemServico.java
                        │   ├── PedidoPecas.java
                        │   ├── ItemPedidoPeca.java
                        │   ├── CategoriaPeca.java
                        │   ├── StatusOrdemServico.java
                        │   └── StatusPedidoPecas.java
                        │
                        └── repository/
                            ├── PecaRepository.java
                            ├── ServicoRepository.java
                            ├── OrdemServicoRepository.java
                            └── PedidoPecasRepository.java
```

---

# 3. Entidades principais

## 3.1 Peca

Representa uma peça disponível no estoque da oficina.

Principais atributos:

```java
private Long codigo;
private String nome;
private String codigoBarras;
private String fornecedorMarca;
private int quantidadeEstoque;
private double precoCusto;
private double precoVenda;
private CategoriaPeca categoria;
private String tamanho;
private String cor;
```

A peça pode ser associada a um ou vários itens de pedido.

---

# 4. Serviço

Representa um serviço oferecido pela oficina.

Principais atributos:

```java
private Long codigo;
private String nome;
private String tempoEstimado;
private double custoTabelado;
```

Os serviços podem ser utilizados em uma Ordem de Serviço.

---

# 5. Ordem de Serviço

A **Ordem de Serviço (OS)** representa o serviço que será realizado pela oficina.

A entidade possui:

```java
private Long codigo;
private String descricao;
private StatusOrdemServico status;
private List<Peca> pecas;
private List<Servico> servicos;
```

## 5.1 Status da Ordem de Serviço

A OS obrigatoriamente trabalha com os seguintes estados:

```text
ABERTO
PENDENTE_PAGAMENTO
PAGO
EM_EXECUCAO
EXECUTADO
```

Representados pelo enum:

```java
public enum StatusOrdemServico {
    ABERTO,
    PENDENTE_PAGAMENTO,
    PAGO,
    EM_EXECUCAO,
    EXECUTADO
}
```

---

# 6. Padrão Builder na Ordem de Serviço

A criação da `OrdemServico` utiliza o padrão de projeto **Builder**.

O Builder é uma classe estática aninhada dentro de `OrdemServico`.

Exemplo simplificado:

```java
public static class Builder {

    private Long codigo;
    private String descricao;
    private StatusOrdemServico status;
    private List<Peca> pecas = new ArrayList<>();
    private List<Servico> servicos = new ArrayList<>();

    public Builder codigo(Long codigo) {
        this.codigo = codigo;
        return this;
    }

    public Builder descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public Builder status(StatusOrdemServico status) {
        this.status = status;
        return this;
    }

    public Builder pecas(List<Peca> pecas) {
        this.pecas = pecas;
        return this;
    }

    public Builder servicos(List<Servico> servicos) {
        this.servicos = servicos;
        return this;
    }

    public OrdemServico build() {
        return new OrdemServico(this);
    }
}
```

Cada método retorna `this`, permitindo o encadeamento:

```java
OrdemServico ordem = new OrdemServico.Builder()
        .codigo(1L)
        .descricao("Revisão do sistema de freios")
        .status(StatusOrdemServico.ABERTO)
        .build();
```

O construtor da `OrdemServico` recebe somente o Builder:

```java
private OrdemServico(Builder builder) {
    this.codigo = builder.codigo;
    this.descricao = builder.descricao;
    this.status = builder.status;
    this.pecas = builder.pecas;
    this.servicos = builder.servicos;
}
```

---

# 7. Pedido de Peças

O `PedidoPecas` representa um pedido realizado pelo estoquista para separar ou comprar materiais necessários para a oficina.

A entidade possui:

```java
private Long codigo;
private StatusPedidoPecas status;
private List<ItemPedidoPeca> itens;
```

O pedido mantém uma lista de itens, e cada item representa uma peça e sua quantidade.

---

# 8. Status do Pedido de Peças

O enum atual é:

```java
public enum StatusPedidoPecas {

    EM_ORCAMENTO,
    PENDENDE_PAGAMENTO,
    PAGO,
    ENTREGUE
}
```

> **Observação:** o nome `PENDENDE_PAGAMENTO` está mantido conforme a implementação atual do projeto. Caso desejado, posteriormente pode ser corrigido para `PENDENTE_PAGAMENTO`, mas isso exigirá alterar as referências existentes.

---

# 9. Entidade Associativa — ItemPedidoPeca

A entidade `ItemPedidoPeca` é responsável por representar a relação entre um pedido e uma peça.

Ela possui:

```java
private Long codigo;
private PedidoPecas pedido;
private Peca peca;
private Integer quantidade;
```

Essa entidade é necessária porque um pedido pode possuir várias peças e cada peça pode possuir uma quantidade diferente.

Exemplo:

```text
Pedido 1
│
├── Pastilha de freio → quantidade: 4
├── Filtro de óleo    → quantidade: 2
└── Correia dentada   → quantidade: 1
```

---

# 10. Relacionamento bidirecional

O relacionamento entre `PedidoPecas` e `ItemPedidoPeca` é bidirecional.

O pedido possui seus itens:

```java
private List<ItemPedidoPeca> itens;
```

Enquanto cada item conhece o pedido ao qual pertence:

```java
private PedidoPecas pedido;
```

Ao adicionar um item:

```java
public void adicionarItem(ItemPedidoPeca item) {

    if (item == null) {
        return;
    }

    if (!itens.contains(item)) {
        itens.add(item);
    }

    item.setPedido(this);
}
```

Assim, as duas referências são mantidas:

```text
PedidoPecas
     │
     │ 1
     │
     │ possui
     │
     │ N
     ▼
ItemPedidoPeca
     │
     │ N
     │
     │ referencia
     │
     │ 1
     ▼
PedidoPecas
```

---

# 11. Regra de negócio do Pedido

Somente pedidos no status:

```text
EM_ORCAMENTO
```

podem receber novas peças.

Portanto, uma tentativa de adicionar uma peça quando o pedido estiver, por exemplo:

```text
PENDENDE_PAGAMENTO
PAGO
ENTREGUE
```

deve ser rejeitada.

Exemplo de validação:

```java
if (pedido.getStatus() != StatusPedidoPecas.EM_ORCAMENTO) {

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                "Só é possível adicionar peças a pedidos no status EM_ORCAMENTO."
            );
}
```

---

# 12. Fluxo de um Pedido

O fluxo esperado é:

```text
EM_ORCAMENTO
      │
      ▼
PENDENDE_PAGAMENTO
      │
      ▼
PAGO
      │
      ▼
ENTREGUE
```

Enquanto estiver em `EM_ORCAMENTO`, o estoquista pode adicionar peças.

Depois que o pedido avançar para os próximos estados, novas peças não devem ser adicionadas.

---

# 13. DTOs

A API não deve expor diretamente as entidades de domínio.

Por isso são utilizados **DTOs (Data Transfer Objects)**.

O DTO funciona como uma estrutura específica para transportar dados entre o cliente e a API.

Exemplo:

```java
public class OrdemServicoDTO {

    private Long codigo;
    private String descricao;
    private StatusOrdemServico status;

    // construtores
    // getters
    // setters
}
```

O Controller trabalha com DTO:

```java
@RequestBody OrdemServicoDTO dto
```

e não diretamente com:

```java
@RequestBody OrdemServico ordemServico
```

---

# 14. Mappers

Os Mappers são responsáveis pela conversão entre DTO e entidade.

## DTO → Entidade

Exemplo:

```java
public static OrdemServico toEntity(OrdemServicoDTO dto) {

    return new OrdemServico.Builder()
            .codigo(dto.getCodigo())
            .descricao(dto.getDescricao())
            .status(dto.getStatus())
            .build();
}
```

## Entidade → DTO

```java
public static OrdemServicoDTO toDTO(OrdemServico ordemServico) {

    return new OrdemServicoDTO(
            ordemServico.getCodigo(),
            ordemServico.getDescricao(),
            ordemServico.getStatus()
    );
}
```

O fluxo fica:

```text
JSON
 │
 ▼
DTO
 │
 ▼
Mapper
 │
 ▼
Entidade
 │
 ▼
Repository
```

No retorno:

```text
Repository
 │
 ▼
Entidade
 │
 ▼
Mapper
 │
 ▼
DTO
 │
 ▼
JSON
```

---

# 15. DTOs do Pedido

O pedido também possui seus DTOs.

## PedidoPecasDTO

Representa os dados do pedido:

```java
public class PedidoPecasDTO {

    private Long codigo;
    private StatusPedidoPecas status;
    private List<ItemPedidoPecaDTO> itens;
}
```

## ItemPedidoPecaDTO

Representa os dados necessários para um item:

```java
public class ItemPedidoPecaDTO {

    private Long codigo;
    private Long pecaCodigo;
    private Integer quantidade;
}
```

O DTO do item não precisa receber o objeto `Peca` completo.

Por exemplo:

```json
{
    "pecaCodigo": 10,
    "quantidade": 5
}
```

O Mapper/Controller pode localizar a peça através do seu código.

---

# 16. Controllers

Os Controllers são responsáveis por receber as requisições HTTP e devolver as respostas.

## PecaController

Endpoint base:

```text
/api/pecas
```

Operações:

| Método | Endpoint              | Função         |
| ------ | --------------------- | -------------- |
| GET    | `/api/pecas`          | Listar peças   |
| GET    | `/api/pecas/{codigo}` | Buscar peça    |
| POST   | `/api/pecas`          | Criar peça     |
| PUT    | `/api/pecas/{codigo}` | Atualizar peça |
| DELETE | `/api/pecas/{codigo}` | Remover peça   |

---

# 17. ServicoController

Endpoint base:

```text
/api/servicos
```

| Método | Endpoint                 | Função            |
| ------ | ------------------------ | ----------------- |
| GET    | `/api/servicos`          | Listar serviços   |
| GET    | `/api/servicos/{codigo}` | Buscar serviço    |
| POST   | `/api/servicos`          | Criar serviço     |
| PUT    | `/api/servicos/{codigo}` | Atualizar serviço |
| DELETE | `/api/servicos/{codigo}` | Remover serviço   |

---

# 18. OrdemServicoController

Endpoint base:

```text
/api/ordens-servico
```

| Método | Endpoint                       | Função       |
| ------ | ------------------------------ | ------------ |
| GET    | `/api/ordens-servico`          | Listar OS    |
| GET    | `/api/ordens-servico/{codigo}` | Buscar OS    |
| POST   | `/api/ordens-servico`          | Criar OS     |
| PUT    | `/api/ordens-servico/{codigo}` | Atualizar OS |
| DELETE | `/api/ordens-servico/{codigo}` | Remover OS   |

Na criação da OS, são realizadas validações como:

* descrição obrigatória;
* status obrigatório.

---

# 19. PedidoPecasController

Endpoint base:

```text
/api/pedidos-pecas
```

Operações relacionadas às histórias de usuário:

### Criar pedido

```http
POST /api/pedidos-pecas
```

Um novo pedido inicia em:

```text
EM_ORCAMENTO
```

### Adicionar peça

```http
POST /api/pedidos-pecas/{codigo}/itens
```

Exemplo:

```json
{
    "pecaCodigo": 1,
    "quantidade": 5
}
```

A operação somente é permitida quando o pedido está:

```text
EM_ORCAMENTO
```

### Alterar status

```http
PATCH /api/pedidos-pecas/{codigo}/status
```

Exemplo:

```json
{
    "status": "PAGO"
}
```

---

# 20. Repositories

Os repositories utilizam o padrão **Singleton** e armazenam os dados em memória.

Não existe banco de dados nesta implementação.

Exemplo:

```java
public class OrdemServicoRepository {

    private static OrdemServicoRepository instance;

    private final List<OrdemServico> ordens =
            new ArrayList<>();

    private OrdemServicoRepository() {
    }

    public static OrdemServicoRepository getInstance() {

        if (instance == null) {
            instance = new OrdemServicoRepository();
        }

        return instance;
    }
}
```

O mesmo conceito é utilizado nos repositories de:

* `Peca`
* `Servico`
* `OrdemServico`
* `PedidoPecas`

---

# 21. Padrão Singleton

O Singleton garante uma única instância do Repository durante a execução da aplicação.

O acesso é realizado através de:

```java
PecaRepository.getInstance();
```

ou:

```java
OrdemServicoRepository.getInstance();
```

Isso permite que os dados armazenados em memória sejam compartilhados pelos Controllers.

---

# 22. Arquitetura geral

A arquitetura da aplicação pode ser representada da seguinte forma:

```text
                    CLIENTE
                       │
                       │ HTTP/JSON
                       ▼
                ┌──────────────┐
                │  Controller  │
                └──────┬───────┘
                       │
                       ▼
                   ┌───────┐
                   │  DTO  │
                   └───┬───┘
                       │
                       ▼
                  ┌──────────┐
                  │  Mapper  │
                  └────┬─────┘
                       │
                       ▼
                ┌──────────────┐
                │    Model     │
                │              │
                │ Peca         │
                │ Servico      │
                │ OrdemServico │
                │ PedidoPecas  │
                └──────┬───────┘
                       │
                       ▼
                ┌──────────────┐
                │  Repository  │
                │   Singleton  │
                └──────┬───────┘
                       │
                       ▼
                MEMÓRIA DA API
```

---

# 23. Relacionamentos principais

```text
OrdemServico
│
├── 0..* Peca
│
└── 0..* Servico


PedidoPecas
│
└── 1..* ItemPedidoPeca
             │
             └── 1 Peca
```

A relação entre `PedidoPecas` e `Peca` é realizada através de:

```text
ItemPedidoPeca
```

Isso permite registrar a quantidade de cada peça dentro de um pedido.

---

# 24. Exemplo completo de relacionamento

Considere:

```text
Pedido #1
Status: EM_ORCAMENTO
```

Com:

```text
Item #1
Peça: Pastilha de Freio
Quantidade: 4

Item #2
Peça: Filtro de Óleo
Quantidade: 2
```

O modelo lógico será:

```text
                 PedidoPecas
                 codigo = 1
                 status = EM_ORCAMENTO
                       │
             ┌─────────┴─────────┐
             │                   │
             ▼                   ▼
      ItemPedidoPeca       ItemPedidoPeca
      quantidade = 4       quantidade = 2
             │                   │
             ▼                   ▼
       Peca: Pastilha       Peca: Filtro
       de Freio             de Óleo
```

---

# 25. Histórias de usuário implementadas

## Criar um Pedido

> **COMO** estoquista da MecâniQA,
> **EU QUERO** criar um novo Pedido de Peças
> **PARA QUE** eu possa iniciar a separação ou compra dos materiais necessários para a oficina.

Endpoint:

```http
POST /api/pedidos-pecas
```

O pedido inicia em:

```text
EM_ORCAMENTO
```

---

## Adicionar peças a um pedido existente

> **COMO** estoquista,
> **EU QUERO** adicionar novas peças e suas respectivas quantidades a um pedido já aberto
> **PARA QUE** a lista de materiais reflita exatamente a necessidade do reparo.

Endpoint:

```http
POST /api/pedidos-pecas/{codigo}/itens
```

Regra:

```text
Pedido deve estar EM_ORCAMENTO.
```

---

## Modificar o status do Pedido

> **COMO** gerente de suprimentos,
> **EU QUERO** atualizar o status do Pedido
> **PARA QUE** o financeiro e a oficina saibam em que etapa o pedido se encontra.

Endpoint:

```http
PATCH /api/pedidos-pecas/{codigo}/status
```

Estados disponíveis:

```text
EM_ORCAMENTO
PENDENDE_PAGAMENTO
PAGO
ENTREGUE
```

---

# 26. Resumo dos padrões utilizados

| Padrão/Técnica       | Utilização                     |
| -------------------- | ------------------------------ |
| Singleton            | Repositories                   |
| Builder              | Construção da Ordem de Serviço |
| DTO                  | Transferência de dados da API  |
| Mapper               | Conversão DTO ↔ Entidade       |
| Entidade Associativa | `ItemPedidoPeca`               |
| REST                 | Comunicação HTTP da API        |

---

# 27. Requisitos técnicos atendidos

* [x] Java
* [x] Spring Boot
* [x] API REST
* [x] Repositories em memória
* [x] Singleton nos repositories
* [x] Sem Lombok
* [x] Getters e setters manuais
* [x] Construtores manuais
* [x] Ordem de Serviço
* [x] Status da Ordem de Serviço
* [x] Pedido de Peças
* [x] Status do Pedido de Peças
* [x] Entidade associativa `ItemPedidoPeca`
* [x] Relacionamento bidirecional Pedido ↔ Item
* [x] DTOs
* [x] Mappers
* [x] Builder para Ordem de Serviço
* [x] Regra de negócio para adição de peças
* [x] Controllers REST
* [x] Testes de endpoints utilizando Postman

---

# 28. Fluxo final da aplicação

```text
                    ┌─────────────┐
                    │   Cliente   │
                    └──────┬──────┘
                           │
                           ▼
                    ┌─────────────┐
                    │ Controller  │
                    └──────┬──────┘
                           │
                           ▼
                       ┌───────┐
                       │  DTO  │
                       └───┬───┘
                           │
                           ▼
                      ┌─────────┐
                      │ Mapper  │
                      └────┬────┘
                           │
             ┌─────────────┴──────────────┐
             │                            │
             ▼                            ▼
      ┌──────────────┐             ┌──────────────┐
      │ OrdemServico │             │ PedidoPecas  │
      │    Builder   │             │              │
      └──────┬───────┘             └──────┬───────┘
             │                            │
             │                            ▼
             │                     ┌──────────────┐
             │                     │ItemPedidoPeca│
             │                     └──────┬───────┘
             │                            │
             │                            ▼
             │                         ┌──────┐
             │                         │ Peca │
             │                         └──────┘
             │
             ▼
       ┌────────────┐
       │ Repository │
       │ Singleton  │
       └─────┬──────┘
             │
             ▼
       ┌────────────┐
       │  Memória   │
       └────────────┘
```

---

# 29. Conclusão

A estrutura da MecâniQA foi organizada separando as responsabilidades entre **Controllers, DTOs, Mappers, Models e Repositories**.

A `OrdemServico` utiliza o padrão **Builder** para facilitar sua construção, enquanto `ItemPedidoPeca` funciona como entidade associativa entre `PedidoPecas` e `Peca`.

Os pedidos possuem um fluxo de estados definido e a inclusão de novas peças é controlada pela regra de negócio que permite alterações somente enquanto o pedido estiver em `EM_ORCAMENTO`.

Os repositories permanecem utilizando **Singleton com armazenamento em memória**, mantendo a restrição da atividade de não utilizar banco de dados e não utilizar Lombok.
