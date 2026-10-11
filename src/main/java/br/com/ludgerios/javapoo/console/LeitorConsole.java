package br.com.ludgerios.javapoo.console;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class LeitorConsole {
    private static final Pattern DINHEIRO = Pattern.compile("[+-]?[0-9]+(?:[.,][0-9]{1,2})?");

    private final Scanner entrada;
    private final PrintWriter saida;

    public LeitorConsole(Scanner entrada, PrintWriter saida) {
        this.entrada = Objects.requireNonNull(entrada, "Entrada não pode ser nula.");
        this.saida = Objects.requireNonNull(saida, "Saída não pode ser nula.");
    }

    public int lerInteiro(String mensagem) {
        while (true) {
            String linha = lerLinha(mensagem);
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException erro) {
                escreverLinha("Entrada inválida. Digite um número inteiro entre "
                        + Integer.MIN_VALUE + " e " + Integer.MAX_VALUE + ".");
            }
        }
    }

    public int lerInteiro(String mensagem, int minimo, int maximo) {
        if (minimo > maximo) {
            throw new IllegalArgumentException("Intervalo de leitura inválido.");
        }
        while (true) {
            int valor = lerInteiro(mensagem);
            if (valor >= minimo && valor <= maximo) {
                return valor;
            }
            escreverLinha("Valor fora do intervalo. Informe um número entre "
                    + minimo + " e " + maximo + ".");
        }
    }

    public int lerOpcao(String mensagem, int quantidadeOpcoes) {
        if (quantidadeOpcoes < 1) {
            throw new IllegalArgumentException("O menu deve ter pelo menos uma opção.");
        }
        while (true) {
            int opcao = lerInteiro(mensagem);
            if (opcao >= 1 && opcao <= quantidadeOpcoes) {
                return opcao;
            }
            escreverLinha("Opção inválida. Escolha uma opção entre 1 e " + quantidadeOpcoes + ".");
        }
    }

    public String lerTexto(String mensagem) {
        while (true) {
            String linha = lerLinha(mensagem);
            if (!linha.isBlank()) {
                return linha;
            }
            escreverLinha("Entrada inválida. O texto não pode ficar vazio.");
        }
    }

    public BigDecimal lerDinheiro(String mensagem) {
        while (true) {
            String linha = lerLinha(mensagem);
            if (DINHEIRO.matcher(linha).matches()) {
                return new BigDecimal(linha.replace(',', '.')).setScale(2);
            }
            escreverLinha("Valor monetário inválido. Use até duas casas decimais, "
                    + "com vírgula ou ponto, sem separador de milhar.");
        }
    }

    public void escreverLinha(String mensagem) {
        saida.println(mensagem);
        saida.flush();
    }

    private String lerLinha(String mensagem) {
        saida.print(mensagem);
        saida.flush();
        return entrada.nextLine().strip();
    }
}
