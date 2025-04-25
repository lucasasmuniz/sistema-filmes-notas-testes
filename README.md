# DSMovie - Desafio de Testes com JaCoCo

Este repositório contém a resolução do **Desafio DSMovie Jacoco**, com o objetivo de implementar testes unitários para os serviços (`services`) do projeto DSMovie.

## Objetivo Pessoal

Este desafio foi realizado como forma de aprimorar minhas práticas com testes automatizados em Java. Através dele, explorei com mais profundidade conceitos de cobertura de código, testes unitários e a utilização da ferramenta **JaCoCo** para análise de cobertura.

---

## Sobre o Desafio

O projeto **DSMovie** é uma aplicação de filmes e avaliações, onde:

- Os dados dos filmes são públicos;
- Apenas usuários com perfil `ADMIN` podem alterar os dados (inserir, atualizar, deletar);
- Avaliações de filmes (`Score`) podem ser feitas por qualquer usuário logado (`CLIENT` ou `ADMIN`);
- Toda vez que um usuário registra uma nota (0 a 5), o sistema recalcula a média das notas e armazena essa média e a contagem de votos na entidade `Movie`.

### Testes Implementados

Os seguintes testes unitários foram desenvolvidos para alcançar 100% de cobertura com o **JaCoCo**:

#### `MovieServiceTests`
- `findAllShouldReturnPagedMovieDTO`
- `findByIdShouldReturnMovieDTOWhenIdExists`
- `findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist`
- `insertShouldReturnMovieDTO`
- `updateShouldReturnMovieDTOWhenIdExists`
- `updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist`
- `deleteShouldDoNothingWhenIdExists`
- `deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist`
- `deleteShouldThrowDatabaseExceptionWhenDependentId`

#### `ScoreServiceTests`
- `saveScoreShouldReturnMovieDTO`
- `saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId`

#### `UserServiceTests`
- `authenticatedShouldReturnUserEntityWhenUserExists`
- `authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists`
- `loadUserByUsernameShouldReturnUserDetailsWhenUserExists`
- `loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists`

---

## Sobre os Controllers

Os testes deste desafio foram focados exclusivamente nas camadas de **serviço** (`service`). A camada de **controller** não foi coberta por testes unitários, pois geralmente **não contém lógica de negócio, estruturas condicionais ou ramificações complexas**.

Normalmente, os métodos dos controllers apenas encaminham a requisição para os services, e por isso a cobertura de testes nesses pontos não é considerada essencial. Caso fosse necessário testar os controllers, o ideal seria realizar **testes de integração** ou testes de API usando ferramentas como `MockMvc` ou `RestAssured`.

---

## Cobertura de Código vs Cobertura de Testes

### Cobertura de Código
Mede **quanto do código-fonte foi executado** durante a execução dos testes. É uma métrica quantitativa. Pode ser medida com tipos básicos de cobertura:

- **Statement Coverage (Line Coverage)**  
  `= (linhas executadas / total de linhas) * 100`

- **Branch Coverage**  
  `= (branches executadas / total de branches) * 100`

- **Function Coverage**  
  Verifica se todas as funções foram chamadas pelo menos uma vez.

*Nota:* Nem sempre é necessário atingir 100% de cobertura de código. Métodos triviais como `getters`, `setters`, `equals`, e `hashCode` nem sempre precisam ser testados. A alta cobertura de código **não garante ausência de bugs**.

### Cobertura de Testes
Refere-se ao **quanto das funcionalidades ou requisitos estão cobertos por testes**. Foca mais na lógica do sistema e em como os testes abordam diferentes fluxos de execução.

---

## Sobre o JaCoCo

**JaCoCo** (Java Code Coverage) é uma das ferramentas mais populares e gratuitas para medir cobertura de código em projetos Java. Ele fornece relatórios detalhados em HTML, XML e outros formatos, mostrando:

- Quais linhas foram executadas;
- Quais métodos ou classes não foram cobertos por testes;
- Cobertura de branches (if/else, switch, etc.).

Alternativas incluem o **SonarQube**, que é uma ferramenta paga com recursos mais avançados, mas o JaCoCo atende muito bem a projetos que desejam melhorar a qualidade do código com foco em testes.

---

## Diagrama UML

![Image](https://github.com/user-attachments/assets/294112bb-82ec-4fef-81e3-0f1f97afa8a0)

---

## Tecnologias

- Java
- Spring Boot
- JUnit
- Mockito
- JaCoCo

---

## Resultados de Cobertura

> **Cobertura total de código com JaCoCo:** 100%  
> **Total de testes implementados:** 15

---