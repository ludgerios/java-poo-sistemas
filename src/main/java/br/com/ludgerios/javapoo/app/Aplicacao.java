package br.com.ludgerios.javapoo.app;

import br.com.ludgerios.javapoo.console.FimDaEntradaException;
import br.com.ludgerios.javapoo.console.LeitorConsole;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public final class Aplicacao {
    private final LeitorConsole console;
    private final MenuBancario menuBancario;
    private final MenuEstoque menuEstoque;

    public Aplicacao(Scanner entrada, PrintWriter saida) {
        console = new LeitorConsole(entrada, saida);
        menuBancario = new MenuBancario(console);
        menuEstoque = new MenuEstoque(console);
    }

    public Aplicacao(Reader entrada, Writer saida) {
        this(new Scanner(entrada), new PrintWriter(saida, true));
    }

    public static void main(String[] argumentos) {
        Scanner entrada = new Scanner(System.in, StandardCharsets.UTF_8);
        PrintWriter saida = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
        new Aplicacao(entrada, saida).executar();
    }

    public void executar() {
        try {
            while (true) {
                console.escreverLinha("\n===== JAVA POO — SISTEMAS =====");
                console.escreverLinha("1 - Sistema bancário");
                console.escreverLinha("2 - Sistema de estoque");
                console.escreverLinha("3 - Encerrar aplicação");
                int opcao = console.lerOpcao("Escolha uma opção: ", 3);
                switch (opcao) {
                    case 1 -> menuBancario.executar();
                    case 2 -> menuEstoque.executar();
                    case 3 -> {
                        console.escreverLinha("Aplicação encerrada.");
                        return;
                    }
                    default -> throw new IllegalStateException("Opção não validada pelo leitor.");
                }
            }
        } catch (FimDaEntradaException fimDaEntrada) {
            console.escreverLinha("\nEntrada encerrada. Aplicação finalizada.");
        }
    }
}
