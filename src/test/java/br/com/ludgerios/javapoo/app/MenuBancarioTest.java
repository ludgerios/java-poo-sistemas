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

class MenuBancarioTest {
    @Test
    void cadastraDepositaSacaEConsultaSaldoEmReais() {
        String saida = executar("1\n1\n101\nBreno Ludgerio\n1000,50\n4\n10.10\n3\n0,60\n2\n5\n3\n");

        assertTrue(saida.contains("Conta criada com sucesso!"));
        assertTrue(saida.contains("Depósito realizado com sucesso!"));
        assertTrue(saida.contains("Saque realizado com sucesso!"));
        assertTrue(saida.contains("Conta Nº: 101"));
        assertTrue(saida.contains("Titular: Breno Ludgerio"));
        assertTrue(saida.contains("Saldo: R$ 1.010,00"));
        assertTrue(saida.contains("Aplicação encerrada."));
    }

    @ParameterizedTest
    @CsvSource({
        "4, 0, Valor do depósito deve ser maior que zero.",
        "4, -1, Valor do depósito deve ser maior que zero.",
        "3, 0, Valor do saque deve ser maior que zero.",
        "3, -1, Valor do saque deve ser maior que zero.",
        "3, 11, Saldo insuficiente para saque."
    })
    void rejeitaOperacaoSemMensagemDeSucessoESemAlterarSaldo(String opcao, String valor, String erro) {
        String saida = executar("1\n1\n101\nBreno\n10\n" + opcao + "\n" + valor + "\n2\n5\n3\n");

        assertTrue(saida.contains("Erro: " + erro));
        assertTrue(saida.contains("Saldo: R$ 10,00"));
        assertFalse(saida.contains("Depósito realizado com sucesso!"));
        assertFalse(saida.contains("Saque realizado com sucesso!"));
    }

    @Test
    void rejeitaSaldoInicialNegativoEPermiteNovoCadastro() {
        String saida = executar("1\n1\n101\nBreno\n-10\n1\n101\nBreno\n0\n2\n5\n3\n");

        assertTrue(saida.contains("Erro: Saldo inicial não pode ser negativo."));
        assertEquals(1, ocorrencias(saida, "Conta criada com sucesso!"));
        assertTrue(saida.contains("Saldo: R$ 0,00"));
    }

    @Test
    void repeteCamposDeCadastroInvalidosAteReceberEntradasValidas() {
        String saida = executar("1\n1\nx\n\n0\n2147483648\n101\n \nBreno\nNaN\n1.234\n10,00\n2\n5\n3\n");

        assertEquals(4, ocorrencias(saida, "Entrada inválida."));
        assertEquals(1, ocorrencias(saida, "Valor fora do intervalo."));
        assertEquals(2, ocorrencias(saida, "Valor monetário inválido."));
        assertTrue(saida.contains("Titular: Breno"));
        assertTrue(saida.contains("Saldo: R$ 10,00"));
    }

    @Test
    void preservaContaAoRetornarEImpedeSubstituicaoSilenciosa() {
        String saida = executar("1\n1\n101\nBreno\n5,00\n5\n1\n1\n2\n4\n5.00\n5\n2\n5\n1\n2\n5\n3\n");

        assertEquals(1, ocorrencias(saida, "Conta criada com sucesso!"));
        assertTrue(saida.contains("Já existe uma conta cadastrada. O cadastro atual foi preservado."));
        assertEquals(2, ocorrencias(saida, "Titular: Breno"));
        assertTrue(saida.contains("Saldo: R$ 5,00"));
        assertTrue(saida.contains("Saldo: R$ 10,00"));
        assertTrue(saida.contains("Aplicação encerrada."));
    }

    @Test
    void informaAusenciaDeContaAntesDeConsultaSaqueEDeposito() {
        String saida = executar("1\n2\n3\n4\n5\n3\n");

        assertEquals(3, ocorrencias(saida, "Nenhuma conta cadastrada."));
        assertFalse(saida.contains("Valor para saque:"));
        assertFalse(saida.contains("Valor para depósito:"));
        assertFalse(saida.contains("realizado com sucesso!"));
    }

    @Test
    void repeteFormatoMonetarioInvalidoDuranteOperacoesEMantemPrecisao() {
        String saida = executar("1\n1\n101\nBreno\n0,10\n4\n1.000,50\n1e3\nNaN\n0.20\n3\nletras\n0,10\n2\n5\n3\n");

        assertEquals(4, ocorrencias(saida, "Valor monetário inválido."));
        assertEquals(1, ocorrencias(saida, "Depósito realizado com sucesso!"));
        assertEquals(1, ocorrencias(saida, "Saque realizado com sucesso!"));
        assertTrue(saida.contains("Saldo: R$ 0,20"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"3", "4"})
    void encerraEmEOFDuranteOperacaoSemInformarSucesso(String opcao) {
        String saida = executar("1\n1\n101\nBreno\n10\n" + opcao + "\n");

        assertTrue(saida.contains("Entrada encerrada. Aplicação finalizada."));
        assertFalse(saida.contains("Depósito realizado com sucesso!"));
        assertFalse(saida.contains("Saque realizado com sucesso!"));
    }
}
