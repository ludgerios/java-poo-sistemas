package br.com.ludgerios.javapoo.console;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class LeitorConsoleTest {
    private final StringWriter saida = new StringWriter();

    @Test
    void leInteiroETextoEmLinhasSeparadas() {
        LeitorConsole leitor = leitor(" 42 \n Breno Ludgerio \n");

        assertEquals(42, leitor.lerInteiro("Número: "));
        assertEquals("Breno Ludgerio", leitor.lerTexto("Nome: "));
    }

    @Test
    void repeteEntradaInteiraComLetrasVazioEOverflow() {
        LeitorConsole leitor = leitor("abc\n\n2147483648\n-2147483649\n7\n");

        assertEquals(7, leitor.lerInteiro("Número: "));
        assertEquals(4, ocorrencias("Entrada inválida."));
    }

    @Test
    void repeteInteiroForaDaFaixaESoConsomeCampoAtual() {
        LeitorConsole leitor = leitor("-1\n0\n100\n25\nNome\n");

        assertEquals(25, leitor.lerInteiro("Número: ", 1, 50));
        assertEquals(3, ocorrencias("Valor fora do intervalo."));
        assertEquals("Nome", leitor.lerTexto("Nome: "));
    }

    @Test
    void informaOpcoesInexistentesEPermiteNovaTentativa() {
        assertEquals(3, leitor("0\n6\n3\n").lerOpcao("Opção: ", 3));
        assertEquals(2, ocorrencias("Opção inválida."));
    }

    @Test
    void repeteTextoVazioERemoveEspacosExternos() {
        assertEquals("Café", leitor(" \n\t\n Café \n").lerTexto("Nome: "));
        assertEquals(2, ocorrencias("O texto não pode ficar vazio."));
    }

    @ParameterizedTest
    @CsvSource({
        "0, 0.00",
        "'10,50', 10.50",
        "10.5, 10.50",
        "'+25,10', 25.10",
        "-1.25, -1.25",
        "'1000,50', 1000.50",
        "1000.50, 1000.50"
    })
    void converteDinheiroComVirgulaOuPontoSemPontoFlutuante(String texto, String esperado) {
        assertEquals(new BigDecimal(esperado), leitor(texto + "\n").lerDinheiro("Valor: "));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "", "abc", "NaN", "Infinity", "1e3", "1.000,50", "1,000.50",
        "1.000.50", "1,000,50", "1.234", "1.000", "1,000", "R$ 10", "12,34.56", ".50", "10,"
    })
    void rejeitaFormatoMonetarioInvalidoAntesDeConverter(String texto) {
        assertEquals(new BigDecimal("10.50"), leitor(texto + "\n10,50\n").lerDinheiro("Valor: "));
        assertEquals(1, ocorrencias("Valor monetário inválido."));
    }

    @Test
    void fimDaEntradaNaoEntraEmLoopDeNovasTentativas() {
        assertThrows(FimDaEntradaException.class, () -> leitor("abc\n").lerInteiro("Número: "));
        assertEquals(1, ocorrencias("Entrada inválida."));
    }

    @Test
    void naoFechaScannerCompartilhado() {
        Scanner entrada = new Scanner("3\nlinha restante\n");
        LeitorConsole leitor = new LeitorConsole(entrada, new PrintWriter(saida));

        assertEquals(3, leitor.lerOpcao("Opção: ", 3));
        assertEquals("linha restante", entrada.nextLine());
        assertTrue(saida.toString().contains("Opção: "));
    }

    private LeitorConsole leitor(String entrada) {
        return new LeitorConsole(new Scanner(entrada), new PrintWriter(saida));
    }

    private long ocorrencias(String texto) {
        return saida.toString().lines().filter(linha -> linha.contains(texto)).count();
    }
}
