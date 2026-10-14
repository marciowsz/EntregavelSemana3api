# API Escola — Spring Boot + PostgreSQL (Semanas 02 e 03)

API REST de **Alunos** e **Cursos** construída com Spring Boot, arquitetura em camadas e persistência real no PostgreSQL via JPA/Hibernate.

## Fluxo de uma requisição

```
Cliente (Postman) → Controller → Service → Repository → PostgreSQL
```

| Camada | Classe | Responsabilidade |
|---|---|---|
| Entity | `Aluno`, `Curso` | Mapeamento JPA (`@Entity`, `@Id`, `@GeneratedValue`) — o Hibernate cria as tabelas |
| Repository | `AlunoRepository`, `CursoRepository` | Estendem `JpaRepository` e herdam `save`, `findAll`, `findById`, `delete` |
| Service | `AlunoService`, `CursoService` | Regras de negócio (ex.: erro 404 quando o id não existe) |
| Controller | `AlunoController`, `CursoController` | Endpoints REST, recebem e devolvem JSON |

A **injeção de dependência** é feita via construtor: o Spring cria os Beans (`@Repository`, `@Service`, `@RestController`) e entrega automaticamente o Repository ao Service e o Service ao Controller. Com um único construtor, o `@Autowired` é opcional.

## Como rodar

1. Crie o banco no PostgreSQL: `CREATE DATABASE escola;`
2. Ajuste usuário e senha em `src/main/resources/application.properties`.
3. Execute `mvn spring-boot:run` (ou rode a classe `ApiApplication` pela IDE).
4. No console, com `show-sql=true`, aparecem os comandos `create table aluno` e `create table curso` gerados pelo Hibernate (`ddl-auto=update`).

## Endpoints

### Desafio prático

| Método | URL | Resposta |
|---|---|---|
| GET | `/alunos/ra` | `O RA do aluno [Seu Nome] é [Seu RA]` |

### Alunos — CRUD com DTO (Semana 03)

| Método | URL | Corpo | Sucesso | Id inexistente |
|---|---|---|---|---|
| POST | `/alunos` | `AlunoRequest` | **201 Created** + `AlunoResponse` | — |
| GET | `/alunos` | — | **200 OK** + lista de `AlunoResponse` | — |
| GET | `/alunos/{id}` | — | **200 OK** + `AlunoResponse` | 404 Not Found |
| PUT | `/alunos/{id}` | `AlunoRequest` | **204 No Content** | 404 Not Found |
| DELETE | `/alunos/{id}` | — | **204 No Content** | 404 Not Found |

### Cursos

| Método | URL | Descrição |
|---|---|---|
| GET | `/cursos` | Lista todos |
| GET | `/cursos?turno=noite` | Filtra por turno (`@RequestParam` opcional) |
| GET | `/cursos/{id}` | Busca por id |
| POST | `/cursos` | Cria |
| PUT | `/cursos/{id}` | Atualiza |
| DELETE | `/cursos/{id}` | Exclui |

## Testando no Postman / Insomnia

**POST** `http://localhost:8080/alunos` — Body → raw → JSON:

```json
{
  "nome": "Ana Souza",
  "ra": "123456",
  "email": "ana@email.com"
}
```

**POST** `http://localhost:8080/cursos`:

```json
{
  "descricao": "Análise e Desenvolvimento de Sistemas",
  "turno": "Noite"
}
```

Depois, **GET** `http://localhost:8080/alunos` e `http://localhost:8080/cursos` para listar o que foi salvo no banco. No console aparece o SQL executado, por exemplo:

```sql
insert into aluno (email, nome, ra) values (?, ?, ?)
select a1_0.id, a1_0.email, a1_0.nome, a1_0.ra from aluno a1_0
```

---

## Semana 03 — DTOs e CRUD completo

### Por que DTO?

| Classe | Papel |
|---|---|
| `AlunoRequest` | Dados de **entrada** (POST e PUT): `nome`, `ra`, `email`. Não tem `id`, então o cliente não consegue forçar um id nem sobrescrever outro registro. |
| `AlunoResponse` | Dados de **saída**: `id`, `nome`, `ra`, `email`. Construída a partir da Entity. |
| `Aluno` (Entity) | Só existe entre Service, Repository e banco; **nunca** sai pela API. |

Assim, a estrutura do banco pode mudar (por exemplo, um campo interno novo na Entity) sem quebrar quem consome a API, e nenhum dado interno vaza por engano.

### Fluxo de cada operação

| Operação | O que o Service faz |
|---|---|
| `create` | Copia os campos do `AlunoRequest` para um `new Aluno()`, chama `repository.save()` e devolve `new AlunoResponse(salvo)`. |
| `getById` | `Optional<Aluno> optional = repository.findById(id)`; `if (optional.isPresent())` chama `optional.get()` e converte para `AlunoResponse`, `else` devolve `null` e o Controller responde 404. |
| `update` | Busca com `Optional` e `if/else`; se existe, atualiza os campos da Entity com o `AlunoRequest` e salva de novo. |
| `delete` | `if (repository.existsById(id))` chama `deleteById(id)`; senão, o Controller responde 404 em vez de estourar erro com id inválido. |

Todo o tratamento é feito com `if/else` explícito, **sem expressões lambda** (sem `orElseThrow(() -> ...)`, `map(...)` ou `forEach`). Até a conversão da lista em `getAll` usa um `for` tradicional.

### Testes no Postman

| # | Requisição | Resposta esperada |
|---|---|---|
| 1 | `POST /alunos` com `{"nome":"Ana","ra":"123","email":"ana@email.com"}` | 201 + JSON com `id` |
| 2 | `GET /alunos/1` | 200 + JSON do aluno |
| 3 | `GET /alunos/99` | 404 |
| 4 | `PUT /alunos/1` com `{"nome":"Ana Souza","ra":"123","email":"novo@email.com"}` | 204, sem corpo |
| 5 | `GET /alunos/1` | 200, com o nome atualizado |
| 6 | `DELETE /alunos/1` | 204 |
| 7 | `DELETE /alunos/1` de novo | 404 (o registro já não existe) |
