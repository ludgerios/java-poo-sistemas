package br.com.ludgerios.javapoo.banco;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Conta educacional com saldo não negativo e valores exatos em centavos. */
public final class Conta {
    private final int numeroConta;
    private final String titular;
    private BigDecimal saldo;

    public Conta(int numeroConta, String titular, BigDecimal saldoInicial) {
        if (numeroConta <= 0) {
            throw new IllegalArgumentException("Número da conta deve ser positivo.");
        }
        if (titular == null || titular.isBlank()) {
            throw new IllegalArgumentException("Titular não pode ser nulo ou vazio.");
        }

        BigDecimal saldoValidado = normalizarValor(saldoInicial, "Saldo inicial");
        if (saldoValidado.signum() < 0) {
            throw new IllegalArgumentException("Saldo inicial não pode ser negativo.");
        }

        this.numeroConta = numeroConta;
        this.titular = titular.strip();
        this.saldo = saldoValidado;
    }

    public int getNumeroConta() {
        return numeroConta;
    }

    public String getTitular() {
        return titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void depositarDinheiro(BigDecimal valor) {
        BigDecimal deposito = validarOperacao(valor, "depósito");
        saldo = saldo.add(deposito);
    }

    public void sacarDinheiro(BigDecimal valor) {
        BigDecimal saque = validarOperacao(valor, "saque");
        if (saque.compareTo(saldo) > 0) {
            throw new IllegalArgumentException("Saldo insuficiente para saque.");
        }
        saldo = saldo.subtract(saque);
    }

    private static BigDecimal validarOperacao(BigDecimal valor, String operacao) {
        String campo = "Valor do " + operacao;
        BigDecimal valorValidado = normalizarValor(valor, campo);
        if (valorValidado.signum() <= 0) {
            throw new IllegalArgumentException(campo + " deve ser maior que zero.");
        }
        return valorValidado;
    }

    private static BigDecimal normalizarValor(BigDecimal valor, String campo) {
        if (valor == null) {
            throw new IllegalArgumentException(campo + " não pode ser nulo.");
        }
        try {
            // UNNECESSARY aceita zeros extras e rejeita frações de centavo sem arredondar.
            return valor.setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException(
                    campo + " deve ter no máximo duas casas decimais.", exception);
        }
    }
}
