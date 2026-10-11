package br.com.ludgerios.javapoo.app;

import static br.com.ludgerios.javapoo.app.SimuladorConsole.executar;
import static br.com.ludgerios.javapoo.app.SimuladorConsole.ocorrencias;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AplicacaoTest {
    @Test
    void encerraComOpcaoDoMenuPrincipal() {
        String saida = executar("3\n");

        assertTrue(saida.contains("Aplicação encerrada."));
        assertFalse(saida.contains("Entrada encerrada."));
    }

    @Test
    void retornaDosDoisSubmenusAoMenuPrincipal() {
        String saida = executar("1\n5\n2\n5\n3\n");

        assertTrue(saida.contains("MENU BANCÁRIO"));
        assertTrue(saida.contains("MENU DE ESTOQUE"));
        assertEquals(2, ocorrencias(saida, "Retornando ao menu principal."));
        assertTrue(saida.contains("Aplicação encerrada."));
    }

    @Test
    void recuperaMenuPrincipalAposLetrasVazioOverflowEOpcoesInvalidas() {
        String saida = executar("texto\n\n2147483648\n99\n-1\n3\n");

        assertEquals(3, ocorrencias(saida, "Entrada inválida."));
        assertEquals(2, ocorrencias(saida, "Opção inválida."));
        assertTrue(saida.contains("Aplicação encerrada."));
    }

    @Test
    void recuperaAmbosSubmenusAposOpcoesInvalidasEEntradasNaoNumericas() {
        String saida = executar("1\n0\nx\n6\n5\n2\n0\n?\n6\n5\n3\n");

        assertEquals(2, ocorrencias(saida, "Entrada inválida."));
        assertEquals(4, ocorrencias(saida, "Opção inválida."));
        assertTrue(saida.contains("Aplicação encerrada."));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "", "1\n", "2\n", "1\n1\n", "1\n1\n10\n", "1\n1\n10\nBreno\n",
        "2\n1\n", "2\n1\n10\n", "2\n1\n10\nCafé\n"
    })
    void encerraNormalmenteComEOFAntesOuDuranteCadastro(String entrada) {
        String saida = executar(entrada);

        assertTrue(saida.contains("Entrada encerrada. Aplicação finalizada."));
        assertFalse(saida.contains("Conta criada com sucesso!"));
        assertFalse(saida.contains("Produto cadastrado com sucesso!"));
    }

    @Test
    void mantemScannerCompartilhadoAbertoAposRetornoEEncerramento() {
        Scanner entrada = new Scanner("1\n5\n3\nlinha restante\n");
        StringWriter saida = new StringWriter();
        new Aplicacao(entrada, new PrintWriter(saida)).executar();

        assertEquals("linha restante", entrada.nextLine());
        assertTrue(saida.toString().contains("Aplicação encerrada."));
    }
}
