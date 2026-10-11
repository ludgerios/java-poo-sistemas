package br.com.ludgerios.javapoo.app;

import br.com.ludgerios.javapoo.banco.Conta;
import br.com.ludgerios.javapoo.console.LeitorConsole;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public final class MenuBancario {
    private final LeitorConsole console;
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
    private Conta conta;

    public MenuBancario(LeitorConsole console) {
        this.console = Objects.requireNonNull(console, "Console não pode ser nulo.");
    }

    public void executar() {
        while (true) {
            console.escreverLinha("\n===== MENU BANCÁRIO =====");
            console.escreverLinha("1 - Cadastrar conta");
            console.escreverLinha("2 - Consultar conta e saldo");
            console.escreverLinha("3 - Sacar dinheiro");
            console.escreverLinha("4 - Depositar dinheiro");
            console.escreverLinha("5 - Retornar ao menu principal");
            int opcao = console.lerOpcao("Escolha uma opção: ", 5);
            try {
                switch (opcao) {
                    case 1 -> cadastrarConta();
                    case 2 -> mostrarConta();
                    case 3 -> sacar();
                    case 4 -> depositar();
                    case 5 -> {
                        console.escreverLinha("Retornando ao menu principal.");
                        return;
                    }
                    default -> throw new IllegalStateException("Opção não validada pelo leitor.");
                }
            } catch (IllegalArgumentException erro) {
                console.escreverLinha("Erro: " + erro.getMessage());
            }
        }
    }

    private void cadastrarConta() {
        if (conta != null) {
            console.escreverLinha("Já existe uma conta cadastrada. O cadastro atual foi preservado.");
            return;
        }
        int numero = console.lerInteiro("Número da conta: ", 1, Integer.MAX_VALUE);
        String titular = console.lerTexto("Nome do titular: ");
        BigDecimal saldoInicial = console.lerDinheiro("Saldo inicial: ");
        conta = new Conta(numero, titular, saldoInicial);
        console.escreverLinha("Conta criada com sucesso!");
    }

    private void mostrarConta() {
        if (temConta()) {
            console.escreverLinha("Conta Nº: " + conta.getNumeroConta());
            console.escreverLinha("Titular: " + conta.getTitular());
            console.escreverLinha("Saldo: " + moeda.format(conta.getSaldo()));
        }
    }

    private void sacar() {
        if (temConta()) {
            conta.sacarDinheiro(console.lerDinheiro("Valor para saque: "));
            console.escreverLinha("Saque realizado com sucesso!");
        }
    }

    private void depositar() {
        if (temConta()) {
            conta.depositarDinheiro(console.lerDinheiro("Valor para depósito: "));
            console.escreverLinha("Depósito realizado com sucesso!");
        }
    }

    private boolean temConta() {
        if (conta == null) {
            console.escreverLinha("Nenhuma conta cadastrada.");
            return false;
        }
        return true;
    }
}
