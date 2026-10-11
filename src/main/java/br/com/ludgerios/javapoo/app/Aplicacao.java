package br.com.ludgerios.javapoo.app;

import br.com.ludgerios.javapoo.console.LeitorConsole;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

public final class Aplicacao {
    private final LeitorConsole console;
    private final MenuBancario menuBancario;
    private final MenuEstoque menuEstoque;

    public Aplicacao(Reader entrada, Writer saida) {
        console = new LeitorConsole(new Scanner(entrada), new PrintWriter(saida, true));
        menuBancario = new MenuBancario(console);
        menuEstoque = new MenuEstoque(console);
    }

    public static void main(String[] argumentos) {
        Reader entrada = new InputStreamReader(System.in, StandardCharsets.UTF_8);
        Writer saida = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
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
                }
            }
        } catch (NoSuchElementException fimDaEntrada) {
            console.escreverLinha("\nEntrada encerrada. Aplicação finalizada.");
        }
    }
}
