# Assembleia

https://sicredi-assembleia.herokuapp.com/


## Rodando o projeto:

Requisitos:

- Java 11
- Docker

O projeto pode ser iniciado pelo comando `make run-local`, que iniciará uma instância do banco de dados utilizando o arquivo `docker-compose.yml` e em seguida rodando a aplicação usando `./gradlew bootRun`.

Para rodar os testes basta usar o comando `make test`.

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
	"durationInMinutes": 60
}
```

O campo `subjectId` é de preenchimento obrigatório, o campo `durationInMinutes` indica a duração da sessão de votação em minutos, e no caso de omitido será aplicado o prazo de 1 minuto a partir do recebimento da requisição. A criação de mais de uma sessão de votação para uma mesma pauta é permitida, portanto, não existe a verificação de já existir uma votação ativa para a pauta.

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

### Resultado da votação:

GET `/voting-session/{votingSessionId}/report`

A _path variable_ `votingSessionId` é de preenchimento obrigatório.

**Respostas da requisição:**

Quando for encontrada uma sessão de votação com o identificador informado na URL, **mesmo que a sessão ainda esteja aberta**, será retornada uma resposta como a seguinte:


```jsx
200 - OK
{
    "id": 1,
    "votesInFavor": 500, 
    "votesAgainst": 200,
    "votesCount": 700,
    "result": "Subject approved with 500 votes in favor and 200 votes against"
}
```


No caso da sessão não existir, a resposta será semelhante à seguinte:

```jsx
404 - NOT FOUND

"Voting session with id 1 is not available."
```

## Pontos de melhorias:

- Melhor tratamento de exceções do tipo ConstraintViolationException e das mensagens de erro retornadas
- Query que busca os votos no banco poderia ser melhorada para buscar todos os dados em uma única consulta em vez de uma para cada tipo de voto
- Usar um ENUM para mapear os valores dos votos
- Melhorar a validação dos valores dos votos
- Usar alguma ferramenta adequada para documentação da API, ex: Swagger
- Dividir os tipos diferentes de teste em arquivos separados do gradle e tasks diferentes, ex: make integration para rodar apenas testes de integração
- Adicionar testes de componente apontando para uma implementação de banco de dados em memória
