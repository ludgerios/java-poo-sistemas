# Diagnóstico dos códigos originais

Análise realizada antes da refatoração dos quatro arquivos de `ProjetoEstoque/src`.
As cópias em `docs/originais` preservam exatamente o material recebido; são referências históricas e não entram no build Maven.

| Arquivo | Problema observado | Correção aplicada |
| --- | --- | --- |
| `ContaMain.java` | `opcao` não recebe entrada e é usada sem inicialização; a classe não compila. | Leitura controlada da opção em cada iteração. |
| `ContaMain.java` | `Conta` interna reúne dados, regras e impressão; construtor vazio e setters permitem estados inválidos. | Extrair o modelo, validar a criação e remover setters; manter a apresentação nos menus. |
| `ContaMain.java` | `double` perde precisão decimal; valores iniciais negativos, NaN e infinito não são tratados adequadamente. | Usar `BigDecimal` com duas casas e validação explícita; rejeitar formatos inválidos. |
| `ContaMain.java` | Depósito inválido imprime erro no modelo e sucesso no menu. Saques diferentes recebem um erro genérico. | Rejeitar operações com mensagens específicas e só informar sucesso após concluir a operação. |
| `ContaMain.java` | Identificador não positivo e titular vazio são aceitos. | Exigir número positivo e titular não vazio. |
| `Estoque.java` | `nextLine()` é chamado depois de `close()`, causando `IllegalStateException` no encerramento. | Um leitor compartilhado; não fechar `System.in` ao retornar de um submenu. |
| `Estoque.java` | `nextInt()` lança `InputMismatchException` com letras ou números fora de `int`. | Ler linhas, converter com tratamento de `NumberFormatException` e permitir nova tentativa. |
| `Estoque.java` | Reposição imprime sucesso mesmo para valores inválidos; recadastro substitui silenciosamente o produto. | Sucesso apenas após validação; impedir substituição acidental de cadastro. |
| `Produto.java` | Construtor aceita código inválido, nome vazio e quantidade negativa. | Validar todos os campos e manter código e nome imutáveis. |
| `Produto.java` | Venda negativa aumenta o estoque; venda zero é aceita. | Exigir quantidade estritamente positiva e estoque suficiente. |
| `Produto.java` | Reposição negativa diminui o estoque; zero é aceito; soma pode ultrapassar `Integer.MAX_VALUE`. | Validar quantidade e limite antes de alterar o estoque. |
| Todos os menus | Entrada e lógica estão misturadas; campos vazios e fim da entrada não são tratados; não há menu unificado nem testes. | Separar console, modelos e aplicação; encerrar corretamente em EOF; testar fluxos simulados. |
| `module-info.java` | Módulo vazio, sem exportações ou dependências, não agrega isolamento útil a essas aplicações pequenas. | Remover JPMS da versão executável e usar a estrutura convencional do Maven. |

O fluxo original de cadastro, consulta, saque/depósito e venda/reposição foi mantido. A aplicação continua educacional, de console e com dados em memória.

## Verificação executada nos originais

As cópias históricas foram compiladas com JDK 17 usando `javac --release 17 -encoding UTF-8`, em diretório temporário externo ao repositório.

- Os quatro arquivos juntos falharam: `ContaMain.java:91: error: variable opcao might not have been initialized`.
- `Estoque.java` e `Produto.java` compilaram isoladamente. A opção `5` produziu `IllegalStateException: Scanner closed` em `Estoque.java:66`.
- A entrada `abc` no menu de estoque produziu `InputMismatchException` em `Estoque.java:20`.
- Com quantidade inicial `10`, vender `-3` mudou o estoque para `13`; adicionar `-20` depois mudou para `-7` e exibiu sucesso.

## Decisões da revisão

Os pacotes e a nomenclatura estão em português: `app`, `banco`, `estoque` e `console`. Os modelos preservam as operações originais, com exceções específicas de validação representadas por `IllegalArgumentException`; os menus exibem suas mensagens e só anunciam sucesso após concluir a operação.

Número de conta e código de produto são inteiros positivos. Titular e nome são imutáveis, assim como os identificadores. Saldo e quantidade só mudam por operações válidas, sem setters. A reposição verifica o limite antes de somar, impedindo overflow.

`BigDecimal` usa escala 2 com `RoundingMode.UNNECESSARY`: frações de centavo são rejeitadas e zeros extras são normalizados. A entrada textual do console permite até duas casas com ponto ou vírgula e sem separadores de milhar, evitando a ambiguidade de `1.000`.

Um `Scanner` compartilhado lê linhas completas. Erros de formato permitem nova tentativa; violações de negócio retornam ao submenu com erro específico. EOF encerra a aplicação pelo fluxo principal. Os submenus não fecham a entrada recebida.

Foi mantido um cadastro de conta e um de produto por execução. Ambos persistem ao retornar ao menu principal; um recadastro é bloqueado para preservar o estado. Não foram adicionados persistência, múltiplos cadastros, frameworks ou banco de dados.

A classe `Conta` deixou de ser interna, e a apresentação saiu dos modelos. O módulo JPMS vazio e as configurações Eclipse não participam da versão executável; o Maven compila somente `src/main/java` e testa `src/test/java`. O resultado final é registrado em [validacao.md](validacao.md).
