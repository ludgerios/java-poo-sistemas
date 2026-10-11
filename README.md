# Sistema bancário e controle de estoque

Projeto de estudos de Java e Programação Orientada a Objetos com duas aplicações de console.

## Como funciona

O menu inicial permite escolher entre o sistema bancário e o estoque.

- **Banco:** cadastrar uma conta, consultar o saldo, depositar e sacar dinheiro.
- **Estoque:** cadastrar um produto, consultar a quantidade, registrar vendas e repor unidades.

Cada sistema mantém um cadastro durante a execução. A opção `5` volta ao menu inicial, e a opção `3` do menu inicial encerra o programa. Os dados ficam em memória e são perdidos ao sair.

O saldo usa `BigDecimal`. Depósitos, saques, vendas e reposições precisam ser positivos. O programa também verifica saldo e estoque disponíveis e permite tentar novamente após entradas inválidas.

Para valores em dinheiro, use ponto ou vírgula, com até duas casas decimais e sem separador de milhar: `1000,50` ou `1000.50`.

## Executar

É necessário ter **JDK 17 ou superior** e **Maven** instalados.

```bash
git clone https://github.com/ludgerios/java-poo-sistemas.git
cd java-poo-sistemas
mvn clean verify
java -jar target/java-poo-sistemas-1.0.0.jar
```

Os mesmos comandos funcionam no PowerShell e no terminal integrado do VS Code. Para abrir o projeto no VS Code, execute `code .` dentro da pasta.

## Código

As classes estão em `src/main/java/br/com/ludgerios/javapoo`:

- `app`: inicialização e menus.
- `banco`: classe `Conta` e operações bancárias.
- `estoque`: classe `Produto` e movimentações de estoque.
- `console`: leitura e conversão das entradas.

Os testes usam JUnit 5 e ficam em `src/test/java`. Para executá-los separadamente:

```bash
mvn test
```

## Autor

[Breno Ludgerio](https://github.com/ludgerios)
