# Contas a Pagar API

Esta é uma API REST para um sistema simples de contas a pagar. O sistema permite realizar o CRUD de uma conta a pagar, alterar a situação dela quando for efetuado pagamento, obter informações sobre as contas cadastradas no banco de dados, e importar um lote de contas de um arquivo CSV.

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- PostgreSQL
- Docker
- Docker Compose
- JUnit (para testes)
- Maven

## Funcionalidades

- Cadastrar conta
- Atualizar conta
- Alterar a situação da conta
- Obter a lista de contas a pagar com filtro de data de vencimento e descrição
- Obter conta por ID
- Obter valor total pago por período
- Importar contas a pagar via arquivo CSV

## Segurança

A API está protegida por Basic Auth. As credenciais padrão são:

- **Usuário:** isaias
- **Senha:** 123

## Pré-requisitos

- Docker e Docker Compose instalados
- JDK 17
- Maven

## Configuração e Execução

### Clonando o Repositório

```bash
git clone https://github.com/seu-usuario/contas-a-pagar.git
cd contas-a-pagar
