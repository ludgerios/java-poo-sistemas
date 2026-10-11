package br.com.ludgerios.javapoo.app;

import static br.com.ludgerios.javapoo.app.SimuladorConsole.executar;
import static br.com.ludgerios.javapoo.app.SimuladorConsole.ocorrencias;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class MenuEstoqueTest {
    @Test
    void cadastraVendeRepoeEConsultaEstoque() {
        String saida = executar("2\n1\n10\nCafé\n10\n3\n3\n4\n5\n2\n5\n3\n");

        assertTrue(saida.contains("Produto cadastrado com sucesso!"));
        assertTrue(saida.contains("Venda realizada com sucesso!"));
        assertTrue(saida.contains("Estoque atualizado com sucesso!"));
        assertTrue(saida.contains("Código: 10"));
        assertTrue(saida.contains("Produto: Café"));
        assertTrue(saida.contains("Quantidade: 12"));
        assertTrue(saida.contains("Aplicação encerrada."));
    }

    @ParameterizedTest
    @CsvSource({
        "3, 0, Quantidade da venda deve ser maior que zero.",
        "3, -1, Quantidade da venda deve ser maior que zero.",
        "3, 11, Estoque insuficiente para venda.",
        "4, 0, Quantidade da reposição deve ser maior que zero.",
        "4, -1, Quantidade da reposição deve ser maior que zero."
    })
    void rejeitaOperacaoSemSucessoFalsoESemAlterarEstoque(String opcao, String quantidade, String erro) {
        String saida = executar("2\n1\n10\nCafé\n10\n" + opcao + "\n" + quantidade + "\n2\n5\n3\n");

        assertTrue(saida.contains("Erro: " + erro));
        assertEquals(2, ocorrencias(saida, "Quantidade: 10"));
        assertFalse(saida.contains("Venda realizada com sucesso!"));
        assertFalse(saida.contains("Estoque atualizado com sucesso!"));
    }

    @Test
    void rejeitaReposicaoQueExcedeLimiteDeInteiro() {
        String saida = executar("2\n1\n10\nCafé\n2147483647\n4\n1\n2\n5\n3\n");

        assertTrue(saida.contains("Erro: Reposição excede o limite de estoque."));
        assertEquals(2, ocorrencias(saida, "Quantidade: 2147483647"));
        assertFalse(saida.contains("Estoque atualizado com sucesso!"));
    }

    @Test
    void repeteCodigoNomeEQuantidadeInicialInvalidos() {
        String saida = executar("2\n1\n0\nx\n2147483648\n10\n \nCafé\n-1\nletras\n2147483648\n10\n2\n5\n3\n");

        assertEquals(5, ocorrencias(saida, "Entrada inválida."));
        assertEquals(2, ocorrencias(saida, "Valor fora do intervalo."));
        assertTrue(saida.contains("Produto: Café"));
        assertEquals(2, ocorrencias(saida, "Quantidade: 10"));
        assertTrue(saida.contains("Aplicação encerrada."));
    }

    @Test
    void preservaProdutoAoRetornarEImpedeSubstituicaoSilenciosa() {
        String saida = executar("2\n1\n10\nCafé\n10\n5\n2\n1\n3\n2\n5\n1\n5\n2\n2\n5\n3\n");

        assertEquals(1, ocorrencias(saida, "Produto cadastrado com sucesso!"));
        assertTrue(saida.contains("Já existe um produto cadastrado. O cadastro atual foi preservado."));
        assertEquals(2, ocorrencias(saida, "Produto: Café"));
        assertTrue(saida.contains("Quantidade: 8"));
        assertTrue(saida.contains("Aplicação encerrada."));
    }

    @Test
    void informaAusenciaDeProdutoAntesDeConsultaVendaEReposicao() {
        String saida = executar("2\n2\n3\n4\n5\n3\n");

        assertEquals(3, ocorrencias(saida, "Nenhum produto cadastrado."));
        assertFalse(saida.contains("Quantidade a vender:"));
        assertFalse(saida.contains("Quantidade a adicionar:"));
        assertFalse(saida.contains("com sucesso!"));
    }

    @Test
    void recuperaLetrasEOverflowDuranteVendaEReposicao() {
        String saida = executar("2\n1\n10\nCafé\n10\n3\nx\n2147483648\n3\n4\n\n-2147483649\n5\n2\n5\n3\n");

        assertEquals(4, ocorrencias(saida, "Entrada inválida."));
        assertTrue(saida.contains("Quantidade: 12"));
        assertEquals(1, ocorrencias(saida, "Venda realizada com sucesso!"));
        assertEquals(1, ocorrencias(saida, "Estoque atualizado com sucesso!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"3", "4"})
    void encerraEmEOFDuranteOperacaoSemInformarSucesso(String opcao) {
        String saida = executar("2\n1\n10\nCafé\n10\n" + opcao + "\n");

        assertTrue(saida.contains("Entrada encerrada. Aplicação finalizada."));
        assertFalse(saida.contains("Venda realizada com sucesso!"));
        assertFalse(saida.contains("Estoque atualizado com sucesso!"));
    }
}
