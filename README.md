# Java POO — Sistema Bancário e Gestão de Estoque

Dois sistemas de console para praticar Java e Programação Orientada a Objetos: uma conta bancária e o controle de estoque de um produto. O projeto nasceu como exercício acadêmico e foi revisado para separar responsabilidades, corrigir regras de negócio e validar o comportamento com testes automatizados.

A aplicação usa Java padrão. Os dados ficam **somente em memória** e são descartados quando o programa encerra. O sistema bancário é uma simulação educacional.

## Funcionalidades

### Sistema bancário

- Cadastro de uma conta com número, titular e saldo inicial.
- Consulta dos dados e do saldo em reais.
- Depósitos e saques com valores positivos e validação de saldo disponível.
- Valores exatos em centavos com `BigDecimal`, sem cálculos com `double`.

### Gestão de estoque

- Cadastro de um produto com código, nome e quantidade inicial.
- Consulta, venda e reposição de unidades inteiras.
- Rejeição de quantidades inválidas e de vendas acima do estoque.
- Proteção contra reposição acima do limite de `int`.

O menu inicial permite escolher um sistema ou encerrar. Cada submenu retorna ao principal pela opção **5**, preservando seu cadastro durante a sessão. Um novo cadastro no mesmo sistema é bloqueado para evitar substituir dados silenciosamente.

## Tecnologias

- **Java 17** como versão de compilação; execução com JDK 17 ou superior.
- **Maven** para compilação, testes e empacotamento.
- **JUnit 5** para testes dos modelos, do console e da aplicação.
- **GitHub Actions** para executar `mvn clean verify` em Windows e Linux.

As versões das dependências e dos plugins estão fixadas no [pom.xml](pom.xml). O Maven Wrapper permite usar a versão configurada sem instalar Maven globalmente.

## Estrutura do projeto

```text
java-poo-sistemas/
├── .github/workflows/ci.yml
├── .mvn/
│   ├── jvm.config
│   └── wrapper/maven-wrapper.properties
├── docs/
│   ├── diagnostico.md
│   ├── demonstracao.md
│   ├── validacao.md
│   └── originais/
│       ├── ContaMain.java
│       ├── Estoque.java
│       ├── Produto.java
│       └── module-info.java
├── src/
│   ├── main/java/br/com/ludgerios/javapoo/
│   │   ├── app/
│   │   │   ├── Aplicacao.java
│   │   │   ├── MenuBancario.java
│   │   │   └── MenuEstoque.java
│   │   ├── banco/Conta.java
│   │   ├── estoque/Produto.java
│   │   └── console/
│   │       ├── LeitorConsole.java
│   │       └── FimDaEntradaException.java
│   └── test/java/br/com/ludgerios/javapoo/
│       ├── app/
│       │   ├── AplicacaoTest.java
│       │   ├── MenuBancarioTest.java
│       │   ├── MenuEstoqueTest.java
│       │   └── SimuladorConsole.java
│       ├── banco/ContaTest.java
│       ├── estoque/ProdutoTest.java
│       └── console/LeitorConsoleTest.java
├── .gitattributes
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

`Conta` e `Produto` concentram as regras e não imprimem mensagens no console. Os menus apresentam os dados e os resultados das operações. `LeitorConsole` lê linhas e trata erros de conversão, campos vazios e valores fora da faixa permitida. `Aplicacao` coordena a seleção dos sistemas e o encerramento.

Os [quatro arquivos originais](docs/originais) foram preservados para comparação e não são compilados pelo Maven. O [diagnóstico técnico](docs/diagnostico.md) relaciona os problemas e suas correções. A versão executável usa o classpath convencional, pois o módulo JPMS vazio não acrescentava valor ao escopo do projeto.

## Como executar

### Pré-requisitos

- JDK 17 ou superior, com `JAVA_HOME` apontando para o JDK e seu diretório `bin` no `PATH`.
- Git para clonar o repositório.
- Conexão à internet na primeira compilação para baixar Maven e as dependências.

Confira o ambiente com `java -version` e `javac -version`. Ambos devem indicar versão 17 ou superior. O Wrapper instala Maven, mas requer um JDK já disponível.

### Windows — PowerShell

```powershell
git clone https://github.com/ludgerios/java-poo-sistemas.git
Set-Location java-poo-sistemas
.\mvnw.cmd clean verify
java -jar target/java-poo-sistemas-1.0.0.jar
```

Para abrir a pasta no VS Code, execute `code .` nesse diretório. Os mesmos comandos de compilação e execução funcionam no terminal integrado.

### Linux — Bash

```bash
git clone https://github.com/ludgerios/java-poo-sistemas.git
cd java-poo-sistemas
./mvnw clean verify
java -jar target/java-poo-sistemas-1.0.0.jar
```

### Maven instalado

O comando principal de validação é:

```text
mvn clean verify
```

Ele limpa os resultados anteriores, compila o código, executa os testes e gera o JAR executável. Para executar apenas os testes, use `mvn test`, `.\mvnw.cmd test` no Windows ou `./mvnw test` no Linux.

## Regras e entradas

- Número da conta e código do produto: inteiros de **1 a 2.147.483.647**.
- Titular e nome do produto: texto não vazio; espaços nas extremidades são removidos.
- Saldo inicial e quantidade inicial: zero ou positivos.
- Depósitos, saques, vendas e reposições: estritamente positivos.
- Dinheiro no console: `1000`, `1000,50` ou `1000.50`, com até duas casas decimais e **sem separador de milhar**. Notação científica e valores como `NaN` não são aceitos.
- Saques acima do saldo, vendas acima do estoque e reposições que ultrapassem o limite são rejeitados sem modificar os dados.

Valores monetários são apresentados no formato brasileiro. Frações de centavo não são arredondadas: são rejeitadas. Na API do modelo, zeros decimais adicionais podem ser normalizados, como `1.230` para `1.23`.

Entradas inválidas exibem uma mensagem e permitem continuar. O fim da entrada, como Ctrl+D em Bash ou Ctrl+Z seguido de Enter em terminais Windows compatíveis, encerra a aplicação corretamente. O leitor é compartilhado e os submenus não fecham `System.in`.

## Demonstração

Os [exemplos de interação](docs/demonstracao.md) foram obtidos executando o JAR, com cadastro, movimentação bancária, venda e reposição. Incluem os valores digitados e a saída real da aplicação.

## Qualidade e testes

Os testes dos modelos verificam criação, operações válidas e inválidas, precisão dos centavos, limites e integridade após rejeições. Os testes do console verificam conversões e novas tentativas. Os testes da aplicação usam entradas simuladas para validar menus, retorno ao principal, persistência durante a sessão, erros e encerramento.

Execute `mvn clean verify` ou o equivalente pelo Wrapper. Os relatórios detalhados são gerados em `target/surefire-reports/`, fora do versionamento. O [registro de validação](docs/validacao.md) apresenta os resultados verificados.

A [integração contínua](https://github.com/ludgerios/java-poo-sistemas/actions/workflows/ci.yml) roda em push e pull request e falha se a compilação ou algum teste falhar. As ações estão fixadas por SHA e têm apenas permissão de leitura do repositório.

## Escopo

O projeto mantém uma conta e um produto por sessão, sem persistência, autenticação, banco de dados ou interface web. Os identificadores são positivos, sem regras de dígito verificador bancário. Não há taxas, juros, transferências ou operações concorrentes.

Evoluções possíveis são salvar dados em arquivo e permitir múltiplos cadastros, mantendo as validações e a interface simples.

## Autor

**Breno Ludgerio** — [GitHub: ludgerios](https://github.com/ludgerios)
