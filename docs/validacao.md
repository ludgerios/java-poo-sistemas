# Registro de validação

Validação local realizada em **11/10/2026**, no Windows, com JDK Temurin **17.0.20.1+1**, Maven **3.10.0** e JUnit **5.14.4**. O código é compilado para Java 17.

## Build e testes

Comando principal executado:

```text
mvn --batch-mode --no-transfer-progress clean verify
```

Resultado: **BUILD SUCCESS**. As 150 execuções de testes passaram, sem falhas, erros ou testes ignorados. O total inclui os casos de testes parametrizados.

| Classe de teste | Casos executados | Cenários principais |
| --- | ---: | --- |
| `ContaTest` | 47 | Criação, identificador/titular, saldo inicial, depósitos, saques, centavos e estado após rejeição. |
| `ProdutoTest` | 33 | Cadastro, códigos/nomes, vendas, reposições, limites de inteiro e integridade do estoque. |
| `LeitorConsoleTest` | 30 | Linhas, conversão, campos vazios, faixas, formatos monetários, novas tentativas e EOF. |
| `AplicacaoTest` | 14 | Menus principal/submenus, opções inválidas, retorno, encerramento e EOF durante cadastro. |
| `MenuBancarioTest` | 13 | Fluxo bancário, apresentação BRL, persistência na sessão, erros sem sucesso indevido e EOF. |
| `MenuEstoqueTest` | 13 | Cadastro/venda/reposição, estado na sessão, operações inválidas, overflow e EOF. |
| **Total** | **150** | **0 falhas, 0 erros, 0 ignorados** |

Os relatórios são gerados em `target/surefire-reports/`. O JAR executável gerado é `target/java-poo-sistemas-1.0.0.jar`; esses arquivos são resultados locais de build e estão ignorados pelo Git.

O Maven Wrapper oficial usa a distribuição 3.10.0 com verificação de SHA-256. `mvnw.cmd` foi validado no PowerShell e `mvnw` no Git Bash. O Enforcer também foi verificado: Java 8 é rejeitado com uma mensagem de que o projeto exige JDK 17 ou superior.

## Execução do JAR

Foram executados cinco fluxos do JAR com entradas reais fornecidas ao processo:

1. Banco: cadastro, depósito, saque, consulta, retorno e encerramento; saldo final R$ 1.010,00.
2. Estoque: cadastro, venda, reposição, consulta, retorno e encerramento; quantidade final 12.
3. Erros bancários: letras e opção inexistente, depósito negativo e saldo insuficiente; saldo permaneceu R$ 10,00.
4. Erros de estoque: venda negativa, reposição negativa e venda acima do disponível; quantidade permaneceu 10.
5. EOF durante um cadastro: encerramento normal sem anunciar criação de conta.

Todas as execuções retornaram código **0**, com saída de erro vazia. As operações rejeitadas não emitiram mensagens de sucesso. Os trechos de saída estão em [demonstracao.md](demonstracao.md).

## Repositório e integração contínua

A versão final preserva os quatro arquivos históricos somente em `docs/originais`. O build compila sete classes de produção em `src/main/java`, e os modelos não têm setters públicos ou impressão no console.

O workflow [CI Java](https://github.com/ludgerios/java-poo-sistemas/actions/workflows/ci.yml) executa `mvn --batch-mode --no-transfer-progress clean verify` em **Ubuntu e Windows**, com Temurin 17, a cada push e pull request. As ações estão fixadas por SHA, e o workflow possui somente `contents: read`. Os resultados de cada execução estão disponíveis no GitHub Actions.

Não são versionados `target`, arquivos `.class`, logs, configurações locais de IDE ou credenciais. Não foi adicionada licença ao repositório.

## Limitações mantidas

Aplicação educacional de console, com uma conta e um produto por sessão. Os dados permanecem em memória. Não há autenticação, armazenamento persistente, múltiplos cadastros ou regras de instituições financeiras reais.
