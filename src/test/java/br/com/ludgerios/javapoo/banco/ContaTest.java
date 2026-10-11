package br.com.ludgerios.javapoo.banco;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ContaTest {
    @Test
    void criaContaComDadosValidos() {
        Conta conta = new Conta(123, "Ana Maria", new BigDecimal("100"));

        assertAll(
                () -> assertEquals(123, conta.getNumeroConta()),
                () -> assertEquals("Ana Maria", conta.getTitular()),
                () -> assertEquals(new BigDecimal("100.00"), conta.getSaldo()));
    }

    @Test
    void removeEspacosExternosSemAlterarNomeDoTitular() {
        Conta conta = new Conta(1, "\t Ana  Maria \u2003", BigDecimal.ZERO);

        assertEquals("Ana  Maria", conta.getTitular());
    }

    @Test
    void aceitaMaiorNumeroDeContaInteiro() {
        Conta conta = new Conta(Integer.MAX_VALUE, "Ana", BigDecimal.ZERO);

        assertEquals(Integer.MAX_VALUE, conta.getNumeroConta());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0})
    void rejeitaNumeroDeContaNaoPositivo(int numero) {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> new Conta(numero, "Ana", BigDecimal.ZERO));

        assertEquals("Número da conta deve ser positivo.", erro.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n", "\u2003"})
    void rejeitaTitularNuloOuEmBranco(String titular) {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> new Conta(1, titular, BigDecimal.ZERO));

        assertEquals("Titular não pode ser nulo ou vazio.", erro.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"0, 0.00", "0.000, 0.00", "1, 1.00", "1.2, 1.20", "1.230, 1.23", "1E+2, 100.00"})
    void padronizaSaldoInicialSemArredondar(String saldoInicial, String esperado) {
        Conta conta = new Conta(1, "Ana", new BigDecimal(saldoInicial));

        assertEquals(new BigDecimal(esperado), conta.getSaldo());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-0.01", "-1", "-200000000000000000.00"})
    void rejeitaSaldoInicialNegativo(String saldoInicial) {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> new Conta(1, "Ana", new BigDecimal(saldoInicial)));

        assertEquals("Saldo inicial não pode ser negativo.", erro.getMessage());
    }

    @Test
    void rejeitaSaldoInicialNulo() {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> new Conta(1, "Ana", null));

        assertEquals("Saldo inicial não pode ser nulo.", erro.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.001", "10.999", "-0.001"})
    void rejeitaSaldoInicialComFracaoDeCentavo(String saldoInicial) {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> new Conta(1, "Ana", new BigDecimal(saldoInicial)));

        assertEquals("Saldo inicial deve ter no máximo duas casas decimais.", erro.getMessage());
    }

    @Test
    void depositoValidoAumentaSaldoEPreservaIdentidade() {
        Conta conta = new Conta(123, "Ana", new BigDecimal("100.00"));

        conta.depositarDinheiro(new BigDecimal("12.30"));

        assertAll(
                () -> assertEquals(new BigDecimal("112.30"), conta.getSaldo()),
                () -> assertEquals(123, conta.getNumeroConta()),
                () -> assertEquals("Ana", conta.getTitular()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "-0.01", "0", "0.000"})
    void rejeitaDepositoNaoPositivoSemAlterarSaldo(String valor) {
        Conta conta = contaComSaldoDeCem();
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> conta.depositarDinheiro(new BigDecimal(valor)));

        assertAll(
                () -> assertEquals("Valor do depósito deve ser maior que zero.", erro.getMessage()),
                () -> assertEquals(new BigDecimal("100.00"), conta.getSaldo()));
    }

    @Test
    void rejeitaDepositoNuloSemAlterarSaldo() {
        Conta conta = contaComSaldoDeCem();
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> conta.depositarDinheiro(null));

        assertAll(
                () -> assertEquals("Valor do depósito não pode ser nulo.", erro.getMessage()),
                () -> assertEquals(new BigDecimal("100.00"), conta.getSaldo()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.009", "1.001"})
    void rejeitaDepositoComFracaoDeCentavoSemAlterarSaldo(String valor) {
        Conta conta = contaComSaldoDeCem();
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> conta.depositarDinheiro(new BigDecimal(valor)));

        assertAll(
                () -> assertEquals("Valor do depósito deve ter no máximo duas casas decimais.",
                        erro.getMessage()),
                () -> assertEquals(new BigDecimal("100.00"), conta.getSaldo()));
    }

    @Test
    void aceitaDepositoComZerosDecimaisExtras() {
        Conta conta = contaComSaldoDeCem();

        conta.depositarDinheiro(new BigDecimal("2.5000"));

        assertEquals(new BigDecimal("102.50"), conta.getSaldo());
    }

    @Test
    void saqueValidoDiminuiSaldo() {
        Conta conta = contaComSaldoDeCem();

        conta.sacarDinheiro(new BigDecimal("12.30"));

        assertEquals(new BigDecimal("87.70"), conta.getSaldo());
    }

    @Test
    void permiteSaqueDoSaldoInteiro() {
        Conta conta = contaComSaldoDeCem();

        conta.sacarDinheiro(new BigDecimal("100.00"));

        assertEquals(new BigDecimal("0.00"), conta.getSaldo());
    }

    @ParameterizedTest
    @ValueSource(strings = {"100.01", "1000.00"})
    void rejeitaSaqueAcimaDoSaldoSemAlterarSaldo(String valor) {
        Conta conta = contaComSaldoDeCem();
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> conta.sacarDinheiro(new BigDecimal(valor)));

        assertAll(
                () -> assertEquals("Saldo insuficiente para saque.", erro.getMessage()),
                () -> assertEquals(new BigDecimal("100.00"), conta.getSaldo()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "-0.01", "0", "0.000"})
    void rejeitaSaqueNaoPositivoSemAlterarSaldo(String valor) {
        Conta conta = contaComSaldoDeCem();
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> conta.sacarDinheiro(new BigDecimal(valor)));

        assertAll(
                () -> assertEquals("Valor do saque deve ser maior que zero.", erro.getMessage()),
                () -> assertEquals(new BigDecimal("100.00"), conta.getSaldo()));
    }

    @Test
    void rejeitaSaqueNuloSemAlterarSaldo() {
        Conta conta = contaComSaldoDeCem();
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> conta.sacarDinheiro(null));

        assertAll(
                () -> assertEquals("Valor do saque não pode ser nulo.", erro.getMessage()),
                () -> assertEquals(new BigDecimal("100.00"), conta.getSaldo()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.009", "1.001"})
    void rejeitaSaqueComFracaoDeCentavoSemAlterarSaldo(String valor) {
        Conta conta = contaComSaldoDeCem();
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> conta.sacarDinheiro(new BigDecimal(valor)));

        assertAll(
                () -> assertEquals("Valor do saque deve ter no máximo duas casas decimais.",
                        erro.getMessage()),
                () -> assertEquals(new BigDecimal("100.00"), conta.getSaldo()));
    }

    @Test
    void aceitaSaqueComZerosDecimaisExtras() {
        Conta conta = contaComSaldoDeCem();

        conta.sacarDinheiro(new BigDecimal("2.5000"));

        assertEquals(new BigDecimal("97.50"), conta.getSaldo());
    }

    @Test
    void mantemPrecisaoEmOperacoesDecimaisSucessivas() {
        Conta conta = new Conta(1, "Ana", new BigDecimal("0.10"));

        conta.depositarDinheiro(new BigDecimal("0.20"));
        assertEquals(new BigDecimal("0.30"), conta.getSaldo());
        for (int i = 0; i < 100; i++) {
            conta.depositarDinheiro(new BigDecimal("0.01"));
        }
        conta.sacarDinheiro(new BigDecimal("1.29"));

        assertEquals(new BigDecimal("0.01"), conta.getSaldo());
    }

    @Test
    void preservaCentavosEmValoresMaioresQueLimiteDeTiposPrimitivos() {
        BigDecimal saldoInicial = new BigDecimal("99999999999999999999999999.99");
        Conta conta = new Conta(1, "Ana", saldoInicial);

        conta.depositarDinheiro(new BigDecimal("0.01"));
        assertEquals(new BigDecimal("100000000000000000000000000.00"), conta.getSaldo());
        conta.sacarDinheiro(new BigDecimal("0.01"));

        assertEquals(saldoInicial, conta.getSaldo());
    }

    private static Conta contaComSaldoDeCem() {
        return new Conta(1, "Ana", new BigDecimal("100.00"));
    }
}
