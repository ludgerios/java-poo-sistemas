package br.com.ludgerios.javapoo.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AplicacaoTest {
    @Test
    void retornaDosDoisSistemasEEncerra() {
        String saida = executar("1\n5\n2\n5\n3\n");

        assertTrue(saida.contains("MENU BANCÁRIO"));
        assertTrue(saida.contains("MENU DE ESTOQUE"));
        assertEquals(2, ocorrencias(saida, "Retornando ao menu principal."));
        assertTrue(saida.contains("Aplicação encerrada."));
    }

    @ParameterizedTest
    @ValueSource(strings = {"letras", "", "99"})
    void recuperaEntradasInvalidasDoMenu(String entrada) {
        String saida = executar(entrada + "\n3\n");

        assertTrue(saida.contains(entrada.equals("99") ? "Opção inválida." : "Entrada inválida."));
        assertTrue(saida.contains("Aplicação encerrada."));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2"})
    void exigeCadastroAntesDasOperacoes(String sistema) {
        String saida = executar(sistema + "\n2\n3\n4\n5\n3\n");

        String mensagem = sistema.equals("1") ? "Nenhuma conta cadastrada." : "Nenhum produto cadastrado.";
        assertEquals(3, ocorrencias(saida, mensagem));
        assertFalse(saida.contains("com sucesso!"));
    }

    @Test
    void cadastraContaDepositaSacaEConsulta() {
        String saida = executar("1\n1\n123\nAna\n100,00\n4\n12.30\n3\n2,30\n2\n5\n3\n");

        assertTrue(saida.contains("Conta criada com sucesso!"));
        assertTrue(saida.contains("Depósito realizado com sucesso!"));
        assertTrue(saida.contains("Saque realizado com sucesso!"));
        assertTrue(saida.contains("Conta Nº: 123"));
        assertTrue(saida.contains("Titular: Ana"));
        assertTrue(saida.contains("Saldo: R$ 110,00"));
    }

    @Test
    void cadastraProdutoVendeRepoeEConsulta() {
        String saida = executar("2\n1\n10\nCafé\n10\n3\n3\n4\n5\n2\n5\n3\n");

        assertTrue(saida.contains("Produto cadastrado com sucesso!"));
        assertTrue(saida.contains("Venda realizada com sucesso!"));
        assertTrue(saida.contains("Estoque atualizado com sucesso!"));
        assertTrue(saida.contains("Produto: Café"));
        assertTrue(saida.contains("Quantidade: 12"));
    }

    @Test
    void recuperaDadosInvalidosDuranteCadastros() {
        String saida = executar("1\n1\nx\n0\n123\n\nAna\nabc\n1.234\n100\n5\n"
                + "2\n1\n2147483648\n10\n\nCafé\n-1\nx\n10\n5\n3\n");

        assertTrue(saida.contains("Entrada inválida."));
        assertTrue(saida.contains("Valor fora do intervalo."));
        assertTrue(saida.contains("Valor monetário inválido."));
        assertEquals(1, ocorrencias(saida, "Conta criada com sucesso!"));
        assertEquals(1, ocorrencias(saida, "Produto cadastrado com sucesso!"));
        assertTrue(saida.contains("Aplicação encerrada."));
    }

    @Test
    void preservaCadastrosAoRetornarEImpedeSubstituicao() {
        String saida = executar("1\n1\n123\nAna\n100\n5\n1\n1\n2\n5\n"
                + "2\n1\n10\nCafé\n10\n5\n2\n1\n2\n5\n3\n");

        assertEquals(1, ocorrencias(saida, "Conta criada com sucesso!"));
        assertEquals(1, ocorrencias(saida, "Produto cadastrado com sucesso!"));
        assertTrue(saida.contains("Já existe uma conta cadastrada."));
        assertTrue(saida.contains("Já existe um produto cadastrado."));
        assertTrue(saida.contains("Titular: Ana"));
        assertTrue(saida.contains("Quantidade: 10"));
    }

    @Test
    void informaFalhasSemAnunciarSucessoDasOperacoes() {
        String saida = executar("1\n1\n123\nAna\n100\n3\n101\n4\n0\n2\n5\n"
                + "2\n1\n10\nCafé\n10\n3\n11\n4\n-1\n2\n5\n3\n");

        assertTrue(saida.contains("Saldo insuficiente para saque."));
        assertTrue(saida.contains("Estoque insuficiente para venda."));
        assertTrue(saida.contains("Saldo: R$ 100,00"));
        assertTrue(saida.contains("Quantidade: 10"));
        assertFalse(saida.contains("Saque realizado com sucesso!"));
        assertFalse(saida.contains("Depósito realizado com sucesso!"));
        assertFalse(saida.contains("Venda realizada com sucesso!"));
        assertFalse(saida.contains("Estoque atualizado com sucesso!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1\n1\n123\nAna\n", "2\n1\n10\nCafé\n"})
    void encerraCadastroIncompletoSemAnunciarSucesso(String entrada) {
        String saida = executar(entrada);

        assertTrue(saida.contains("Entrada encerrada. Aplicação finalizada."));
        assertFalse(saida.contains("com sucesso!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1\n1\n123\nAna\n100\n3\n", "1\n1\n123\nAna\n100\n4\n",
            "2\n1\n10\nCafé\n10\n3\n", "2\n1\n10\nCafé\n10\n4\n"})
    void encerraOperacaoIncompletaSemAnunciarSucesso(String entrada) {
        String saida = executar(entrada);

        assertTrue(saida.contains("Entrada encerrada. Aplicação finalizada."));
        assertFalse(saida.contains("Saque realizado com sucesso!"));
        assertFalse(saida.contains("Depósito realizado com sucesso!"));
        assertFalse(saida.contains("Venda realizada com sucesso!"));
        assertFalse(saida.contains("Estoque atualizado com sucesso!"));
    }

    @Test
    void encerraQuandoNaoHaEntrada() {
        assertTrue(executar("").contains("Entrada encerrada. Aplicação finalizada."));
    }

    private static String executar(String entrada) {
        StringWriter saida = new StringWriter();
        new Aplicacao(new StringReader(entrada), saida).executar();
        return saida.toString().replace('\u00a0', ' ');
    }

    private static long ocorrencias(String saida, String texto) {
        return saida.lines().filter(linha -> linha.contains(texto)).count();
    }
}
