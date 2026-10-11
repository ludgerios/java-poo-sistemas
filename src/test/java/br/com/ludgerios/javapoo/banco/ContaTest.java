package br.com.ludgerios.javapoo.banco;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ContaTest {
    @Test
    void criaContaENormalizaOsDados() {
        Conta conta = new Conta(123, " Ana Maria ", new BigDecimal("100"));

        assertAll(
                () -> assertEquals(123, conta.getNumeroConta()),
                () -> assertEquals("Ana Maria", conta.getTitular()),
                () -> assertEquals(new BigDecimal("100.00"), conta.getSaldo()));
    }

    @Test
    void rejeitaCadastroInvalido() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Conta(0, "Ana", BigDecimal.ZERO)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Conta(-1, "Ana", BigDecimal.ZERO)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Conta(1, null, BigDecimal.ZERO)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Conta(1, " ", BigDecimal.ZERO)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Conta(1, "Ana", null)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Conta(1, "Ana", new BigDecimal("-1"))),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Conta(1, "Ana", new BigDecimal("0.001"))));
    }

    @Test
    void permiteSaldoInicialZero() {
        assertEquals(new BigDecimal("0.00"), new Conta(1, "Ana", BigDecimal.ZERO).getSaldo());
    }

    @Test
    void movimentaDinheiroSemPerderCentavosOuAlterarIdentidade() {
        Conta conta = new Conta(123, "Ana", new BigDecimal("0.10"));

        conta.depositarDinheiro(new BigDecimal("0.20"));
        assertEquals(new BigDecimal("0.30"), conta.getSaldo());
        conta.sacarDinheiro(new BigDecimal("0.2900"));

        assertAll(
                () -> assertEquals(new BigDecimal("0.01"), conta.getSaldo()),
                () -> assertEquals(123, conta.getNumeroConta()),
                () -> assertEquals("Ana", conta.getTitular()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "0.001"})
    void rejeitaValoresInvalidosSemAlterarSaldo(String valor) {
        Conta conta = contaComSaldo();
        BigDecimal quantia = new BigDecimal(valor);

        assertThrows(IllegalArgumentException.class, () -> conta.depositarDinheiro(quantia));
        assertEquals(new BigDecimal("100.00"), conta.getSaldo());
        assertThrows(IllegalArgumentException.class, () -> conta.sacarDinheiro(quantia));
        assertEquals(new BigDecimal("100.00"), conta.getSaldo());
    }

    @Test
    void rejeitaOperacoesNulasSemAlterarSaldo() {
        Conta conta = contaComSaldo();

        assertThrows(IllegalArgumentException.class, () -> conta.depositarDinheiro(null));
        assertThrows(IllegalArgumentException.class, () -> conta.sacarDinheiro(null));
        assertEquals(new BigDecimal("100.00"), conta.getSaldo());
    }

    @Test
    void rejeitaSaqueAcimaDoSaldo() {
        Conta conta = contaComSaldo();

        assertThrows(IllegalArgumentException.class,
                () -> conta.sacarDinheiro(new BigDecimal("100.01")));
        assertEquals(new BigDecimal("100.00"), conta.getSaldo());
    }

    @Test
    void permiteSacarTodoOSaldo() {
        Conta conta = contaComSaldo();

        conta.sacarDinheiro(new BigDecimal("100"));

        assertEquals(new BigDecimal("0.00"), conta.getSaldo());
    }

    private static Conta contaComSaldo() {
        return new Conta(1, "Ana", new BigDecimal("100"));
    }
}
