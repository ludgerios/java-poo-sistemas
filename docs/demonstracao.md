# Demonstração real no terminal

Executado em 11/10/2026 com JDK 17 e o JAR gerado por `mvn clean verify`:

```text
java -jar target/java-poo-sistemas-1.0.0.jar
```

As entradas abaixo foram fornecidas ao processo em linhas. A saída foi capturada diretamente do JAR; os trechos são recortes da saída real e não incluem o eco dos valores digitados. Todas as execuções terminaram com código 0 e sem saída de erro.

## Conta bancária

Sequência: selecionar banco → cadastrar conta `101`, titular `Breno Ludgerio`, saldo `1000,50` → depositar `10.10` → sacar `0,60` → consultar → retornar → encerrar.

Entrada:

```text
1
1
101
Breno Ludgerio
1000,50
4
10.10
3
0,60
2
5
3
```

Consulta capturada após as operações:

```text
Conta Nº: 101
Titular: Breno Ludgerio
Saldo: R$ 1.010,00
```

Saldo: R$ 1.000,50 + R$ 10,10 − R$ 0,60 = **R$ 1.010,00**.

## Estoque

Sequência: selecionar estoque → cadastrar produto `10`, nome `Café`, quantidade `10` → vender `3` → adicionar `5` → consultar → retornar → encerrar.

Entrada:

```text
2
1
10
Café
10
3
3
4
5
2
5
3
```

Consulta capturada após as operações:

```text
Código: 10
Produto: Café
Quantidade: 12
```

Quantidade: 10 − 3 + 5 = **12 unidades**.

## Erros tratados

Em uma execução bancária separada, foram informados `abc` e a opção `99` no menu; depois, uma conta com saldo `10` recebeu tentativa de depósito `-1` e saque `11`. Trechos capturados:

```text
Escolha uma opção: Entrada inválida. Digite um número inteiro entre -2147483648 e 2147483647.
Escolha uma opção: Opção inválida. Escolha uma opção entre 1 e 3.
Escolha uma opção: Valor para depósito: Erro: Valor do depósito deve ser maior que zero.
Escolha uma opção: Valor para saque: Erro: Saldo insuficiente para saque.
Saldo: R$ 10,00
```

Em outra execução, um produto com quantidade `10` recebeu tentativas de venda `-3`, reposição `-20` e venda `11`. Trechos capturados:

```text
Quantidade: 10
Escolha uma opção: Quantidade a vender: Erro: Quantidade da venda deve ser maior que zero.
Escolha uma opção: Quantidade a adicionar: Erro: Quantidade da reposição deve ser maior que zero.
Escolha uma opção: Quantidade a vender: Erro: Estoque insuficiente para venda.
Quantidade: 10
```

As operações rejeitadas não produziram mensagens de sucesso nem alteraram os dados.

## Retorno e encerramento

Ao retornar do submenu, o menu principal continuou disponível. Final da execução bancária:

```text
1 - Sistema bancário
2 - Sistema de estoque
3 - Encerrar aplicação
Escolha uma opção: Aplicação encerrada.
```

Também foi encerrada a entrada durante um cadastro incompleto. A aplicação terminou normalmente com `Entrada encerrada. Aplicação finalizada.`, sem anunciar cadastro concluído.
