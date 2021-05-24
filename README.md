# Assembleia
## Rodando o projeto:

Requisitos:

- Java 11
- Docker

O projeto pode ser iniciado pelo comando `make run-local`, que iniciará uma instância do banco de dados utilizando o arquivo `docker-compose.yml` e em seguida rodando a aplicação usando `./gradlew bootRun`

## Endpoints disponíveis

- Cadastrar nova pauta
- Abrir sessão de votação para uma pauta
- Receber votos

### Cadastrar nova pauta:

POST /voting-session

Corpo da requisição:

```json
{
	"title": "Título da pauta a ser votada",
	"details": "Detalhes sobre a pauta"
}
```

O campo `title` é de preenchimento obrigatório.

Respostas da requisição:

Quando a requisição for enviada corretamente e não houver outra pauta com o mesmo assunto e título, será retornada uma resposta como a seguinte:

```jsx
201 - CREATED
{
	"id": 1,
	"title": "Título da pauta a ser votada",
	"details": "Detalhes sobre a pauta"
}
```

No caso da pauta a ser cadastrada já existir na aplicação, a resposta será semelhante à seguinte:

```jsx
409 - CONFLICT

"Subject already exists."
```

No caso do corpo da requisição não estar de acordo com o contrato, a resposta será a seguinte:

```jsx
400 - BAD REQUEST
```

### Abrir sessão de votação para uma pauta:

POST /voting-session

Corpo da requisição:

```json
{
	"subjectId": 1,
	"endsAt": "2021-05-25T01:30:00"
}
```

O campo `subjectId` é de preenchimento obrigatório, o campo `endsAt` indica a data e hora de término da sessão de votação, e no caso de omitido será aplicado o prazo de 1 minuto a partir do recebimento da requisição. A criação de mais de uma sessão de votação para uma mesma pauta é permitida, portanto, não existe a verificação de já existir uma votação ativa para a pauta.

Respostas da requisição:

Quando a sessão for criada corretamente, será retornada uma resposta como a seguinte:

```jsx
201 - CREATED
{
	"id": 1,
	"subjectId": 1,
	"endsAt": "2021-05-25T01:30:00"
}
```

No caso do corpo da requisição não estar de acordo com o contrato, a resposta será a seguinte:

```jsx
400 - BAD REQUEST
```

### Receber votos:

POST /vote

**Corpo da requisição:**

```json
{
	"userId": "04323459687",
	"votingSessionId": 1,
	"voteValue": "Sim"
}
```

O campo `userId` foi atribuído como String para aceitar o recebimento tanto de ids numéricos quanto alfanuméricos como UUIDs.

O campo `voteValue` deve ser preenchido apenas com os valores `sim` ou `não`, aceitando caixa alta ou caixa baixa para qualquer das letras.

Todos os campos têm preenchimento obrigatório.

**Respostas da requisição:**

Quando a requisição for enviada corretamente, caso o usuário ainda não tenha votado e a sessão de votação esteja aberta, será retornada uma resposta como a seguinte:


```jsx
201 - CREATED
{
	"id": 1,
	"userId": "04323459687",
	"votingSessionId": 1,
	"voteValue": "Sim"
}
```

No caso do usuário já ter um voto computado para a pauta, a resposta será semelhante à seguinte:

```jsx
409 - CONFLICT

"User with id 04323459687 already voted for this session."
```

No caso da sessão já ter expirado ou não existir, a resposta será semelhante à seguinte:

```jsx
404 - NOT FOUND

"Voting session with id 1 is not available."
```

No caso do corpo da requisição não estar de acordo com o contrato, a resposta será a seguinte:

```jsx
400 - BAD REQUEST
```